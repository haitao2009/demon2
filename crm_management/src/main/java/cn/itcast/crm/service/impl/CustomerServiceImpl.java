package cn.itcast.crm.service.impl;

import cn.itcast.crm.dao.CustomerRepository;
import cn.itcast.crm.domain.Customer;
import cn.itcast.crm.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.List;

/**
 * Created by Administrator on 2017/12/30.
 */
@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    //注入Dao
    @Autowired
    private CustomerRepository customerRepository;

    //查询所有未关联用户 客户没有关联定区
    @Override
    public List<Customer> findNoAssociationCustomers() {
        //fixedAreaId is null
        return customerRepository.findByFixedAreaIdIsNull();
    }

    //客户已经关联到某指定定区的列表
    @Override
    public List<Customer> findHasAssociationFixedAreaCustomers(String fixedAdreaId) {
        return customerRepository.findByFixedAreaId(fixedAdreaId);
    }

    //将客户关联到定区上，将所有用户id拼接成字符串1,2,3

    @Override
    public void AssociationCustomersToFixedarea(String customerIdStr, String fixedAreaId) {

        // 解除关联动作
        customerRepository.clearFixedAreaId(fixedAreaId);

        // 切割字符串 1,2,3
        if (StringUtils.isBlank(customerIdStr)) {
            return;
        }
        String[] customerIdArray = customerIdStr.split(",");
        for (String idStr : customerIdArray) {
            Integer id = Integer.parseInt(idStr);
            customerRepository.updateFixedAreaId(fixedAreaId, id);
        }
    }
}


