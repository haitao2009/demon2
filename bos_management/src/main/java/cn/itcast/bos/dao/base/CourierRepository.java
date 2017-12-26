package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Administrator on 2017/12/26.
 */
public interface CourierRepository extends JpaRepository<Courier,Integer> {
}
