package com.jhelper.jserve.web.memo;

import java.util.Date;
import java.util.List;

import com.jhelper.jserve.web.entity.MemoVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemoService {

    @Autowired
    MemoRepository memoRepository;

    public List<MemoVO> findAll() {
        return memoRepository.findAll();
    }

    public MemoVO findById(Integer id) {
        return memoRepository.findById(id).orElse(null);
    }

    public MemoVO create(MemoVO memoVO) {
        memoVO.setRegisterDate(new Date());
        return memoRepository.save(memoVO);
    }

    public MemoVO update(MemoVO memoVO) {
        return memoRepository.save(memoVO);
    }

    public void delete(Integer id) {
        memoRepository.deleteById(id);
    }

}
