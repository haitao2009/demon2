package cn.itcast.bos.service.base;

import cn.itcast.bos.dao.base.StandardReposistory;
import cn.itcast.bos.domain.base.Standard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/12/23.
 */
@Service
@Transactional
public class StandarServiceImpl implements StandardService {
    //注入standarddao
    @Autowired
    private StandardReposistory standardReposistory;

    @Override
    public void sava(Standard standard) {
        standardReposistory.save(standard);
    }
}
