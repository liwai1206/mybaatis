package com.yc.mavenstudy.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.yc.mavenstudy.core.MyBatisConfig;

/**
 * Druid连接池的工具类
 * 在使用spring框架时，只需要提供获取连接池的方法和静态块的代码
 * @author 外哥 
 */
@SuppressWarnings("all")
public class DBHelper_Mysql {
//	1.定义成员变量DataSource
	private static DataSource  ds ;
	
	/*
	 * 获取连接池的方法
	 */
	public static DataSource getDs() {
		return ds;
	} 

	/*
	 * 2.加载配置文件，获取连接池对象
	 */
	static {
		try {
//			2.加载配置文件
			Properties pso = new Properties();
//			InputStream is = DBHelper_Mysql.class.getClassLoader().getResourceAsStream("druid.properties") ;
//			pso.load(is);
			MyBatisConfig myBatisConfig = new MyBatisConfig("mybatis-config.xml") ;
			Map<String, String> map = myBatisConfig.getDataSourceMap();
			 
			map.forEach( ( key , val ) -> {
				pso.put( key, val) ;
			});
			
//			获取连接池对象DataSource
			ds = DruidDataSourceFactory.createDataSource(pso) ;    
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
}
