package com.reljicd.controller;

import com.reljicd.model.Link;
import com.reljicd.model.Tag;
import com.reljicd.model.User;
import com.reljicd.service.LinkService;
import com.reljicd.service.TagService;
import com.reljicd.service.UserService;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.Collection;

/**
 * Controller for {@link Link}
 *
 * @author Dusan
 */
@Controller
public class LinkController {

    @Autowired
    private TagService tagService;
    @Autowired
    private LinkService linkService;
    @Autowired
    private UserService userService;

    /**
     * GET handler for new link form
     * returns LinkDTO in model as a backing bean for form
     *
     * @param principal
     * @return
     */
    @RequestMapping(value = "/newLink", method = RequestMethod.GET)
    public ModelAndView newPost(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findByUsername(principal.getName());
        LinkDTO linkDTO = new LinkDTO();
        modelAndView.addObject("linkDTO", linkDTO);
        modelAndView.setViewName("linkForm");
        return modelAndView;
    }

    /**
     * Handler of new Link form
     *
     * @param linkDTO       as a backing bean, should be validated
     * @param bindingResult
     * @param principal
     * @return
     */
    @RequestMapping(value = "/newLink", method = RequestMethod.POST)
    public ModelAndView createNewPost(@Valid LinkDTO linkDTO, BindingResult bindingResult, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.findByUsername(principal.getName());
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("linkForm");
        } else {

            /**
             * Get collection of tags from string from text box in form
             */
            Collection<Tag> tags = tagService.getTagsFromString(linkDTO.tags);

            Link link = new Link();
            link.setUrl(linkDTO.getUrl());
            link.setUser(user);
            link.setTags(tags);
            link = linkService.saveLink(link);
            modelAndView.setViewName("redirect:/link/" + link.getId() + "/recommendTags");
        }
        return modelAndView;
    }

    /**
     * Reccomend Tags resource
     * Not possible to recommend tags if user is not logged in, or if he is now the owner of the link
     *
     * @param id        of a {@link Link}
     * @param principal
     * @return
     */
    @RequestMapping(value = "/link/{id}/recommendTags", method = RequestMethod.GET)
    public ModelAndView reccomendTagsToLink(@PathVariable Long id, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        Link link = linkService.findLinkForId(id);
        if (link == null) {
            modelAndView.setViewName("error");
        }
        //  Not possible to recommend tags if user is not logged in, or if he is now the owner of the link
        else if (principal == null || !principal.getName().equals(link.getUser().getUsername())) {
            modelAndView.setViewName("403");
        } else {
            // Get tags from other users
            Collection<Tag> tagsFromOtherUsers = tagService.getTagsFromOtherUsersForLink(link);
            // Get tags from web analysis
            Collection<Tag> tagsFromWebPageAnalysis = tagService.getTagsFromWebPageAnalysis(link);
            modelAndView.addObject("tagsFromOtherUsers", tagsFromOtherUsers);
            modelAndView.addObject("tagsFromWebPageAnalysis", tagsFromWebPageAnalysis);
            modelAndView.addObject("link", link);
            modelAndView.setViewName("recommendTags");
        }
        return modelAndView;
    }

    /**
     * Handler for adding tags to links
     * Not possible to add tag if user is not logged in, or if he is now the owner of the link
     *
     * @param linkId    is ID of link
     * @param tagString string representation of tag
     * @param principal
     * @return
     */
    @RequestMapping(value = "/link/{linkId}/addTag/{tagString}", method = RequestMethod.GET)
    public ModelAndView addTagToLink(@PathVariable("linkId") Long linkId, @PathVariable("tagString") String tagString, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        Link link = linkService.findLinkForId(linkId);
        if (link == null) {
            modelAndView.setViewName("error");
        }
        // Not possible to add tag if user is not logged in, or if he is now the owner of the link
        else if (principal == null || !principal.getName().equals(link.getUser().getUsername())) {
            modelAndView.setViewName("403");
        } else {
            Tag tag = new Tag();
            tag.setTag(tagString);
            // If there is tag with that string already in data store, use it
            Tag tagAlreadyExist = tagService.findByTag(tagString);
            if (tagAlreadyExist != null) tag = tagAlreadyExist;

            if (!link.getTags().contains(tag)) link.getTags().add(tag);
            linkService.saveLink(link);

            //redirect back to Recommend Tags page
            modelAndView.setViewName("redirect:/link/" + link.getId() + "/recommendTags");
        }
        return modelAndView;
    }

    /**
     * HTTP DELETE method
     * Deletes {@link Link} with provided id
     * Not possible to delete if user is not logged in, or if he is now the owner of the link
     *
     * @param id
     * @param principal
     * @return
     */
    @RequestMapping(value = "/link/{id}", method = RequestMethod.DELETE)
    public ModelAndView deleteLinkWithId(@PathVariable Long id, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        Link link = linkService.findLinkForId(id);
        if (link == null) {
            modelAndView.setViewName("error");
        }
        // Not possible to delete if user is not logged in, or if he is now the owner of the link
        else if (principal == null || !principal.getName().equals(link.getUser().getUsername())) {
            modelAndView.setViewName("403");
        } else {
            linkService.delete(link);
            modelAndView.setViewName("redirect:/home");
        }
        return modelAndView;
    }

    /**
     * Link Data Transfer Object
     * For transfering string of tags from textbox
     *
     * @author Dusan
     */
    public static class LinkDTO {

        @URL(regexp = "(http(s)?:\\/\\/.)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)",
                message = "Please provide correct URL")
        private String url;
        @NotNull
        @Length(min = 1, message = "*Please put some tags")
        private String tags;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }
    }
}
