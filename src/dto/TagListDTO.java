package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class TagListDTO {
	private int tag_seq;			//	시퀀스
	private int p_id;				//	상품 seq
	private String tag;			//	사이즈 id
	private String allTag;		//	사이즈 row 전체
}
