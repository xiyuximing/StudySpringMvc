package com.cy.controller;

import com.cy.dao.ResumeDao;
import com.cy.jpa.pojo.Resume;
import com.cy.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/resume")
public class ResumeController {
    @Autowired
    private ResumeService resumeService;

    @RequestMapping("/qryAll")
    @ResponseBody
    public List<Resume> qryAll() {
        return resumeService.qryAll();
    }

    @RequestMapping("/insertOne")
    @ResponseBody
    public Resume insertOne(Resume resume) {
        return resumeService.insertOne(resume);
    }

    @RequestMapping("/updateOne")
    @ResponseBody
    public Resume updateOne(Resume resume) {
        return resumeService.updateOne(resume);
    }

    @RequestMapping("/deleteOne/{id}")
    @ResponseBody
    public void deleteOne(@PathVariable(value = "id") Long id) {
        resumeService.deleteOne(id);
    }

}
