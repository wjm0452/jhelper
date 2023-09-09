package com.jhelper.jserve.web.sql;

import java.util.List;

import com.jhelper.jserve.web.entity.ConnInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConnInfoService {

    @Autowired
    ConnInfoRepository connInfoRepository;

    public List<ConnInfo> findAll() {
        return connInfoRepository.findAll();
    }

    public ConnInfo findById(String id) {
        return connInfoRepository.findById(id).orElse(null);
    }

    public ConnInfo save(ConnInfo connInfo) {
        return connInfoRepository.save(connInfo);
    }

    public void delete(String id) {
        connInfoRepository.deleteById(id);
    }
}
