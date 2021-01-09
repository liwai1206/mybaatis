package com.yc.mavenstudy.core;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

import com.yc.mavenstudy.domain.MapperInfo;

/**
* 解析配置文件的类
* @author : 外哥
* 邮箱 ： liwai2012220663@163.com
* 创建时间:2021年1月8日 下午8:25:10
*/
@SuppressWarnings("all")
public class MyBatisConfig {
	private Map<String , String> dataSourceMap = new HashMap<>() ;
	private List<String> mappers = new ArrayList<>() ;
	private static Map<String, MapperInfo> mapperInfos = new HashMap<>() ;
	
	public MyBatisConfig( String config) {
		parseXML( config ) ;
		
		parseMapper() ;
		
//		for (String s : mappers) {
//			System.out.println( s );
//		}
//		
//		dataSourceMap.forEach( ( key ,val) -> {
//			System.out.println( key + ":" + val );
//		});
		
//		System.out.println( mapperInfos );
		
//		mapperInfos.forEach( ( key ,val) -> {
//			System.out.println( key + ":" + val );
//		});
	}

	/**
	 * 解析映射文件
	 */
	private void parseMapper() {
		if ( mappers == null || mappers.isEmpty() ) {
			// 如果没有映射文件
			return ;
		}
		try {
			SAXReader reader = null ;
			Document doc = null ;
			String namespace = "" ;
			MapperInfo mapperInfo = null ;
			String sql = null ;
			String nodeName = null ;
			List<Element> nodes = null ;
			Pattern pattern = null ;
			Matcher matcher = null ;
			List<String> paramNames = null ;
			
			for (String mapper : mappers) {
				InputStream is = this.getClass().getClassLoader().getResourceAsStream(mapper);
				reader = new SAXReader() ;
				doc = reader.read(is) ;
				
				// 获取命名空间
				namespace = doc.getRootElement().attributeValue("namespace") ;
				
				if ( namespace != null && !"".equals(namespace)) {
					// 命名空间不为空
					namespace += "." ;
				}
				
				// 获取mappers节点下的所有子节点
				nodes = doc.selectNodes("/mapper/*") ;
				
				// 遍历所有节点
				for (Element el : nodes) {
					mapperInfo = new MapperInfo() ;
					// 获取节点名 select/insert等
					nodeName = el.getName() ;
					
					if ( "select".equalsIgnoreCase(nodeName)) {
						// 表示这是一个查询语句
						mapperInfo.setUpdate(false);
					}
					
					// 设置参数类型
					mapperInfo.setParameterType( el.attributeValue("parameterType") );
					// 设置返回值类型
					mapperInfo.setResultType( el.attributeValue("resultType"));
					// 获取sql语句
					sql = el.getTextTrim() ;
					// 创建一个正则表达式
					pattern = Pattern.compile("[#][{]\\w+}") ;
					// 匹配sql语句
					matcher = pattern.matcher(sql) ;
					
					paramNames = new ArrayList<>() ;
					// 迭代所有匹配到的结果
					while ( matcher.find() ) {
						// 将匹配到的结果中的#{}符号全部替换成空
						paramNames.add( matcher.group().replaceAll("[#{}]*", "")) ;
					}
					
					// 设置参数列表
					mapperInfo.setParamNames(paramNames);
					// 将sql语句中匹配成功的地方全部替换成？
					sql = matcher.replaceAll("?") ;
					// 设置sql语句
					mapperInfo.setSql(sql); 
					
					// 将这个节点的信息添加到集合中
					mapperInfos.put( namespace + el.attributeValue("id"), mapperInfo) ;
				}
				
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * 解析xml文件
	 * @param config
	 */
	private void parseXML(String config) {
		SAXReader reader = new SAXReader() ;
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(config);
		Document doc = null ;
		
		try {
			doc = reader.read(is) ;
			
			List<Element> list = doc.selectNodes("//dataSource/property");
			 
			for (Element el : list) {
				dataSourceMap.put(el.attributeValue("name"), el.attributeValue("value")) ;
			}
			
			list = doc.selectNodes("//mappers/mapper") ;
			for (Element el : list) {
				mappers.add(el.attributeValue("resource")) ;
			}
			
		} catch (DocumentException e) {
			e.printStackTrace();
		} 
	}

	public Map<String, String> getDataSourceMap() {
		return dataSourceMap;
	}

	public  List<String> getMappers() {
		return mappers;
	}

	public static MapperInfo getMapperInfo( String sqlId) {
		return mapperInfos.get(sqlId);
	}
	
	
	
	
}
