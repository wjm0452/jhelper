package com.jhelper.jserve.web.cache;

import java.util.List;

import com.jhelper.jserve.web.entity.CacheVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

    @Autowired
    CacheRepository cacheRepository;

    public List<CacheVO> findAll() {
        return cacheRepository.findAll();
    }

    public CacheVO findById(String id) {
        return cacheRepository.findById(id).orElse(null);
    }

    public CacheVO save(CacheVO cacheVO) {
        return cacheRepository.save(cacheVO);
    }

    public void delete(String id) {
        cacheRepository.deleteById(id);
    }
}
