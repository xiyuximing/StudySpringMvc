package com.cy.dao;

import com.cy.jpa.pojo.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ResumeDao extends JpaRepository<Resume,Long>, JpaSpecificationExecutor<Resume> {
}
