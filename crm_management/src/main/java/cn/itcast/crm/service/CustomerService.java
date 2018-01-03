package cn.itcast.crm.service;

import cn.itcast.crm.domain.Customer;

import javax.ws.rs.*;
import java.util.List;

/**
 * Created by Administrator on 2017/12/30.
 */
public interface CustomerService {
    //查询所有未关联用户的接口
    @Path("/noassociationcustomers")
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Customer> findNoAssociationCustomers();

    //已经关联到某指定定区客户的列表
    @Path("/associationfixedareacustomers/{fixedareaid}")
    @GET
    @Produces({"application/xml", "application/json"})
    public List<Customer> findHasAssociationFixedAreaCustomers(
            @PathParam("fixedareaid") String fixedAreaId);

    //将客户关联到定区上，将所有用户id拼接成字符串1,2,3
    @Path("/associationcustomerstofixedarea")
    @PUT
    public void AssociationCustomersToFixedarea(
            @QueryParam("customerIdStr") String customerIdStr,
            @QueryParam("fixedAreaId") String fixedAreaId);




}
