package com.charityconnector.entity;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "article")
public class Article {
    private Long id;
    private String title;
    private String body;
    private Long charityId;
    private Date insertTime;
    private Date updateTime;


    /* Required by JPA specification */
    public Article() {
        super();
    }

    public Article(Long id, String title, String body, Long charityId, Date insertTime, Date updateTime) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.charityId = charityId;
        this.insertTime = insertTime;
        this.updateTime = updateTime;
    }

    public Article(long l) {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name="body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Column(name="charity_id")
    public Long getCharityId() {
        return charityId;
    }

    public void setCharityId(long charityId) {
        this.charityId = charityId;
    }



    @Column(name="insert_time")
    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }

    @Column(name="update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


}


