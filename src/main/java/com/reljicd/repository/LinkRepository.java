package com.reljicd.repository;

import com.reljicd.model.Link;
import com.reljicd.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

/**
 * @author Dusan
 */
public interface LinkRepository extends JpaRepository<Link, Long> {
    Link findByUser(@Param("user") User user);

    Page<Link> findByUserOrderByCreateDateDesc(User user, Pageable pageable);

    Page<Link> findAllByOrderByCreateDateDesc(Pageable pageable);

    Collection<Link> findAllByUrl(String url);
}
