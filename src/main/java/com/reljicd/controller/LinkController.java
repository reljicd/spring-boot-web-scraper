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
import java.util.Optional;

@Controller
public class LinkController {

    private final TagService tagService;
    private final LinkService linkService;
    private final UserService userService;

    @Autowired
    public LinkController(TagService tagService, LinkService linkService, UserService userService) {
        this.tagService = tagService;
        this.linkService = linkService;
        this.userService = userService;
    }

    /**
     * GET handler for new link form
     * returns LinkDTO in model as a backing bean for form
     */
    @RequestMapping(value = "/newLink", method = RequestMethod.GET)
    public ModelAndView newPost(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<User> user = userService.findByUsername(principal.getName());
        if (user.isPresent()) {
            LinkDTO linkDTO = new LinkDTO();
            modelAndView.addObject("linkDTO", linkDTO);
            modelAndView.setViewName("/linkForm");
        } else {
            modelAndView.setViewName("/error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/newLink", method = RequestMethod.POST)
    public ModelAndView createNewPost(@Valid LinkDTO linkDTO, BindingResult bindingResult, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<User> user = userService.findByUsername(principal.getName());
        if (user.isPresent()) {
            if (bindingResult.hasErrors()) {
                modelAndView.setViewName("/linkForm");
            } else {
                // Get collection of tags from string from text box in form
                Collection<Tag> tags = tagService.getTagsFromString(linkDTO.tags);

                Link link = new Link();
                link.setUrl(linkDTO.getUrl());
                link.setUser(user.get());
                link.setTags(tags);
                link = linkService.saveLink(link);
                modelAndView.setViewName("redirect:/link/" + link.getId() + "/recommendTags");
            }
        } else {
            modelAndView.setViewName("/error");
        }
        return modelAndView;
    }

    /**
     * Recommend Tags resource
     * Not possible to recommend tags if user is not logged in, or if he is now the owner of the link
     */
    @RequestMapping(value = "/link/{id}/recommendTags", method = RequestMethod.GET)
    public ModelAndView recommendTagsToLink(@PathVariable Long id, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Link> link = linkService.findLinkForId(id);
        if (link.isPresent()) {
            //  Not possible to recommend tags if user is not logged in, or if he is now the owner of the link
            if (principal == null || !principal.getName().equals(link.get().getUser().getUsername())) {
                modelAndView.setViewName("/403");
            } else {
                // Get tags from other users
                Collection<Tag> tagsFromOtherUsers = tagService.getTagsFromOtherUsersForLink(link.get());
                // Get tags from web analysis
                Collection<Tag> tagsFromWebPageAnalysis = tagService.getTagsFromWebPageAnalysis(link.get());
                modelAndView.addObject("tagsFromOtherUsers", tagsFromOtherUsers);
                modelAndView.addObject("tagsFromWebPageAnalysis", tagsFromWebPageAnalysis);
                modelAndView.addObject("link", link.get());
                modelAndView.setViewName("/recommendTags");
            }
        } else {
            modelAndView.setViewName("/error");
        }
        return modelAndView;
    }

    /**
     * Handler for adding tags to links
     * Not possible to add tag if user is not logged in, or if he is now the owner of the link
     */
    @RequestMapping(value = "/link/{linkId}/addTag/{tagString}", method = RequestMethod.GET)
    public ModelAndView addTagToLink(@PathVariable("linkId") Long linkId, @PathVariable("tagString") String tagString, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Link> link = linkService.findLinkForId(linkId);
        if (link.isPresent()) {
            // Not possible to add tag if user is not logged in, or if he is now the owner of the link
            if (principal == null || !principal.getName().equals(link.get().getUser().getUsername())) {
                modelAndView.setViewName("/403");
            } else {
                Tag tag = new Tag();
                tag.setTag(tagString);
                // If there is tag with that string already in data store, use it
                Optional<Tag> tagAlreadyExist = tagService.findByTag(tagString);
                if (tagAlreadyExist.isPresent()) tag = tagAlreadyExist.get();

                if (!link.get().getTags().contains(tag)) link.get().getTags().add(tag);
                linkService.saveLink(link.get());

                //redirect back to Recommend Tags page
                modelAndView.setViewName("redirect:/link/" + link.get().getId() + "/recommendTags");
            }
        } else {
            modelAndView.setViewName("/error");
        }
        return modelAndView;
    }

    /**
     * HTTP DELETE method
     * Deletes {@link Link} with provided id
     * Not possible to delete if user is not logged in, or if he is now the owner of the link
     */
    @RequestMapping(value = "/link/{id}", method = RequestMethod.DELETE)
    public ModelAndView deleteLinkWithId(@PathVariable Long id, Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        Optional<Link> link = linkService.findLinkForId(id);
        if (link.isPresent()) {
            // Not possible to delete if user is not logged in, or if he is now the owner of the link
            if (principal == null || !principal.getName().equals(link.get().getUser().getUsername())) {
                modelAndView.setViewName("/403");
            } else {
                linkService.delete(link.get());
                modelAndView.setViewName("redirect:/home");
            }
        } else {
            modelAndView.setViewName("/error");
        }
        return modelAndView;
    }

    /**
     * Link Data Transfer Object
     * For transferring string of tags from textbox
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
