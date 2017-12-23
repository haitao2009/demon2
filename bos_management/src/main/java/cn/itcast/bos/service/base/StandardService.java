package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Standard;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2017/12/23.
 */
public interface StandardService {
    public void sava(Standard standard);
}
