package kr.co.myproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.myproject.entity.BoardType;
import kr.co.myproject.entity.Comment;
import kr.co.myproject.mapper.CommentMapper;


@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    public int commentInsert(Comment Comment)
    {
        return commentMapper.commentInsert(Comment);
    }

    public List<Comment> findComment(int parentIdx, BoardType boardType)
    {
        return commentMapper.findComment(parentIdx, boardType);
    }

    public int commentDelete(int idx)
    {
        return commentMapper.commentDelete(idx);
    }
}
