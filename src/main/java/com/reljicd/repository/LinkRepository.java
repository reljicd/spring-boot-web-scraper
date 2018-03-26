package com.reljicd.repository;

import com.reljicd.model.Link;
import com.reljicd.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface LinkRepository extends JpaRepository<Link, Long> {
    Page<Link> findByUserOrderByCreateDateDesc(User user, Pageable pageable);

    Page<Link> findAllByOrderByCreateDateDesc(Pageable pageable);

    Optional<Link> findById(Long id);

    Collection<Link> findAllByUrl(String url);
}
