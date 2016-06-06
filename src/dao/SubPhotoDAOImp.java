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

import dto.SubPhotoDTO;

@Repository
public class SubPhotoDAOImp implements IfitDAO {

	private SubPhotoDTO subPhotoDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " sub_photo  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public SubPhotoDAOImp(){
	
	}
	
	//	목록
	public Object getOneRow(Map<String, Object> paramMap) {	
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<SubPhotoDTO> list = new ArrayList<SubPhotoDTO>();
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		String sql = "";
		
		sqlMap.put("one", 1);
		
        sql = "	SELECT * FROM, SUBSTRING_INDEX(photo_url, '/', -1) AS photo_url_name "+ table_name + "WHERE :one = :one 	\n";
        if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        list  = this.jdbcTemplate.query(sql,sqlMap,new BeanPropertyRowMapper(SubPhotoDTO.class));
        this.subPhotoDTO = (list.size() == 1) ? list.get(0) : null;
        return this.subPhotoDTO;
	}
	
//	LIST
	public Object getList(Map<String, Object> paramMap) {
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<SubPhotoDTO> list = new ArrayList<SubPhotoDTO>();
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
			sql += "	SELECT p_id, photo_url, photo_type, SUBSTRING_INDEX(photo_url, '/', -1) AS photo_url_name,	\n";
			sql += "	photo_seq	\n";
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
			sql += " ORDER BY photo_seq DESC		\n";
			sql += " LIMIT :startNum, :countPerPage	\n";
		}
        
        System.out.println("sql:::"+sql);
        
        if(isCount){
        	return this.jdbcTemplate.queryForInt(sql,sqlMap);
		}else{
			list  = this.jdbcTemplate.query(sql, sqlMap, new BeanPropertyRowMapper(SubPhotoDTO.class));
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(p_id, photo_url, photo_type)	\n";
		sql += "	values(:p_id, :photo_url, :photo_type)	\n";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("p_id", ((SubPhotoDTO) dto).getP_id(), Types.NUMERIC);
		paramSource.addValue("photo_url", ((SubPhotoDTO) dto).getPhoto_url(), Types.VARCHAR);
		paramSource.addValue("photo_type", ((SubPhotoDTO) dto).getPhoto_type(), Types.NUMERIC);
		
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
		sql += "	p_id = :p_id, photo_url = :photo_url, photo_type = :photo_type	\n";
		sql += "	where photo_seq = :photo_seq	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("p_id", ((SubPhotoDTO) dto).getP_id(), Types.NUMERIC);
		paramSource.addValue("photo_url", ((SubPhotoDTO) dto).getPhoto_url(), Types.VARCHAR);
		paramSource.addValue("photo_type", ((SubPhotoDTO) dto).getPhoto_type(), Types.NUMERIC);
		paramSource.addValue("photo_seq", ((SubPhotoDTO) dto).getPhoto_seq(), Types.NUMERIC);
		
		if(this.jdbcTemplate.update(sql, paramSource) > 0){
			return ((SubPhotoDTO) dto).getPhoto_seq();
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
		List<String> photo_url = (List<String>) (paramMap.containsKey("photo_url") ? paramMap.get("photo_url") : null);

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "";
		sql += "	DELETE FROM " + table_name + "	\n";
		sql += "	WHERE p_id = :p_id	\n";

        if(photo_url!=null){
        	sql += " and SUBSTRING_INDEX(photo_url,  '/', -1 ) IN(		\n";
        	for(int i=0; i<photo_url.size(); i++){
        		sql += i!=0 ? "," : "";
        		sql += (":photo_url"+i);
        		paramSource.addValue(("photo_url"+i), photo_url.get(i), Types.VARCHAR);
    		}
        	sql += ")	\n";
        }
		
//		SqlLobValue lobValue = new SqlLobValue(dto.getBbs_content(), lobHandler);
        
		paramSource.addValue("p_id", p_id, Types.NUMERIC);
		int rtnInt = this.jdbcTemplate.update(sql, paramSource);
		if(rtnInt > 0){
			return rtnInt;
		}else{
			return 0;
		}
	}
}
