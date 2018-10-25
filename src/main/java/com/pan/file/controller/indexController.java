package com.pan.file.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by FantasticPan on 2018/1/24.
 */
@Controller
public class indexController {

    @RequestMapping(value = {"", "/"})
    public String index() {
        return "form";
    }
}
