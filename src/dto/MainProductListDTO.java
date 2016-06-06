package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class MainProductListDTO {
	private int m_p_seq;
	private int p_id;
	private int main_type;				
	private int product_order;			
	
	// join data
	private String p_name;
}
