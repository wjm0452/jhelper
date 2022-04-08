package com.jhelper.jserve.web;

import java.util.List;

import com.jhelper.jserve.web.entity.QnaVO;
import com.jhelper.jserve.web.qna.QnaService;

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
@RequestMapping("/api/qna")
public class QnAController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    QnaService qnaService;

    @GetMapping
    public List<QnaVO> allQna() {
        return qnaService.findAll();
    }

    @GetMapping("/{id}")
    public QnaVO qna(@PathVariable Integer id) {
        return qnaService.findById(id);
    }

    @PostMapping
    public QnaVO createQna(@RequestBody QnaVO qnaVO) {
        return qnaService.create(qnaVO);
    }

    @PutMapping
    public QnaVO updateQna(@RequestBody QnaVO qnaVO) {
        return qnaService.update(qnaVO);
    }

    @DeleteMapping("/{id}")
    public void deleteQna(@PathVariable Integer id) {
        qnaService.delete(id);
    }
}
