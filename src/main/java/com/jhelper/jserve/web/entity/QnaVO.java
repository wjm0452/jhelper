package com.jhelper.jserve.web.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "qna")
public class QnaVO {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String registerId;

    private Date registerDate;

    private Integer parentId;

    private String title;

    private String content;
}
