package com.lecture.security.controller.user;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {
    @GetMapping("/messages")
    public String message() throws Exception{
        return "user/messages";
    }

    @PostMapping("/api/messages") //Ajax 에서 요청시에는 Post 방식으로 해야함.
    @ResponseBody //ajax로 요청했기 때문에 body에 넣어서 응답해야 한다.
    /*public String apiMessage() throws Exception{
        return "messages OK!";
    }*/
    public ResponseEntity apiMessage() throws Exception{
        return ResponseEntity.ok().body("ok");
    }
}
