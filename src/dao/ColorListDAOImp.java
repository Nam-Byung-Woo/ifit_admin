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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import util.system.StringUtil;

import dto.ColorListDTO;

@Repository
public class ColorListDAOImp implements IfitDAO {

	private ColorListDTO colorListDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " color_list  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public ColorListDAOImp(){
	
	}
	
	//	조건에 맞는 목록
	public Object getOneRow(Map<String, Object> paramMap) {	
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<ColorListDTO> list = new ArrayList<ColorListDTO>();
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		String sql = "";
		
		sqlMap.put("one", 1);
		
        sql = "	SELECT * FROM "+ table_name + " WHERE :one = :one	\n";
        if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        list  = this.jdbcTemplate.query(sql,sqlMap,new BeanPropertyRowMapper(ColorListDTO.class));
        this.colorListDTO = (list.size() == 1) ? list.get(0) : null;
        return this.colorListDTO;
	}
	
//	LIST
	public Object getList(Map<String, Object> paramMap) {
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<ColorListDTO> list = new ArrayList<ColorListDTO>();
		boolean isCount = paramMap.containsKey("isCount") ? (boolean)paramMap.get("isCount") : false;
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		int pageNum = paramMap.containsKey("pageNum") ? (int)paramMap.get("pageNum") : 0;
		int countPerPage = paramMap.containsKey("countPerPage") ? (int)paramMap.get("countPerPage") : 0;
		int startNum = (pageNum-1)*countPerPage;
		String customSql = paramMap.containsKey("customSQL") ? paramMap.get("customSQL").toString() : "";
		
		String sql = "";
		
		sqlMap.put("one", 1);
		sqlMap.put("startNum", startNum);
		sqlMap.put("countPerPage", countPerPage);
		
		if(isCount){
			sql += "	SELECT COUNT(*)	\n";
		}else{
			sql += "	SELECT color_id, color_val, color_name, color_order 	\n";
		}
        sql += " FROM "+ table_name + " WHERE :one = :one \n";
        if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        if(customSql!=null && !customSql.equals("")){
        	sql += " and " + customSql + "		\n";
        }
        
        sql += " ORDER BY color_order ASC		\n";
        
        if(isCount || pageNum==0){
		}else{
			sql += " LIMIT :startNum, :countPerPage	\n";
		}
        
        System.out.println("sql:::"+sql);
        
        if(isCount){
        	return this.jdbcTemplate.queryForInt(sql,sqlMap);
		}else{
			list  = this.jdbcTemplate.query(sql, sqlMap, new BeanPropertyRowMapper(ColorListDTO.class));
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(color_val, color_name, color_order)	\n";
		sql += "	values(:color_val, :color_name, :color_order)	\n";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("color_val", ((ColorListDTO) dto).getColor_val(), Types.VARCHAR);
		paramSource.addValue("color_name", ((ColorListDTO) dto).getColor_name(), Types.VARCHAR);
		paramSource.addValue("color_order", ((ColorListDTO) dto).getColor_order(), Types.NUMERIC);
		
		int rtnInt = 0;
		
		KeyHolder keyHolder = new GeneratedKeyHolder(); 
		try{
			rtnInt = this.jdbcTemplate.update(sql, paramSource, keyHolder);
		}catch(Exception e){
			System.out.println("error::::"+e);
		}
		
		long longKey = keyHolder.getKey().longValue();
		
		if(rtnInt > 0){
			return StringUtil.longToInt(longKey);
		}else{
			return 0;
		}
	}

	public int update(Object dto) {
		String sql = "";
		sql += "	UPDATE " + table_name + " SET	\n";
		sql += "	color_val = :color_val, color_name = :color_name, color_order = :color_order	\n";
		sql += "	where color_id = :color_id		\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("color_val", ((ColorListDTO) dto).getColor_val(), Types.VARCHAR);
		paramSource.addValue("color_name", ((ColorListDTO) dto).getColor_name(), Types.VARCHAR);
		paramSource.addValue("color_order", ((ColorListDTO) dto).getColor_order(), Types.NUMERIC);
		paramSource.addValue("color_id", ((ColorListDTO) dto).getColor_id(), Types.NUMERIC);
		
		if(this.jdbcTemplate.update(sql, paramSource) > 0){
			return ((ColorListDTO) dto).getColor_id();
		}else{
			return 0;
		}
	}
	
	//	DELETE
	public int delete(Map<String, Object> paramMap) {
//		int next_seq = getMaxSeq();
//		if(next_seq == 0){
//			next_seq = 1;
//		}
		int color_id = paramMap.containsKey("color_id") ? (int)paramMap.get("color_id") : 0;
		
		String sql = "";
		sql += "	DELETE FROM " + table_name + "	\n";
		sql += "	WHERE color_id = :color_id	\n";

//		SqlLobValue lobValue = new SqlLobValue(dto.getBbs_content(), lobHandler);

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("color_id", color_id, Types.NUMERIC);
		int rtnInt = this.jdbcTemplate.update(sql, paramSource);
		if(rtnInt > 0){
			return rtnInt;
		}else{
			return 0;
		}
	}
}
