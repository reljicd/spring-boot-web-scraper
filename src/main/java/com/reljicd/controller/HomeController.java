package com.reljicd.controller;

import com.reljicd.model.Link;
import com.reljicd.service.LinkService;
import com.reljicd.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

/**
 * Controller for home page, including support for pagination
 *
 * @author Dusan
 */
@Controller
public class HomeController {

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = {5, 10, 20};

    @Autowired
    private LinkService linkService;

    /**
     * Return all links from all users and show them
     *
     * @param pageSize
     * @param page
     * @return
     */
    @GetMapping("/home")
    public ModelAndView home(@RequestParam("pageSize") Optional<Integer> pageSize,
                             @RequestParam("page") Optional<Integer> page) {

        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

//        Page<Link> links = linkService.findAllPageable(new PageRequest(evalPage, evalPageSize));
        Page<Link> links = linkService.findAllOrderedByDatePageable(new PageRequest(evalPage, evalPageSize));
        Pager pager = new Pager(links.getTotalPages(), links.getNumber(), BUTTONS_TO_SHOW);

        ModelAndView modelAndView = new ModelAndView();
//        Collection<Link> links = linkService.findNLatestLinks(5);
        modelAndView.addObject("links", links);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        modelAndView.setViewName("/home");
        return modelAndView;
    }

}
