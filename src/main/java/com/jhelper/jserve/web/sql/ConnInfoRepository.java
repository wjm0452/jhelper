package com.jhelper.jserve.web.sql;

import com.jhelper.jserve.web.entity.ConnInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnInfoRepository extends JpaRepository<ConnInfo, String> {

}
