package com.jhelper.jserve.web.memo;

import com.jhelper.jserve.web.entity.MemoVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends JpaRepository<MemoVO, Integer> {

}
