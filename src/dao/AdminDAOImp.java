package dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import dto.AdminDTO;

@Repository
public class AdminDAOImp implements IfitDAO {

	private AdminDTO adminDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " ADMIN  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public AdminDAOImp(){
	
	}
	
	//	조건에 맞는 관리자목록
	public Object getOneRow(Map<String, Object> paramMap) {	
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<AdminDTO> list = new ArrayList<AdminDTO>();
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
        
        System.out.println(sql);
        
        list  = this.jdbcTemplate.query(sql,sqlMap,new BeanPropertyRowMapper(AdminDTO.class));
        this.adminDTO = (list.size() == 1) ? list.get(0) : null;
        return this.adminDTO;
	}
	
//	LIST
	public Object getList(Map<String, Object> paramMap) {
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<AdminDTO> list = new ArrayList<AdminDTO>();
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
			sql += "	SELECT ID, NAME, TEL1, TEL2, TEL3, 	\n";
			sql += "	SEQ,	\n";
			sql += "	CASE		\n";
			sql += "	WHEN DATE_FORMAT(regdate,'%p') = 'AM' THEN 		\n";
			sql += "	DATE_FORMAT(regdate, '%Y.%m.%d 오전 %h:%i:%s')		\n";
			sql += "	ELSE		\n";
			sql += "	DATE_FORMAT(regdate, '%Y.%m.%d 오후 %h:%i:%s')		\n";
			sql += "	END AS REGDATE, regdate as orig_regdate		\n";
		}
        sql += " FROM "+ table_name + " T WHERE :one = :one \n";
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
			list  = this.jdbcTemplate.query(sql, sqlMap, new BeanPropertyRowMapper(AdminDTO.class));
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(id, pw, name, tel1, tel2, tel3)	\n";
		sql += "	values(:id, :pw, :name, :tel1, :tel2, :tel3)	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("id", ((AdminDTO)dto).getId(), Types.VARCHAR);
		paramSource.addValue("pw", ((AdminDTO)dto).getPw(), Types.VARCHAR);
		paramSource.addValue("name", ((AdminDTO)dto).getName(), Types.VARCHAR);
		paramSource.addValue("tel1", ((AdminDTO)dto).getTel1(), Types.VARCHAR);
		paramSource.addValue("tel2", ((AdminDTO)dto).getTel2(), Types.VARCHAR);
		paramSource.addValue("tel3", ((AdminDTO)dto).getTel3(), Types.VARCHAR);
		
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
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		String sql = "";
		sql += "	UPDATE " + table_name + " SET	\n";
		sql += "	name = :name, tel1 = :tel1, tel2 = :tel2, tel3 = :tel3	\n";
		
		if(!((AdminDTO)dto).getId().equals("")){
			sql += ", id = :id	\n";
			paramSource.addValue("id", ((AdminDTO)dto).getId(), Types.VARCHAR);
		}
		
		if(!((AdminDTO)dto).getPw().equals("")){
			sql += ", pw = :pw	\n";
			paramSource.addValue("pw", ((AdminDTO)dto).getPw(), Types.VARCHAR);
		}
		
		sql += "	where seq = :seq	\n";
		
		paramSource.addValue("name", ((AdminDTO)dto).getName(), Types.VARCHAR);
		paramSource.addValue("tel1", ((AdminDTO)dto).getTel1(), Types.VARCHAR);
		paramSource.addValue("tel2", ((AdminDTO)dto).getTel2(), Types.VARCHAR);
		paramSource.addValue("tel3", ((AdminDTO)dto).getTel3(), Types.VARCHAR);
		paramSource.addValue("seq", ((AdminDTO)dto).getSeq(), Types.NUMERIC);
		
		if(this.jdbcTemplate.update(sql, paramSource) > 0){
			return ((AdminDTO)dto).getSeq();
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
		int seq = paramMap.containsKey("seq") ? (int)paramMap.get("seq") : 0;
		
		String sql = "";
		sql += "	DELETE FROM " + table_name + "	\n";
		sql += "	WHERE seq = :seq	\n";

//		SqlLobValue lobValue = new SqlLobValue(dto.getBbs_content(), lobHandler);

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("seq", seq, Types.NUMERIC);
		int rtnInt = this.jdbcTemplate.update(sql, paramSource);
		if(rtnInt > 0){
			return rtnInt;
		}else{
			return 0;
		}
	}
}
