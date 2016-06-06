package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class ColorMapDTO {
	private int color_map_seq;	//	시퀀스
	private int p_id;					//	상품 seq
	private int color_id;			//	color id
}
