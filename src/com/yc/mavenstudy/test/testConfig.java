package com.yc.mavenstudy.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;

import com.yc.mavenstudy.core.MyBatisConfig;
import com.yc.mavenstudy.core.SqlSession;
import com.yc.mavenstudy.core.SqlSessionFactory;
import com.yc.mavenstudy.domain.Account;
import com.yc.mavenstudy.util.DBHelper_Mysql;

/**
*
* @author : 外哥
* 邮箱 ： liwai2012220663@163.com
* 创建时间:2021年1月8日 下午8:34:08
*/
public class testConfig {
	@Test
	public void ConfigTest() {
		new MyBatisConfig("mybatis-config.xml") ;
	} 
	
	@Test
	public void testDBHelper() {
		DataSource ds = DBHelper_Mysql.getDs(); 
		System.out.println( ds == null );
	}
	
	@Test
	public void testSqlSessionAccount() {
		Account account = new Account("mysession", 1.0 ) ;
		SqlSession session = new SqlSession() ;
		int update = -1 ;
		try {
			update = session.update( "MyMapper.add", account ) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println( update );
	}
	
	@Test
	public void testSqlSessionMap() {
		Map<String, Object> map = new HashMap<String, Object>() ;
		map.put( "name", "map") ;
		map.put("money", 2.0) ;
		SqlSession session = new SqlSession() ;
		int update = -1 ;
		try {
			update = session.update( "MyMapper.add1", map ) ;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println( update );
	}
	
	@Test
	public void testSelectList() throws Exception {
		 SqlSessionFactory factory = new SqlSessionFactory() ;
		 SqlSession session = factory.openSession();
		 
		 List<Map<String, Object>> list = session.selectList("MyMapper.findAll" , null ) ;
		 
		 list.forEach( System.out :: println );
	}
	
	@Test
	public void testSelectList2() throws Exception {
		 SqlSessionFactory factory = new SqlSessionFactory() ;
		 SqlSession session = factory.openSession();
		 
		 List<Map<String, Object>> list = session.selectList("MyMapper.findById" , 19 ) ;
		 
		 list.forEach( System.out :: println );
		 
	}
}
