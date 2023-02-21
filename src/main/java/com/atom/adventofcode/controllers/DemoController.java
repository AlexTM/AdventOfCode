package com.atom.adventofcode.controllers;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.atom.adventofcode.services.CodeService;
import com.atom.adventofcode.services.ProblemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class DemoController {
    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Autowired
    private ProblemService problemService;

    @Autowired
    private CodeService codeService;

    @GetMapping(value="/getCode", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String getCode(@RequestParam String problem) {
        return codeService.getCode(problem);
    }

    @GetMapping(value="/runCode", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String runCode(@RequestParam(required = false) String problem) {
        logger.info("!!!!!!!!!!!! {}", problem);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("!!!!!!!!!!!! done {}", problem);
        return "OK"+problem;
    }

    @GetMapping("/")
    public String show2(Model model) {
        model.addAttribute("problems", problemService.getProblems());
        model.addAttribute("code", "test");
        return "homepage";
    }


    @GetMapping("/cube")
    public String showCube() {
        return "cube";
    }

}
