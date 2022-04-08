package com.jhelper.jserve.web;

import java.util.List;

import com.jhelper.jserve.web.entity.MemoVO;
import com.jhelper.jserve.web.memo.MemoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/memo")
public class MemoController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    MemoService memoService;

    @GetMapping
    public List<MemoVO> allQna() {
        return memoService.findAll();
    }

    @GetMapping("/{id}")
    public MemoVO qna(@PathVariable Integer id) {
        return memoService.findById(id);
    }

    @PostMapping
    public MemoVO createQna(@RequestBody MemoVO memoVO) {
        return memoService.create(memoVO);
    }

    @PutMapping
    public MemoVO updateQna(@RequestBody MemoVO memoVO) {
        return memoService.update(memoVO);
    }

    @DeleteMapping("/{id}")
    public void deleteQna(@PathVariable Integer id) {
        memoService.delete(id);
    }
}
