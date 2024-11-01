package web.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AjaxController {

    @GetMapping("/test")
    public String test() {
        log.info("왔니");
        return "Spring boot start 한글";
    }

}
