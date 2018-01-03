package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.AreaRepository;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/12/27.
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    //注入Dao层
    @Autowired
    private AreaRepository areaRepository;

    @Override
    public void saveBatch(List<Area> areas) {
        areaRepository.save(areas);
    }


    @Override
    public Page<Area> findPageDate(Specification<Area> specification, Pageable pageable) {
        return areaRepository.findAll(specification,pageable);
    }

    @Override
    public void area_save(Area area) {
        areaRepository.save(area);
    }

    @Override
    public List<Area> findAll() {
        return areaRepository.findAll();
    }
}
