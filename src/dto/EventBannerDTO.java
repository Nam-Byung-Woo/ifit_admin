package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class EventBannerDTO {
	private int banner_seq;
	private int banner_type;				
	private String banner_url;			
	
	// make data
	private String banner_url_name;
}
