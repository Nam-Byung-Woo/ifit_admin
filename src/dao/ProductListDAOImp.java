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

import dto.ProductListDTO;

@Repository
public class ProductListDAOImp implements IfitDAO {

	private ProductListDTO productListDTO; 
	private NamedParameterJdbcTemplate jdbcTemplate;
	private String table_name = " product_list  ";
	
	@Autowired
	public void setDataSource(DataSource dataSource){ 
		this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	
	public ProductListDAOImp(){
	
	}
	
	//	조건에 맞는 목록
	public Object getOneRow(Map<String, Object> paramMap) {	
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<ProductListDTO> list = new ArrayList<ProductListDTO>();
		Map<String, Object> whereMap = (Map<String, Object>) (paramMap.containsKey("whereMap") ? paramMap.get("whereMap") : null);
		String sql = "";
		
		sqlMap.put("one", 1);
        sql += "	SELECT PL.*, A.name AS admin_name, SUBSTRING_INDEX(PL.p_main_url, '/', -1) AS p_main_url_name, SUBSTRING_INDEX(PL.lookup_url, '/', -1) AS lookup_url_name	\n"; 
        sql += "	FROM "+ table_name + " PL JOIN ADMIN A ON PL.admin_seq = A.seq WHERE :one = :one	\n";
        if(whereMap!=null && !whereMap.isEmpty()){
            for( String key : whereMap.keySet() ){
            	sqlMap.put(key, whereMap.get(key));
            	sql += " and " + key + " = :"+key+"		\n";
            }
        }
        
        System.out.println("sql:::"+sql);
        
        list  = this.jdbcTemplate.query(sql,sqlMap,new BeanPropertyRowMapper(ProductListDTO.class));
        this.productListDTO = (list.size() == 1) ? list.get(0) : null;
        
        return this.productListDTO;
	}
	
//	LIST
	public Object getList(Map<String, Object> paramMap) {
		Map<String,Object> sqlMap = new HashMap<String,Object>();
		List<ProductListDTO> list = new ArrayList<ProductListDTO>();
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
			sql += "	SELECT PL.admin_seq, A.name AS ADMIN_NAME, PL.p_name, PL.p_price, PL.detail_info, PL.state, 	\n";
			sql += "	PL.p_id,	\n";
			sql += "	CASE		\n";
			sql += "	WHEN DATE_FORMAT(PL.regdate,'%p') = 'AM' THEN 		\n";
			sql += "	DATE_FORMAT(PL.regdate, '%Y.%m.%d 오전 %h:%i:%s')		\n";
			sql += "	ELSE		\n";
			sql += "	DATE_FORMAT(PL.regdate, '%Y.%m.%d 오후 %h:%i:%s')		\n";
			sql += "	END AS REGDATE, PL.regdate AS orig_regdate		\n";
		}
        sql += " FROM "+ table_name + " PL JOIN ADMIN A ON PL.admin_seq = A.seq WHERE :one = :one \n";
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
			list  = this.jdbcTemplate.query(sql, sqlMap, new BeanPropertyRowMapper(ProductListDTO.class));
	        return list;
		}
	}
	
	//	Write
	public int write(Object dto) {
		String sql = "";
		sql += "	INSERT INTO " + table_name + "	\n";
		sql += "	(p_name, p_main_url, lookup_url, p_price, detail_info, category, cat_ref, admin_seq, state)	\n";
		sql += "	values(:p_name, :p_main_url, :lookup_url, :p_price, :detail_info, :category, :cat_ref, :admin_seq, :state)	\n";

		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("p_name", ((ProductListDTO) dto).getP_name(), Types.VARCHAR);
		paramSource.addValue("p_main_url", ((ProductListDTO) dto).getP_main_url(), Types.VARCHAR);
		paramSource.addValue("lookup_url", ((ProductListDTO) dto).getLookup_url(), Types.VARCHAR);
		paramSource.addValue("p_price", ((ProductListDTO) dto).getP_price(), Types.NUMERIC);
		paramSource.addValue("detail_info", ((ProductListDTO) dto).getDetail_info(), Types.VARCHAR);
		paramSource.addValue("category", ((ProductListDTO) dto).getCategory(), Types.NUMERIC);
		paramSource.addValue("cat_ref", ((ProductListDTO) dto).getCat_ref(), Types.VARCHAR);
		paramSource.addValue("admin_seq", ((ProductListDTO) dto).getAdmin_seq(), Types.NUMERIC);
		paramSource.addValue("state", ((ProductListDTO) dto).getState(), Types.NUMERIC);
		
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
		sql += "	admin_seq = :admin_seq, p_name = :p_name, p_price = :p_price, detail_info = :detail_info, category = :category, cat_ref = :cat_ref, p_main_url = :p_main_url, lookup_url = :lookup_url, state = :state		\n";
		sql += "	where p_id = :p_id	\n";
		
		MapSqlParameterSource paramSource = new MapSqlParameterSource();
		paramSource.addValue("admin_seq", ((ProductListDTO) dto).getAdmin_seq(), Types.NUMERIC);
		paramSource.addValue("p_name", ((ProductListDTO) dto).getP_name(), Types.VARCHAR);
		paramSource.addValue("p_price", ((ProductListDTO) dto).getP_price(), Types.NUMERIC);
		paramSource.addValue("detail_info", ((ProductListDTO) dto).getDetail_info(), Types.VARCHAR);
		paramSource.addValue("category", ((ProductListDTO) dto).getCategory(), Types.NUMERIC);
		paramSource.addValue("cat_ref", ((ProductListDTO) dto).getCat_ref(), Types.VARCHAR);
		paramSource.addValue("p_main_url", ((ProductListDTO) dto).getP_main_url(), Types.VARCHAR);
		paramSource.addValue("lookup_url", ((ProductListDTO) dto).getLookup_url(), Types.VARCHAR);
		paramSource.addValue("state", ((ProductListDTO) dto).getState(), Types.NUMERIC);
		
		paramSource.addValue("p_id", ((ProductListDTO) dto).getP_id(), Types.NUMERIC);
		
		if(this.jdbcTemplate.update(sql, paramSource) > 0){
			return ((ProductListDTO) dto).getP_id();
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
