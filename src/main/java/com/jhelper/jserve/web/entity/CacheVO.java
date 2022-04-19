package com.jhelper.jserve.web.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "cache")
public class CacheVO {
    @Id
    private String key;
    private String value;
}
