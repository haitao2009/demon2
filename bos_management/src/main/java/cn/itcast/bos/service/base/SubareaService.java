package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.SubArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by Administrator on 2017/12/28.
 */
public interface SubareaService {
    void save(SubArea subArea);

    Page<SubArea> findPageData(Specification<SubArea> specification, Pageable pageable);


    void saveimportXls(List<SubArea> subAreas);
}
