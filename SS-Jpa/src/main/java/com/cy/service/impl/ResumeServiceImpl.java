package com.cy.service.impl;

import com.cy.dao.ResumeDao;
import com.cy.jpa.pojo.Resume;
import com.cy.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ResumeServiceImpl implements ResumeService {

    @Autowired
    private ResumeDao resumeDao;

    @Override
    public List<Resume> qryAll() {
        return resumeDao.findAll();
    }

    @Override
    public Resume insertOne(Resume resume) {
        return resumeDao.save(resume);
    }

    @Override
    public Resume updateOne(Resume resume) {
        return resumeDao.save(resume);
    }

    @Override
    public void deleteOne(Long id) {
        resumeDao.deleteById(id);
    }
}
