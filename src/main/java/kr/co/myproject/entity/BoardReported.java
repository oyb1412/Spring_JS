package kr.co.myproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardReported {
    private int idx;
    private int boardIdx;
    private int userIdx;
    private ReportType reportType;
    private int count;
    private String indate;
}
