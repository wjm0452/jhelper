package com.jhelper.jserve.web.entity;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageDto<T> {
    private int totalPages;
    private long totalElements;
    private int size;
    private int number;
    private int numberOfElements;
    private List<T> items;
}
