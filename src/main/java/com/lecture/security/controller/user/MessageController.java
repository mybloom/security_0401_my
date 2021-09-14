package com.lecture.security.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {
    @GetMapping("/messages")
    public String message() throws Exception{
        return "user/messages";
    }

    @GetMapping("/api/messages")
    @ResponseBody //ajax로 요청했기 때문에 body에 넣어서 응답해야 한다.
    public String apiMessage() throws Exception{
        return "messages OK!";
    }
}
