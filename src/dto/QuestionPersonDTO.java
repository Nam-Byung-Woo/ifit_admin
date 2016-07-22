package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class QuestionPersonDTO {
	private int quest_seq;
	private int state;
	private String user_id;
	private String title;
	private String content;		
	private String reply;
	private String quest_date;
	private String reply_date;
	
	// make data
	private String phoneNum;
}
