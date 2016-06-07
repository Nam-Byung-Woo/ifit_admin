package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class PromotionListDTO {
	private int pro_seq;
	private String pro_url;
	private String pro_name;
	private int pro_use;	
	
	// make data
	private String pro_url_name;
}
