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

import util.system.StringUtil;

import dto.ColorMapDTO;

@Repository
public class ColorMapDAOImp implements IfitDAO {

	private ColorMapDTO colorMapDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " color_map  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public ColorMapDAOImp(){
	
	}
	
	//	조건에 맞는 목록
	public Object getOneRow(Map<String, Object> paramMap) {	
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<ColorMapDTO> list = new ArrayList<ColorMapDTO>();
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		String sql = "";
		
		sqlMap.put("one", 1);
        sql = "	SELECT * FROM " + table_name + " WHERE :one = :one	\n";
        if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        sql += " ORDER BY color_map_seq DESC		\n";
        list  = this.jdbcTemplate.query(sql,sqlMap,new BeanPropertyRowMapper(ColorMapDTO.class));
        this.colorMapDTO = (list.size() == 1) ? list.get(0) : null;
        
        System.out.println("sql:::"+sql);
        
        return this.colorMapDTO;
	}
	
//	LIST
	public Object getList(Map<String, Object> paramMap) {
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<ColorMapDTO> list = new ArrayList<ColorMapDTO>();
		boolean isCount = paramMap.containsKey("isCount") ? (boolean)paramMap.get("isCount") : false;
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		int pageNum = paramMap.containsKey("pageNum") ? (int)paramMap.get("pageNum") : 0;
		int countPerPage = paramMap.containsKey("countPerPage") ? (int)paramMap.get("countPerPage") : 0;
		int startNum = (pageNum-1)*countPerPage;
		
		String sql = "";
		
		sqlMap.put("one", 1);
		sqlMap.put("startNum", startNum);
		sqlMap.put("countPerPage", countPerPage);
		
		if(isCount){
			sql += "	SELECT COUNT(*)	\n";
		}else{
			sql += "	SELECT p_id, color_id, 	\n";
			sql += "	color_map_seq	\n";
		}
        sql += " FROM "+ table_name + " T WHERE :one = :one \n";
        if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        if(isCount || pageNum==0){
		}else{
			sql += " ORDER BY color_map_seq DESC		\n";
			sql += " LIMIT :startNum, :countPerPage	\n";
		}
        System.out.println("sql:::"+sql);
        
        if(isCount){
        	return this.jdbcTemplate.queryForInt(sql,sqlMap);
		}else{
			list  = this.jdbcTemplate.query(sql, sqlMap, new BeanPropertyRowMapper(ColorMapDTO.class));
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(p_id, color_id)	\n";
		sql += "	values(:p_id, :color_id)	\n";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("p_id", ((ColorMapDTO) dto).getP_id(), Types.NUMERIC);
		paramSource.addValue("color_id", ((ColorMapDTO) dto).getColor_id(), Types.NUMERIC);
		
		int rtnInt = 0;
		
		try{
			rtnInt = this.jdbcTemplate.update(sql, paramSource);
		}catch(Exception e){
			System.out.println("error::::"+e);
		}
		
		if(rtnInt > 0){
			return rtnInt;
		}else{
			return 0;
		}
	}
	
	public int update(Object dto) {
		String sql = "";
		sql += "	UPDATE " + table_name + " SET	\n";
		sql += "	p_id = :p_id, color_id = :color_id	\n";
		sql += "	where color_map_seq = :color_map_seq	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("p_id", ((ColorMapDTO) dto).getP_id(), Types.NUMERIC);
		paramSource.addValue("color_id", ((ColorMapDTO) dto).getColor_id(), Types.NUMERIC);
		paramSource.addValue("color_map_seq", ((ColorMapDTO) dto).getColor_map_seq(), Types.NUMERIC);
		
		if(this.jdbcTemplate.update(sql, paramSource) > 0){
			return ((ColorMapDTO) dto).getColor_map_seq();
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
		int p_id = paramMap.containsKey("p_id") ? (int)paramMap.get("p_id") : 0;
		
		String sql = "";
		sql += "	DELETE FROM " + table_name + "	\n";
		sql += "	WHERE p_id = :p_id	\n";

//		SqlLobValue lobValue = new SqlLobValue(dto.getBbs_content(), lobHandler);

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("p_id", p_id, Types.NUMERIC);
		int rtnInt = this.jdbcTemplate.update(sql, paramSource);
		if(rtnInt > 0){
			return rtnInt;
		}else{
			return 0;
		}
	}
}
