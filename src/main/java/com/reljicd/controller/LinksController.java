package com.reljicd.controller;

import com.reljicd.model.Link;
import com.reljicd.model.User;
import com.reljicd.service.LinkService;
import com.reljicd.service.UserService;
import com.reljicd.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class LinksController {

    private static final int INITIAL_PAGE = 0;

    private final UserService userService;

    private final LinkService linkService;

    @Autowired
    public LinksController(UserService userService, LinkService linkService) {
        this.userService = userService;
        this.linkService = linkService;
    }

    @RequestMapping(value = "/links/{username}", method = RequestMethod.GET)
    public ModelAndView blogForUsername(@PathVariable String username,
                                        @RequestParam("page") Optional<Integer> page) {

        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        ModelAndView modelAndView = new ModelAndView();
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent()) {
            Page<Link> links = linkService.findByUserOrderedByDatePageable(user.get(), new PageRequest(evalPage, 5));
            Pager pager = new Pager(links);

            modelAndView.addObject("links", links);
            modelAndView.addObject("pager", pager);
            modelAndView.addObject("user", user.get());
            modelAndView.setViewName("/links");

        } else {
            modelAndView.setViewName("/error");
        }
        return modelAndView;
    }
}
