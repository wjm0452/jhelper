package com.jhelper.jserve.web;

import java.util.List;

import com.jhelper.jserve.web.cache.CacheService;
import com.jhelper.jserve.web.entity.CacheVO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CacheService cacheService;

    @GetMapping
    public List<CacheVO> allCache() {
        return cacheService.findAll();
    }

    @GetMapping("/{id}")
    public CacheVO get(@PathVariable String id) {
        return cacheService.findById(id);
    }

    @PostMapping
    public CacheVO save(@RequestBody CacheVO cacheVO) {
        return cacheService.save(cacheVO);
    }

    @DeleteMapping("/{id}")
    public void deleteCache(@PathVariable String id) {
        cacheService.delete(id);
    }
}
