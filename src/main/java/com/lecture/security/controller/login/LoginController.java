package com.lecture.security.controller.login;

import com.lecture.security.domain.entitiy.Account;
import com.lecture.security.security.token.AjaxAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
public class LoginController {


    //exception메세지를 받아서 , 로그인 화면으로 모델에 담아서 전달할 수 있도록 처리한다.
    //@GetMapping("/login")
    @RequestMapping(value={"/login", "/api/login"})
    public String login(@RequestParam(value="error", required = false) String error,
                        @RequestParam(value="exception",required = false) String exception,
                        Model model){
            model.addAttribute("error:",error);
            model.addAttribute("exception:", exception);

        return "user/login/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }

        return "redirect:/user/login/login";
    }

    @GetMapping(value={"/denied","/api/denied"})
    public String accessDenied(@RequestParam(value = "exception", required = false) String exception,
                               Principal principal,
                               Model model){
       /* Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       //principle객체가 account로 CAST되지 않는 에러 발생. AccountContext가 account로 cast되지 않은 에러 .
        Account principle = (Account) authentication.getPrincipal();

        model.addAttribute("username",principle.getUsername());
        model.addAttribute("exception",exception);*/

        //AccountContext가 account로 cast되지 않은 에러 발생
        /*if (principal instanceof UsernamePasswordAuthenticationToken) {
            Account account = (Account) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

            model.addAttribute("username", account.getUsername());
            model.addAttribute("exception", exception);
        }*/


        Account account = null;

        if(principal instanceof UsernamePasswordAuthenticationToken){
            //account = (Account) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }else if(principal instanceof AjaxAuthenticationToken){
            //account = (Account) ((AjaxAuthenticationToken) principal).getPrincipal();
        }



        model.addAttribute("username",principal.getName());
        model.addAttribute("exception",exception);

        return "user/login/denied";
    }
}
;