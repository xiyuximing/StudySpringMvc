package com.cy.demo.controller;

import com.cy.demo.service.IDemoService;
import com.cy.mvcframework.anno.MyAutowired;
import com.cy.mvcframework.anno.MyController;
import com.cy.mvcframework.anno.MyRequestMapping;
import com.cy.mvcframework.anno.Security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MyController
@MyRequestMapping("/demo")
@Security({"lisi","wangwu"})
public class DemoController {

    @MyAutowired
    private IDemoService demoService;

    @MyRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, String username) {
        try {

            request.setAttribute("name", username);
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
