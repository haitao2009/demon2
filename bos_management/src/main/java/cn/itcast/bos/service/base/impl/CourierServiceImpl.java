package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/12/26.
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {
    //注入CourierRepository
    @Autowired
    private CourierRepository courierRepository;

    @Override
    public Page<Courier> findpageDate(Pageable pageable) {
        return courierRepository.findAll(pageable);
    }

    @Override
    public void delBatch(String[] idArray) {
        // 调用DAO实现 update修改操作，将deltag 修改为1
        for (String idStr : idArray) {
            Integer id = Integer.parseInt(idStr);
            courierRepository.updateDelTag(id);
        }
    }
    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);
    }
    //按条件分页查询
    @Override
    public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable) {
        return courierRepository.findAll(specification, pageable);
    }
}
