package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */
public interface AreaService {
    public void saveBatch(List<Area> areas);

    Page<Area> findPageDate(Specification<Area> specification, Pageable pageable);

    public void area_save(Area area);

    List<Area> findAll();
}
