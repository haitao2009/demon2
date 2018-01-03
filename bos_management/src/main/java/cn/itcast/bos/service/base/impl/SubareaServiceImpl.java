package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.SubareaRepository;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubareaService;
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
public class SubareaServiceImpl implements SubareaService {
    //注入Dao
    @Autowired
    private SubareaRepository subareaRepository;

    @Override
    public void save(SubArea subArea) {
        subareaRepository.save(subArea);
    }

    @Override
    public Page<SubArea> findPageData(Specification<SubArea> specification, Pageable pageable) {
        return subareaRepository.findAll(specification, pageable);
    }

    @Override
    public void saveimportXls(List<SubArea> subAreas) {
        subareaRepository.save(subAreas);
    }
}
