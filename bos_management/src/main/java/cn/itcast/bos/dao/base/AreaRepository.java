package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Administrator on 2017/12/27.
 */
public interface AreaRepository extends JpaRepository<Area,String>,JpaSpecificationExecutor<Area> {

}
