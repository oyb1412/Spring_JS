package kr.co.myproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.myproject.entity.Projects;
import kr.co.myproject.mapper.ProjectsMapper;

@Service
public class ProjectsService {
    @Autowired ProjectsMapper projectsMapper;

    public int InsertProjects(Projects projects)
    {
        return projectsMapper.InsertProjects(projects);
    }

    public List<Projects> SelectProjects()
    {
        return projectsMapper.SelectProjects();
    }

    public Projects SelectProjectsByIdx(int idx)
    {
        return projectsMapper.SelectProjectsByIdx(idx);
    }
}
