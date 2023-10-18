package com.side.community.test;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/test")
    public String test(Model m){
        List<Test> tests = testService.allTest();
        m.addAttribute("test",tests);
        System.out.println(tests);
        return "test/tests";
    }

}
