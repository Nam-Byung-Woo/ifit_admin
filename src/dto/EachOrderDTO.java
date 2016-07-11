package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class EachOrderDTO {
	private int order_seq;
	private int pay_seq;
	private int p_id;
	private int color_id;
	private int size_id;
	private int amount;
	private int price;
	private String delivery_number;
	private int admin_seq;
	private int state;
	private String success_date;
	
	// join data
	private String p_name;
}
