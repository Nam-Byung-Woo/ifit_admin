package dto;

import lombok.Data;
import lombok.Setter;
import lombok.AccessLevel;

@Data
public class UserListDTO {
	private int seq;
	private String user_id;				
	private String user_pw;				
	private String rest_key;			
	private String tel1;
	private String tel2;
	private String tel3;
	private String delivery_address_origin;			
	private String email;				
	private String nickname;
	private String route;
	private String regdate;		
}
