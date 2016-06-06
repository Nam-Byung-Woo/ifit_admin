package dao;

import java.util.Map;

public interface IfitDAO {
	public Object getOneRow(Map<String, Object> paramMap);	//	one row
	public Object getList(Map<String, Object> paramMap);			//	List
	public int write(Object object);										//	Write
	public int update(Object object);										//	update
	public int delete(Map<String, Object> paramMap);				//	Delete
}
