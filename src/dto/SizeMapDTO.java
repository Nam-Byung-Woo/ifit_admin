package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class SizeMapDTO {
	private int size_map_seq;		//	시퀀스
	private int p_id;					//	상품 seq
	private int size_id;				//	사이즈 id
}
