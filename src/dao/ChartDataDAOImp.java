package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import dto.ChartDataDTO;

import util.system.StringUtil;

@Repository
public class ChartDataDAOImp implements IfitDAO {

	private NamedParameterJdbcTemplate jdbcTemplate;
	private List<ChartDataDTO> list = new ArrayList<ChartDataDTO>();
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public ChartDataDAOImp(){
	}
	
	public Object getOneRow(Map<String, Object> paramMap) {	
		return null;
	}
	
	public Object getList(Map<String, Object> paramMap) {
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		String sql = paramMap.containsKey("sql") ? (String)paramMap.get("sql") : "";
		String groupBy = paramMap.containsKey("groupBy") ? (String)paramMap.get("groupBy") : "";
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		
		sqlMap.put("one", 1);
		
		if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            }
        }
		
		if(!groupBy.equals("")){
			sql += "	group by " + groupBy	+"	\n";
		}
		
//        if(searchMap!=null && !searchMap.isEmpty()){
//            for( String key : searchMap.keySet() ){
//            	sqlMap.put(key, "%" + searchMap.get(key) + "%");
//            	sql += " and LOWER( "+key+" ) like LOWER( :"+key+" )";
//            }
//        }
//        
//        if(!sortCol.equals("")){
//        	sql += " ORDER BY " + sortCol + " " + sortVal + "		\n";
//        }
//        
//        if(isCount || pageNum==0){
//		}else{
//			sql += " LIMIT :startNum, :countPerPage	\n";
//		}
		
		System.out.println(sql);
        
		list  = this.jdbcTemplate.query(sql, sqlMap, new BeanPropertyRowMapper(ChartDataDTO.class));
        return list;
	}
	
	public int write(Object dto) {
		return 0;
	}
	
	public int update(Object dto) {
		return 0;
	}
	
	public int delete(Map<String, Object> paramMap) {
		return 0;
	}
}
