package com.cy.service;

import com.cy.jpa.pojo.Resume;

import java.util.List;

public interface ResumeService {

    List<Resume> qryAll();

    Resume insertOne(Resume resume);

    Resume updateOne(Resume resume);

    void deleteOne(Long id);

    Resume qryById(Long id);
}
