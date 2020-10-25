package com.cy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
public class LoginResumeController {

    @RequestMapping("/login")
    @ResponseBody
    public Integer login(String username, String password, HttpServletRequest request) {
        if (Objects.equals(username, "admin") && Objects.equals(password, "admin")) {
            request.getSession().setAttribute("token", "token");
           return 200;
        } else {
            return 303;
        }
    }
}
