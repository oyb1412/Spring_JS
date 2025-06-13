package kr.co.myproject.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import kr.co.myproject.entity.BoardVote;

@Mapper
public interface BoardVoteMapper {
    @Insert("INSERT INTO springboot_project_study.boardvote(boardIdx, userIdx, voteType) VALUES(#{boardIdx}, #{userIdx}, #{voteType})")
	public int boardVoteInsert(BoardVote board);

    @Select("SELECT COUNT(*) FROM springboot_project_study.boardvote WHERE userIdx=#{userIdx} and boardIdx=#{boardIdx}")
	public int findBoardVoteCount(int userIdx, int boardIdx);
}
