package com.jhelper.jserve.web.qna;

import com.jhelper.jserve.web.entity.QnaVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaRepository extends JpaRepository<QnaVO, Integer> {

}
