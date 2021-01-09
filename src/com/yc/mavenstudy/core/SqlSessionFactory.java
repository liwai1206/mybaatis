package com.yc.mavenstudy.core;

/**
*
* @author : 外哥
* 邮箱 ： liwai2012220663@163.com
* 创建时间:2021年1月9日 下午2:32:07
*/
public class SqlSessionFactory {

	public SqlSessionFactory() {
		super(); 
	}
	
	public SqlSession openSession() {
		return new SqlSession() ;
	}
	
}
