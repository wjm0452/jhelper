package com.jhelper.jserve.web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "cache")
public class Cache {
    @Id
    private String key;
    private String value;
}
