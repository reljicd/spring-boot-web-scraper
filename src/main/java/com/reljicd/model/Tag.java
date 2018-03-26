package com.reljicd.model;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tag", unique = true)
    private String tag;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "tags")
    private Collection<Link> links;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Collection<Link> getLinks() {
        return links;
    }

    public void setLinks(Collection<Link> links) {
        this.links = links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag1 = (Tag) o;

        return tag.equals(tag1.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }
}
