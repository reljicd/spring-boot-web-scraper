package com.reljicd.controller;

import com.reljicd.model.User;
import com.reljicd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Default controller
 *
 * @author Dusan
 */
@Controller
public class DefaultController {

    @Autowired
    private UserService userService;

    @GetMapping("/403")
    public String error403() {
        return "/403";
    }

}
