package com.jhelper.jserve.web.cache;

import com.jhelper.jserve.web.entity.CacheVO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheRepository extends JpaRepository<CacheVO, String> {

}
