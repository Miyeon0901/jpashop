package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model) { // Model에 데이터를 실어서 view에 넘김
        model.addAttribute("data", "미연!!");
        return "hello"; 
        // resources/templates/hello.html으로 렌더링. 
        // 스프링부트 thymeleaf viewName 매핑
        // prefix, suffix 변경 가능
    }
}
