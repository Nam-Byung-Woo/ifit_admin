package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class ColorListDTO {
	private int color_id;					
	private String color_val;
	private String color_name;
	private int color_order;
}
