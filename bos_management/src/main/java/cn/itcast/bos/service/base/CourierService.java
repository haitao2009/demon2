package cn.itcast.bos.service.base;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Created by Administrator on 2017/12/26.
 */
public interface CourierService {

    public void save(Courier courier);

    public Page<Courier> findpageDate(Pageable pageable);

    public void delBatch(String[] idArray);

    Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable);
}

