package kr.co.myproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Projects {
	private int idx;
	private String title;
	private String content;
	private String indate;
	private String introduce;
	private String thumbnail;
}
