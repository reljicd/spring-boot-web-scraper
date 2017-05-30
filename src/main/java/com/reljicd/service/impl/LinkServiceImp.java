package com.reljicd.service.impl;

import com.reljicd.model.Link;
import com.reljicd.model.User;
import com.reljicd.repository.LinkRepository;
import com.reljicd.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of {@link LinkService}
 *
 * @author Dusan
 */
@Service
public class LinkServiceImp implements LinkService {

    @Autowired
    private LinkRepository linkRepository;

    @Override
    public Collection<Link> findNLatestLinks(int n) {
        return nLatestLinks(n, linkRepository.findAll());
    }

    @Override
    public Collection<Link> findNLatestLinksForUser(int n, User user) {
//        return nLatestLinks(n, user.getLinks());
        return null;
    }

    private Collection<Link> nLatestLinks(int n, Collection<Link> links) {
        return links
                .stream()
                .sorted((a, b) -> b.getCreateDate().compareTo(a.getCreateDate()))
                .limit(n)
                .collect(Collectors.toList());
    }

    @Override
    public Link findLinkForId(Long id) {
        return linkRepository.findOne(id);
    }

    @Override
    public Link saveLink(Link link) {
        return linkRepository.saveAndFlush(link);
    }

    @Override
    public Page<Link> findAllPageable(Pageable pageable) {
        return linkRepository.findAll(pageable);
    }

    @Override
    public Page<Link> findByUserOrderedByDatePageable(User user, Pageable pageable) {
        return linkRepository.findByUserOrderByCreateDateDesc(user, pageable);
    }

    @Override
    public Page<Link> findAllOrderedByDatePageable(Pageable pageable) {
        return linkRepository.findAllByOrderByCreateDateDesc(pageable);
    }

    @Override
    public void delete(Link link) {
        linkRepository.delete(link);
    }
}
