package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.FixedArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */
public interface Fixed_areaService {
    void save(FixedArea fixedArea);

    List<FixedArea> findAll();

    Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable);
}
