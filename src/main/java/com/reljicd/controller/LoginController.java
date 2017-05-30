package com.reljicd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

/**
 * Login Controller
 *
 * @author Dusan
 */
@Controller
public class LoginController {

    /**
     * If user is loged on, redirect to home
     *
     * @param principal
     * @return
     */
    @GetMapping("/login")
    public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/home";
        }
        return "/login";
    }

}
