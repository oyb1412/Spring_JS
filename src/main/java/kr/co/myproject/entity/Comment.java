package kr.co.myproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    private int idx;
    private int parentIdx;
    private String writer;
    private String content;
    private String indate;
    private BoardType boardType;
}
