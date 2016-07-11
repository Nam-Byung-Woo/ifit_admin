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

import dto.PayListDTO;

@Repository
public class PayListDAOImp implements IfitDAO {

	private PayListDTO payListDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " pay_list  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public PayListDAOImp(){
	
	}
	
	public Object getOneRow(Map<String, Object> paramMap) {	
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<PayListDTO> list = new ArrayList<PayListDTO>();
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		String sql = "";
		
		sqlMap.put("one", 1);

		sql += "	SELECT quest_seq, state, user_id, title, content, reply,  	\n";
		sql += "	CASE		\n";
		sql += "	WHEN DATE_FORMAT(quest_date,'%p') = 'AM' THEN 		\n";
		sql += "	DATE_FORMAT(quest_date, '%Y.%m.%d 오전 %h:%i:%s')		\n";
		sql += "	ELSE		\n";
		sql += "	DATE_FORMAT(quest_date, '%Y.%m.%d 오후 %h:%i:%s')		\n";
		sql += "	END AS quest_date,		\n";
		sql += "	CASE		\n";
		sql += "	WHEN DATE_FORMAT(reply_date,'%p') = 'AM' THEN 		\n";
		sql += "	DATE_FORMAT(reply_date, '%Y.%m.%d 오전 %h:%i:%s')		\n";
		sql += "	ELSE		\n";
		sql += "	DATE_FORMAT(reply_date, '%Y.%m.%d 오후 %h:%i:%s')		\n";
		sql += "	END AS reply_date		\n";
				
        sql += "	FROM "+ table_name + " WHERE :one = :one	\n";
        if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        list  = this.jdbcTemplate.query(sql,sqlMap,new BeanPropertyRowMapper(PayListDTO.class));
        this.payListDTO = (list.size() == 1) ? list.get(0) : null;
        return this.payListDTO;
	}
	
//	LIST
	public Object getList(Map<String, Object> paramMap) {
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<PayListDTO> list = new ArrayList<PayListDTO>();
		boolean isCount = paramMap.containsKey("isCount") ? (boolean)paramMap.get("isCount") : false;
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		Map<String, Object> searchMap = (Map<String, Object>) (paramMap.containsKey("searchMap") ? paramMap.get("searchMap") : null);
		int pageNum = paramMap.containsKey("pageNum") ? (int)paramMap.get("pageNum") : 0;
		int countPerPage = paramMap.containsKey("countPerPage") ? (int)paramMap.get("countPerPage") : 0;
		int startNum = (pageNum-1)*countPerPage;
		int tabID = paramMap.containsKey("tabID") ? (int)paramMap.get("tabID") : 0;
		String sortCol = paramMap.containsKey("sortCol") ? (String)paramMap.get("sortCol") : "";
		String sortVal = paramMap.containsKey("sortVal") ? (String)paramMap.get("sortVal") : "";
		
		String sql = "";
		
		sqlMap.put("one", 1);
		sqlMap.put("startNum", startNum);
		sqlMap.put("countPerPage", countPerPage);
		
		if(isCount){
			sql += "	SELECT COUNT(DISTINCT PAY.pay_seq)	\n";
		}else{
			sql += "	SELECT DISTINCT PAY.pay_seq, PAY.pg_success_number, PAY.total_price, PAY.delivery_address, PAY.delivery_address_detail, PAY.user_id,  	\n";
			sql += "	CASE		\n";
			sql += "	WHEN DATE_FORMAT(PAY.order_date,'%p') = 'AM' THEN 		\n";
			sql += "	DATE_FORMAT(PAY.order_date, '%Y.%m.%d 오전 %h:%i:%s')		\n";
			sql += "	ELSE		\n";
			sql += "	DATE_FORMAT(PAY.order_date, '%Y.%m.%d 오후 %h:%i:%s')		\n";
			sql += "	END AS order_date		\n";
		}
        sql += " FROM "+ table_name + " PAY join each_order EO ON PAY.pay_seq = EO.pay_seq \n";
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
        if(tabID!=0){
        	sqlMap.put("tabID", tabID);
            sql += " and EO.state = :tabID	\n";
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
			list  = this.jdbcTemplate.query(sql, sqlMap, new BeanPropertyRowMapper(PayListDTO.class));
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
//		String sql = "";
//		sql += "	INSERT INTO " + table_name + "	\n";
//		sql += "	(title, content)	\n";
//		sql += "	values(:title, :content)	\n";
//
//		MapSqlParameterSource paramSource = new MapSqlParameterSource();
//		paramSource.addValue("title", ((QuestionPersonDTO) dto).getTitle(), Types.VARCHAR);
//		paramSource.addValue("content", ((QuestionPersonDTO) dto).getContent(), Types.VARCHAR);
//		
//		int rtnInt = 0;
//		
//		try{
//			rtnInt = this.jdbcTemplate.update(sql, paramSource);
//		}catch(Exception e){
//			System.out.println("error::::"+e);
//		}
//		
//		if(rtnInt > 0){
//			return rtnInt;
//		}else{
			return 0;
//		}
	}
	
	public int update(Object dto) {
//		String sql = "";
//		sql += "	UPDATE " + table_name + " SET	\n";
//		sql += "	reply = :reply, reply_date = :reply_date, state = :state	, reply_date = now()	\n";
//		sql += "	where quest_seq = :quest_seq	\n";
//		
//		MapSqlParameterSource paramSource = new MapSqlParameterSource();
//		paramSource.addValue("reply", ((PayListDTO) dto).getReply(), Types.VARCHAR);
//		paramSource.addValue("reply_date", ((PayListDTO) dto).getReply_date(), Types.VARCHAR);
//		paramSource.addValue("state", ((PayListDTO) dto).getState(), Types.NUMERIC);
//		paramSource.addValue("quest_seq", ((PayListDTO) dto).getQuest_seq(), Types.NUMERIC);
//		
//		if(this.jdbcTemplate.update(sql, paramSource) > 0){
//			return ((PayListDTO) dto).getQuest_seq();
//		}else{
			return 0;
//		}
	}
	
	//	DELETE
	public int delete(Map<String, Object> paramMap) {
//		int next_seq = getMaxSeq();
//		if(next_seq == 0){
//			next_seq = 1;
//		}
		int quest_seq = paramMap.containsKey("quest_seq") ? (int)paramMap.get("quest_seq") : 0;
		
		String sql = "";
		sql += "	DELETE FROM " + table_name + "	\n";
		sql += "	WHERE quest_seq = :quest_seq	\n";
//
////		SqlLobValue lobValue = new SqlLobValue(dto.getBbs_content(), lobHandler);

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("quest_seq", quest_seq, Types.NUMERIC);
		int rtnInt = this.jdbcTemplate.update(sql, paramSource);
		if(rtnInt > 0){
			return rtnInt;
		}else{
			return 0;
		}
	}
}
