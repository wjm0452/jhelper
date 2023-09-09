package com.jhelper.jserve.web.cache;

import com.jhelper.jserve.web.entity.Cache;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheRepository extends JpaRepository<Cache, String> {

}
