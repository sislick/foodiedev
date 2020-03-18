package com.htf.controller;

import com.htf.pojo.Stu;
import com.htf.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StuController {

    @Autowired
    private StuService stuService;

    @GetMapping("/getStu")
    public Stu getStu(int id){
        return stuService.getStuInfo(id);
    }
}
