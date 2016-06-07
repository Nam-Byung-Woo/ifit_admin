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

import dto.PromotionMapDTO;

@Repository
public class PromotionMapDAOImp implements IfitDAO {

	private PromotionMapDTO promotionMapDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " promotion_map  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public PromotionMapDAOImp(){
	
	}
	
	//	조건에 맞는 관리자목록
	public Object getOneRow(Map<String, Object> paramMap) {	
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<PromotionMapDTO> list = new ArrayList<PromotionMapDTO>();
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		String sql = "";
		
		sqlMap.put("one", 1);
		
        sql = "	SELECT * FROM"+ table_name + "WHERE :one = :one	\n";
        if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        list  = this.jdbcTemplate.query(sql,sqlMap,new BeanPropertyRowMapper(PromotionMapDTO.class));
        this.promotionMapDTO = (list.size() == 1) ? list.get(0) : null;
        return this.promotionMapDTO;
	}
	
//	LIST
	public Object getList(Map<String, Object> paramMap) {
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<PromotionMapDTO> list = new ArrayList<PromotionMapDTO>();
		boolean isCount = paramMap.containsKey("isCount") ? (boolean)paramMap.get("isCount") : false;
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		Map<String, Object> searchMap = (Map<String, Object>) (paramMap.containsKey("searchMap") ? paramMap.get("searchMap") : null);
		int pageNum = paramMap.containsKey("pageNum") ? (int)paramMap.get("pageNum") : 0;
		int countPerPage = paramMap.containsKey("countPerPage") ? (int)paramMap.get("countPerPage") : 0;
		int startNum = (pageNum-1)*countPerPage;
		String sortCol = paramMap.containsKey("sortCol") ? (String)paramMap.get("sortCol") : "";
		String sortVal = paramMap.containsKey("sortVal") ? (String)paramMap.get("sortVal") : "";
		
		String sql = "";
		
		sqlMap.put("one", 1);
		sqlMap.put("startNum", startNum);
		sqlMap.put("countPerPage", countPerPage);
		
		if(isCount){
			sql += "	SELECT COUNT(*)	\n";
		}else{
			sql += "	SELECT PM.*, PL.p_name as p_name	\n";
		}
        sql += " FROM "+ table_name + " PM JOIN product_list PL ON PM.p_id = PL.p_id WHERE :one = :one \n";
        if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        if(searchMap!=null && !searchMap.isEmpty()){
            for( String key : searchMap.keySet() ){
            	sqlMap.put(key, "%" + searchMap.get(key) + "%");
            	sql += " and LOWER( "+key+" ) like LOWER( :"+key+" )";
            }
        }
        
        if(!sortCol.equals("")){
        	sql += " ORDER BY " + sortCol + " " + sortVal + "		\n";
        }
        
        if(isCount || pageNum==0){
		}else{
			sql += " LIMIT :startNum, :countPerPage	\n";
		}
        
        System.out.println("sql:::"+sql);
        
        if(isCount){
        	return this.jdbcTemplate.queryForInt(sql,sqlMap);
		}else{
			list  = this.jdbcTemplate.query(sql, sqlMap, new BeanPropertyRowMapper(PromotionMapDTO.class));
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(p_id, pro_seq, map_order)	\n";
		sql += "	values(:p_id, :pro_seq, :map_order)	\n";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("p_id", ((PromotionMapDTO) dto).getP_id(), Types.NUMERIC);
		paramSource.addValue("pro_seq", ((PromotionMapDTO) dto).getPro_seq(), Types.NUMERIC);
		paramSource.addValue("map_order", ((PromotionMapDTO) dto).getMap_order(), Types.NUMERIC);
		
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
		sql += "	map_order = :map_order 	\n";
		sql += "	where pro_map_seq = :pro_map_seq	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("map_order", ((PromotionMapDTO) dto).getMap_order(), Types.NUMERIC);
		paramSource.addValue("pro_map_seq", ((PromotionMapDTO) dto).getPro_map_seq(), Types.NUMERIC);
		
		if(this.jdbcTemplate.update(sql, paramSource) > 0){
			return ((PromotionMapDTO) dto).getPro_map_seq();
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
		int pro_map_seq = paramMap.containsKey("pro_map_seq") ? (int)paramMap.get("pro_map_seq") : 0;
		
		String sql = "";
		sql += "	DELETE FROM " + table_name + "	\n";
		sql += "	WHERE pro_map_seq = :pro_map_seq	\n";

//		SqlLobValue lobValue = new SqlLobValue(dto.getBbs_content(), lobHandler);

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("pro_map_seq", pro_map_seq, Types.NUMERIC);
		int rtnInt = this.jdbcTemplate.update(sql, paramSource);
		if(rtnInt > 0){
			return rtnInt;
		}else{
			return 0;
		}
	}
}
