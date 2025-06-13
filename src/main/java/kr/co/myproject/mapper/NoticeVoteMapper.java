package kr.co.myproject.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.co.myproject.entity.NoticeVote;

@Mapper
public interface NoticeVoteMapper {
    @Insert("INSERT INTO springboot_project_study.noticevote(noticeIdx, userIdx, voteType) VALUES(#{noticeIdx}, #{userIdx}, #{voteType})")
	public int noticeVoteInsert(NoticeVote notice);

    @Select("SELECT COUNT(*) FROM springboot_project_study.noticevote WHERE userIdx=#{userIdx} and noticeIdx=#{noticeIdx}")
	public int findNoticeVoteCount(int userIdx, int noticeIdx);
}
