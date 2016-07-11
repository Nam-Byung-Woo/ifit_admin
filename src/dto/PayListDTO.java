package dto;

import java.util.List;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class PayListDTO {
	private int pay_seq;
	private String pg_success_number;
	private int total_price;
	private String delivery_address;
	private String delivery_address_detail;
	private String user_id;
	private String order_date;
	
	// make data
	private List<EachOrderDTO> eachOrderList;
	private String eachOrderListToJson;
}
