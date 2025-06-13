package kr.co.myproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    private int idx;
	private String memID;
	private String title;
	private String content;
	private String writer;
	private String indate;
	private int viewCount;
	private int commentCount;
	private int upCount;
	private int downCount;
}
