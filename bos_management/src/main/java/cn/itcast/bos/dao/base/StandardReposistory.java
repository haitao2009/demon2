package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Standard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Administrator on 2017/12/23.
 */
public interface StandardReposistory extends JpaRepository<Standard,Integer>{

}
