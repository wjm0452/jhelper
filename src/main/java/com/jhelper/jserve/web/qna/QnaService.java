package com.jhelper.jserve.web.qna;

import java.util.Date;
import java.util.List;

import com.jhelper.jserve.web.entity.QnaVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QnaService {

    @Autowired
    QnaRepository qnaRepository;

    public List<QnaVO> findAll() {
        return qnaRepository.findAll();
    }

    public QnaVO findById(Integer id) {
        return qnaRepository.findById(id).orElse(null);
    }

    public QnaVO create(QnaVO qnaVO) {
        qnaVO.setRegisterId("wjm");
        qnaVO.setRegisterDate(new Date());
        return qnaRepository.save(qnaVO);
    }

    public QnaVO update(QnaVO qnaVO) {
        return qnaRepository.save(qnaVO);
    }

    public void delete(Integer id) {
        qnaRepository.deleteById(id);
    }

}
