package com.example.TMS.AForTest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StyleTest {
    @GetMapping("/testStyle")
    public String testStyle(Model model){
        return ("/testPages/StyleTest");
    }
}
