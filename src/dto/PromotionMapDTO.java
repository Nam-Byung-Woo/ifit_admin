package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class PromotionMapDTO {
	private int pro_map_seq;
	private int p_id;
	private int pro_seq;				
	private int map_order;			
	
	// join data
	private String p_name;
}
