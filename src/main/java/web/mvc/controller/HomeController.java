package web.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@Slf4j
public class HomeController {
    @GetMapping("/")
    public ModelAndView home() {
        log.info("/요청됨");
        return new ModelAndView("index","message","boot ...");
    }
}
