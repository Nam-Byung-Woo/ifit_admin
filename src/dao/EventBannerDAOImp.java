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

import dto.EventBannerDTO;

import util.system.StringUtil;

@Repository
public class EventBannerDAOImp implements IfitDAO {

	private EventBannerDTO eventBannerDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " event_banner  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public EventBannerDAOImp(){
	
	}
	
	//	조건에 맞는 관리자목록
	public Object getOneRow(Map<String, Object> paramMap) {	
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<EventBannerDTO> list = new ArrayList<EventBannerDTO>();
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		String sql = "";
		
		sqlMap.put("one", 1);
		
        sql = "	SELECT EB.*, PL.p_name, SUBSTRING_INDEX(banner_url, '/', -1) AS banner_url_name		\n";
        sql += " FROM "+ table_name + " EB join product_list PL ON EB.p_id = PL.p_id \n";
        sql += " WHERE :one = :one \n";
        
        if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        list  = this.jdbcTemplate.query(sql,sqlMap,new BeanPropertyRowMapper(EventBannerDTO.class));
        this.eventBannerDTO = (list.size() == 1) ? list.get(0) : null;
        return this.eventBannerDTO;
	}
	
//	LIST
	public Object getList(Map<String, Object> paramMap) {
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<EventBannerDTO> list = new ArrayList<EventBannerDTO>();
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
			sql += "	SELECT EB.banner_type, EB.banner_url, EB.p_id, PL.p_name,	\n";
			sql += "	EB.banner_seq	\n";
		}
		
		sql += " FROM "+ table_name + " EB join product_list PL ON EB.p_id = PL.p_id \n";
        sql += " WHERE :one = :one \n";
		
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
			list  = this.jdbcTemplate.query(sql, sqlMap, new BeanPropertyRowMapper(EventBannerDTO.class));
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(banner_type, banner_url, p_id)	\n";
		sql += "	values(:banner_type, :banner_url, :p_id)	\n";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("banner_type", ((EventBannerDTO) dto).getBanner_type(), Types.NUMERIC);
		paramSource.addValue("banner_url", ((EventBannerDTO) dto).getBanner_url(), Types.VARCHAR);
		paramSource.addValue("p_id", ((EventBannerDTO) dto).getP_id(), Types.NUMERIC);
		
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
		sql += "	banner_type = :banner_type, banner_url = :banner_url, p_id = :p_id	\n";
		sql += "	where banner_seq = :banner_seq	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("banner_type", ((EventBannerDTO) dto).getBanner_type(), Types.NUMERIC);
		paramSource.addValue("banner_url", ((EventBannerDTO) dto).getBanner_url(), Types.VARCHAR);
		paramSource.addValue("p_id", ((EventBannerDTO) dto).getP_id(), Types.NUMERIC);
		paramSource.addValue("banner_seq", ((EventBannerDTO) dto).getBanner_seq(), Types.NUMERIC);
		
		if(this.jdbcTemplate.update(sql, paramSource) > 0){
			return ((EventBannerDTO) dto).getBanner_seq();
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
		int banner_seq = paramMap.containsKey("banner_seq") ? (int)paramMap.get("banner_seq") : 0;
		
		String sql = "";
		sql += "	DELETE FROM " + table_name + "	\n";
		sql += "	WHERE banner_seq = :banner_seq	\n";

//		SqlLobValue lobValue = new SqlLobValue(dto.getBbs_content(), lobHandler);

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("banner_seq", banner_seq, Types.NUMERIC);
		int rtnInt = this.jdbcTemplate.update(sql, paramSource);
		if(rtnInt > 0){
			return rtnInt;
		}else{
			return 0;
		}
	}
}
