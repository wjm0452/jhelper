package com.jhelper.jserve.web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Data;

@Data
@Entity(name = "connInfo")
public class ConnInfo {
    @Id
    private String name;
    private String vendor;
    private String jdbcUrl;
    private String driverClassName;
    private String username;
    private String password;

}
