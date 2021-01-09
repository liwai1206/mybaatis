package com.yc.mavenstudy.domain;

import java.io.Serializable;
import java.util.List;

/**
*	mapper的封装类
* @author : 外哥
* 邮箱 ： liwai2012220663@163.com
* 创建时间:2021年1月8日 下午9:20:44
*/
public class MapperInfo implements Serializable{

	private String parameterType ;
	private String resultType ;
	private String sql ;
	private List<String> paramNames ;
	private boolean update = true ;
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	public String getSql() {
		return sql;
	}
	public void setSql(String sql) {
		this.sql = sql;
	}
	public List<String> getParamNames() {
		return paramNames;
	}
	public void setParamNames(List<String> paramNames) {
		this.paramNames = paramNames;
	}
	public boolean isUpdate() {
		return update;
	}
	public void setUpdate(boolean update) {
		this.update = update;
	}
	@Override
	public String toString() {
		return "MapperInfo [parameterType=" + parameterType + ", resultType=" + resultType + ", sql=" + sql
				+ ", paramNames=" + paramNames + ", update=" + update + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((paramNames == null) ? 0 : paramNames.hashCode());
		result = prime * result + ((parameterType == null) ? 0 : parameterType.hashCode());
		result = prime * result + ((resultType == null) ? 0 : resultType.hashCode());
		result = prime * result + ((sql == null) ? 0 : sql.hashCode());
		result = prime * result + (update ? 1231 : 1237);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapperInfo other = (MapperInfo) obj;
		if (paramNames == null) {
			if (other.paramNames != null)
				return false;
		} else if (!paramNames.equals(other.paramNames))
			return false;
		if (parameterType == null) {
			if (other.parameterType != null)
				return false;
		} else if (!parameterType.equals(other.parameterType))
			return false;
		if (resultType == null) {
			if (other.resultType != null)
				return false;
		} else if (!resultType.equals(other.resultType))
			return false;
		if (sql == null) {
			if (other.sql != null)
				return false;
		} else if (!sql.equals(other.sql))
			return false;
		if (update != other.update)
			return false;
		return true;
	}
	
	
	
}
