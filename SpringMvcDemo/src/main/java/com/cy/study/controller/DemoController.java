package com.cy.study.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/handle01")
    public ModelAndView handle01() {
        Date date = new Date();
        //封装了数据和页面信息
        ModelAndView modelAndView = new ModelAndView();
        //相当于像请求域中request.setAttribute()
        modelAndView.addObject("date", date);

        modelAndView.setViewName("success");
        return modelAndView;
    }
}
