package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class FaqDTO {
	private int faq_seq;
	private String title;				
	private String content;			
}
