package cn.itcast.crm.dao;


import cn.itcast.crm.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {


    List<Customer> findByFixedAreaIdIsNull();

    List<Customer> findByFixedAreaId(String fixedAdreaId);

    @Query("update Customer set fixedAreaId =? where id =?")
    @Modifying
    void updateFixedAreaId(String fixedAreaId, Integer id);


    @Query("update Customer set fixedAreaId = null where fixedAreaId = ?")
    @Modifying
    public void clearFixedAreaId(String fixedAreaId);
}
