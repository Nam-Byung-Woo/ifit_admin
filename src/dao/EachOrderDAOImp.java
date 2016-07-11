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

import dto.EachOrderDTO;

@Repository
public class EachOrderDAOImp implements IfitDAO {

	private EachOrderDTO eachOrderDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " each_order  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public EachOrderDAOImp(){
	
	}
	
	//	조건에 맞는 관리자목록
	public Object getOneRow(Map<String, Object> paramMap) {	
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<EachOrderDTO> list = new ArrayList<EachOrderDTO>();
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
        
        list  = this.jdbcTemplate.query(sql,sqlMap,new BeanPropertyRowMapper(EachOrderDTO.class));
        this.eachOrderDTO = (list.size() == 1) ? list.get(0) : null;
        return this.eachOrderDTO;
	}
	
//	LIST
	public Object getList(Map<String, Object> paramMap) {
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<EachOrderDTO> list = new ArrayList<EachOrderDTO>();
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
			sql += "	SELECT EO.order_seq, EO.pay_seq, EO.p_id, EO.color_id, EO.size_id, EO.amount, EO.price, EO.delivery_number, EO.admin_seq, EO.state, PL.p_name,  	\n";
			sql += "	CASE		\n";
			sql += "	WHEN DATE_FORMAT(EO.success_date,'%p') = 'AM' THEN 		\n";
			sql += "	DATE_FORMAT(EO.success_date, '%Y.%m.%d 오전 %h:%i:%s')		\n";
			sql += "	ELSE		\n";
			sql += "	DATE_FORMAT(EO.success_date, '%Y.%m.%d 오후 %h:%i:%s')		\n";
			sql += "	END AS success_date		\n";
		}
		sql += " FROM "+ table_name + " EO join product_list PL ON EO.p_id = PL.p_id \n";
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
			list  = this.jdbcTemplate.query(sql, sqlMap, new BeanPropertyRowMapper(EachOrderDTO.class));
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
//		paramSource.addValue("title", ((FaqDTO) dto).getTitle(), Types.VARCHAR);
//		paramSource.addValue("content", ((FaqDTO) dto).getContent(), Types.VARCHAR);
//		
		int rtnInt = 0;
//		
//		try{
//			rtnInt = this.jdbcTemplate.update(sql, paramSource);
//		}catch(Exception e){
//			System.out.println("error::::"+e);
//		}
		
		if(rtnInt > 0){
			return rtnInt;
		}else{
			return 0;
		}
	}
	
	public int update(Object dto) {
		String sql = "";
		sql += "	UPDATE " + table_name + " SET		\n";
		sql += "	state = :state, delivery_number = :delivery_number, success_date = now()		\n";
		sql += "	where pay_seq = :pay_seq and admin_seq = :admin_seq	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("state", ((EachOrderDTO) dto).getState(), Types.NUMERIC);
		paramSource.addValue("delivery_number", ((EachOrderDTO) dto).getDelivery_number(), Types.VARCHAR);
		paramSource.addValue("pay_seq", ((EachOrderDTO) dto).getPay_seq(), Types.NUMERIC);
		paramSource.addValue("admin_seq", ((EachOrderDTO) dto).getAdmin_seq(), Types.NUMERIC);
		
		if(this.jdbcTemplate.update(sql, paramSource) > 0){
			return ((EachOrderDTO) dto).getOrder_seq();
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
		int order_seq = paramMap.containsKey("order_seq") ? (int)paramMap.get("order_seq") : 0;
		
		String sql = "";
		sql += "	DELETE FROM " + table_name + "	\n";
		sql += "	WHERE order_seq = :order_seq	\n";

//		SqlLobValue lobValue = new SqlLobValue(dto.getBbs_content(), lobHandler);

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("order_seq", order_seq, Types.NUMERIC);
		int rtnInt = this.jdbcTemplate.update(sql, paramSource);
		if(rtnInt > 0){
			return rtnInt;
		}else{
			return 0;
		}
	}
}
