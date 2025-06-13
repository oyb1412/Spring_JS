package kr.co.myproject.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardVote{
    private int idx;
    private int boardIdx;
    private int userIdx;
    private String voteType;
}