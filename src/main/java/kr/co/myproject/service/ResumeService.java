package kr.co.myproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.myproject.entity.Resume;
import kr.co.myproject.mapper.ResumeMapper;

@Service
public class ResumeService {
    @Autowired ResumeMapper resumeMapper;

    public int InsertResume(Resume resume)
    {
        return resumeMapper.InsertResume(resume);
    }

    public Resume SelectResume(int idx)
    {
        return resumeMapper.SelectResume(idx);
    }
}
