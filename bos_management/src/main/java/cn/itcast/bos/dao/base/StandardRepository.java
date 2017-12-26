package cn.itcast.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import cn.itcast.bos.domain.base.Standard;
import java.util.List;

/**
 * Created by Administrator on 2017/12/23.
 */
public interface StandardRepository extends JpaRepository<Standard,Integer>{




}
