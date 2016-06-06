package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class SubPhotoDTO {
	private int p_id;					//	상품 seq
	private String photo_url;			
	private int photo_type;		//	이미지종류(상단,상세)
	private int photo_seq;
	
	// make data
	private String photo_url_name;
}
