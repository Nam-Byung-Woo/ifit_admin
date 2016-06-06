package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class MainLabelListDTO {
	private int main_type;
	private String label_name;				
	private int label_order;			
}
