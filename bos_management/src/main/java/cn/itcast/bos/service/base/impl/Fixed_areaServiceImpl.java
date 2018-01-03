package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.Fixed_areaRepository;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.service.base.Fixed_areaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */
@Service
@Transactional
public class Fixed_areaServiceImpl implements Fixed_areaService{
    //注入fixedareadao
    @Autowired
    private Fixed_areaRepository fixed_areaRepository;
    @Override
    public void save(FixedArea fixedArea) {
        fixed_areaRepository.save(fixedArea);
    }

    @Override
    public List<FixedArea> findAll() {
        return fixed_areaRepository.findAll();
    }

    @Override
    public Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable) {
        return fixed_areaRepository.findAll(specification, pageable);
    }
}
