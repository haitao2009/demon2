package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/12/23.
 */
public interface StandardService {
    public void save(Standard standard);

    // 分页查询
    public Page<Standard> findPageData(Pageable pageable);


}
