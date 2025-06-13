package kr.co.myproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	private int idx;
	private String username;
	private String password;
	private String writer;
	private Role role;
	private boolean ban;
	private String indate;
}
