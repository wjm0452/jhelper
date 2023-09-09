package com.jhelper.jserve.web.loader;

import com.jhelper.jserve.web.sql.dataloader.DataLoaderService;
import com.jhelper.jserve.web.sql.dataloader.model.DataLoaderVO;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dataloader")
public class DataLoaderController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DataLoaderService dataLoaderService;

    @PostMapping
    public void dataloader(@RequestBody DataLoaderVO dataLoaderVO) throws IOException {
        dataLoaderService.load(dataLoaderVO);
    }
}
