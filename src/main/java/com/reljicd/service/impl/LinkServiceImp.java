package com.reljicd.service.impl;

import com.reljicd.model.Link;
import com.reljicd.model.User;
import com.reljicd.repository.LinkRepository;
import com.reljicd.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LinkServiceImp implements LinkService {

    private final LinkRepository linkRepository;

    @Autowired
    public LinkServiceImp(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public Optional<Link> findLinkForId(Long id) {
        return linkRepository.findById(id);
    }

    @Override
    public Link saveLink(Link link) {
        return linkRepository.saveAndFlush(link);
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
