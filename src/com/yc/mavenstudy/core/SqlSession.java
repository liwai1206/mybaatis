package com.yc.mavenstudy.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import com.yc.mavenstudy.domain.MapperInfo;
import com.yc.mavenstudy.util.DBHelper_Mysql;

/**
*	执行方法
* @author : 外哥
* 邮箱 ： liwai2012220663@163.com
* 创建时间:2021年1月9日 下午2:31:51
*/
@SuppressWarnings("all")
public class SqlSession {
	
	private JdbcTemplate template = new JdbcTemplate( DBHelper_Mysql.getDs() ) ;
	
	public <T> List<T> selectList(  String sqlId, Object obj ) throws Exception{
		// 根据id获取mapper封装信息
			MapperInfo mapperInfo = MyBatisConfig.getMapperInfo( sqlId ) ;
			
			if ( mapperInfo == null ) {
				// 如果没有该id对应的封装信息
				throw new RuntimeException("没有你要执行的" + sqlId + "...") ;
			}
			
			// 获取sql语句
			String sql = mapperInfo.getSql() ; 
			// 获取所有的参数名称
			List<String> paramNames = mapperInfo.getParamNames();
			// 获取参数类型
			String parameterType = mapperInfo.getParameterType();
			// 获取参数值
			List<Object> params =  this.getParams( parameterType , paramNames , obj ) ;
			String resultType = mapperInfo.getResultType() ;
			
			if ( "map".equalsIgnoreCase(resultType)) {
				// 如果结果值类型为map
				return (List<T>) template.queryForList(sql, params.toArray() ) ;
			}
			
			// 如果结果集为对象
			Class<?> cls = Class.forName(resultType) ;
			return (List<T>) template.queryForList(sql, cls, params.toArray()) ;
	}
	
	
	/**
	 * 更新方法
	 * @param sqlId 要执行的方法
	 * @param obj	参数对象
	 * @return
	 * @throws Exception
	 */
	public int update( String sqlId, Object obj) throws Exception {
		// 根据id获取mapper封装信息
		MapperInfo mapperInfo = MyBatisConfig.getMapperInfo( sqlId ) ;
		
		System.out.println( mapperInfo );
		
		if ( mapperInfo == null ) {
			// 如果没有该id对应的封装信息
			throw new RuntimeException("没有你要执行的" + sqlId + "...") ;
		}
		
		// 获取sql语句
		String sql = mapperInfo.getSql() ;
		
		// 获取所有的参数名称
		List<String> paramNames = mapperInfo.getParamNames();
		
		if ( paramNames.isEmpty() || paramNames == null ) {
			// 如果没有参数 , 直接执行该sql语句，返回结果
			return template.update(sql) ;
		}
		
		// 如果有参数
		// 获取参数类型
		String parameterType = mapperInfo.getParameterType();
		
		List<Object> params =  this.getParams( parameterType , paramNames , obj ) ;
		 
		return template.update(sql, params.toArray() ) ;
	}

	/**
	 * 获取所有参数名对应参数的值
	 * @param parameterType
	 * @param paramNames
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private List<Object> getParams(String parameterType, List<String> paramNames, Object obj) throws Exception {
		if ( obj == null ) {
			// 
			return Collections.emptyList() ;
		}
		
		if ( parameterType == null || paramNames.isEmpty() ) {
			// 如果参数类型为空，则返回空集合
			return Collections.emptyList() ;
		}
		
		List<Object> params = new ArrayList<>() ;
		
		if ( "int".equalsIgnoreCase(parameterType) || "float".equalsIgnoreCase(parameterType) || "double".equalsIgnoreCase(parameterType) || "string".equalsIgnoreCase(parameterType)) {
			// 如果参数是基本类型，则直接将参数添加到列表
			 params.add(obj) ; 
		}else if ( "map".equalsIgnoreCase(parameterType) ) { 
			// 如果参数是map类型
			Map<String, Object> map = (Map<String, Object>) obj ;
			// 循环map集合，通过参数名称获取参数值，并存入集合中
			for (String name : paramNames) {
				params.add( map.get(name) ) ;
			}
		}else {
			// 否则参数是实体类
			// 获取类的class文件
			Class<?> cls = Class.forName(parameterType);
			// 获取实体类中的所有方法
			Method[] methods = cls.getDeclaredMethods();
			Map<String, Method> getters = new HashMap<>() ;
			String methodName = null ;
			
			// 循环所有方法，获取当前方法的方法名，将所有的get方法添加到getters集合中
			for (Method md : methods) {
				methodName = md.getName() ;
				
				if ( !methodName.startsWith("get")) {
					continue ;
				}
				
				getters.put(methodName, md) ;
			}
			
			Method method = null ;
			for (String name : paramNames) {
				// 获取方法名
				methodName = "get" + name.substring(0,1).toUpperCase() + name.substring(1) ;
				
				// 根据方法名获取方法
				method = getters.getOrDefault(methodName, null) ;
				if ( method == null ) {
					// 如果方法不存在
					continue ;
				}
				
				// 如果这个方法存在，则执行这个方法，并将值存入集合种
				params.add( method.invoke(obj)) ;
			}
		} 
		
		return params; 
	}
	
}
