package kr.co.myproject.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.myproject.entity.Projects;
import kr.co.myproject.entity.Resume;
import kr.co.myproject.service.ProjectsService;
import kr.co.myproject.service.ResumeService;



@Controller
public class DummyController {
    @Autowired
    private ResumeService resumeService;

    @Autowired
    private ProjectsService projectsService;

    
	private final Logger logger = LoggerFactory.getLogger(PageController.class);

    @GetMapping("/dummy-board-add-page")
    public String DummyBoardAddPage() {
        return "dummyBoardAdd/resume";
    }

    @PostMapping("/dummy-board-add")
    public String DummyBoardAdd(@ModelAttribute Resume resume) {
        LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    	String formattedNow = now.format(formatter);
        resume.setIndate(formattedNow);
        resumeService.InsertResume(resume);
        return "redirect:/";
    }
    
    @GetMapping("/resume-board-check-page")
    public String ResumeBoardCheckPage(Model model) {
        Resume resume = resumeService.SelectResume(2);
        model.addAttribute("resume", resume);
        return "resumeBoardCheck/index";
    }

    @GetMapping("/projects-board-list-page")
    public String ProjectsBoardListPage(Model model) {
        List<Projects> projectsList = projectsService.SelectProjects();
        model.addAttribute("projectsList", projectsList);
        return "ProjectsBoardList/index";
    }

    @GetMapping("/projects-board-check-page")
    public String ProjectsBoardCheckPage(@RequestParam int idx,
                                          Model model) {
        Projects projects = projectsService.SelectProjectsByIdx(idx);
        model.addAttribute("projects", projects);
        return "ProjectsBoardCheck/index";
    }

     @GetMapping("/projects-board-add-page")
    public String ProjectsBoardAddPage() {
        return "dummyBoardAdd/projects";
    }

     @PostMapping("/projects-board-add")
    public String ProjectsBoardAdd(@ModelAttribute Projects projects) {
        projectsService.InsertProjects(projects);
        return "redirect:/";
    }
}
