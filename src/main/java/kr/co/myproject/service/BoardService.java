package kr.co.myproject.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.myproject.entity.Board;
import kr.co.myproject.mapper.BoardMapper;

@Service
public class BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	public int boardInsert(Board board)
	{
		return boardMapper.boardInsert(board);
	}

	public List<Board> getPagedList(int start, int pageSize){
		return boardMapper.getPagedList(start, pageSize);
	}
	
	public List<Board> getList()
	{
		return boardMapper.getList();
	}

	public List<Board> getBoardListDescViewCount()
	{
		return boardMapper.getBoardListDescViewCount();
	}

	public List<Board> getBoardListDescCommentCount()
	{
		return boardMapper.getBoardListDescCommentCount();
	}

	public List<Board> getBoardListIndate()
	{
		return boardMapper.getBoardListIndate();
	}
	
	public int boardUpdate(Board board)
	{
		return boardMapper.boardUpdate(board);
	}
	
	public int plusBoardViewCount(int idx)
	{
		return boardMapper.plusBoardViewCount(idx);
	}
	
	public int boardDelete(int idx)
	{
		return boardMapper.boardDelete(idx);
	}

	public Board findBoard(int idx)
	{
		return boardMapper.findBoard(idx);
	}

	public int plusBoardUpCount(int idx)
	{
		return boardMapper.plusBoardUpCount(idx);
	}

	public int plusBoardDownCount(int idx)
	{
		return boardMapper.plusBoardDownCount(idx);
	}

	public List<Board> searchBoardListPaged(@Param("searchType") String searchType,
    @Param("keyword") String keyword)
	{
		return boardMapper.searchBoardListPaged(searchType, keyword);
	}

	public List<Board> searchBoardListPagedDescViewCount(@Param("searchType") String searchType,
    @Param("keyword") String keyword)
	{
		return boardMapper.searchBoardListPagedDescViewCount(searchType, keyword);
	}

	public List<Board> searchBoardListPagedDescCommentCount(@Param("searchType") String searchType,
    @Param("keyword") String keyword)
	{
		return boardMapper.searchBoardListPagedDescCommentCount(searchType, keyword);
	}

	public List<Board> searchBoardListPagedDescIndate(@Param("searchType") String searchType,
    @Param("keyword") String keyword)
	{
		return boardMapper.searchBoardListPagedDescIndate(searchType, keyword);
	}

	public int countBoardListByType(String searchType, String keyword) {
    	return boardMapper.countBoardListByType(searchType, keyword);
	}
}
