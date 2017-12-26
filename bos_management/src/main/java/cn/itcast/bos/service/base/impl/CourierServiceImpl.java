package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.CourierRepository;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/12/26.
 */
@Service
public class CourierServiceImpl implements CourierService {
    //注入CourierRepository
    @Autowired
    private CourierRepository courierRepository;
    @Override
    public void save(Courier courier) {
    courierRepository.save(courier);
    }
}
