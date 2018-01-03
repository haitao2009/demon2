package cn.itcast.bos.action.base;

import cn.itcast.bos.action.base.common.BaseAction;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.Fixed_areaService;
import cn.itcast.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.*;
import javax.ws.rs.core.MediaType;
import java.util.*;

/**
 * Created by Administrator on 2017/12/28.
 */
@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class fixed_areaAction extends ActionSupport implements ModelDriven<FixedArea> {
    //驱动模型实例化
    private FixedArea fixedArea = new FixedArea();

    @Override
    public FixedArea getModel() {
        return fixedArea;
    }

    //注入fixed_service
    @Autowired
    private Fixed_areaService fixed_areaService;

    //保存操作
    @Action(value = "fixed_save", results = {@Result(name = "success", location = "./pages/base/fixed_area.html", type = "redirect")})
    public String fixed_save() {
        //调用业务层
        fixed_areaService.save(fixedArea);
        return SUCCESS;
    }

    //查询所有的定区方法
    @Action(value = "fixarea_findAll", results = {@Result(name = "success", type = "json")})
    public String findAll() {
        List<FixedArea> fixedAreas = fixed_areaService.findAll();
        ActionContext.getContext().getValueStack().push(fixedAreas);
        return SUCCESS;
    }

    //定区管理分页条件查询
    //属性注入
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }


    @Action(value = "fixedarea_pageQuery", results = {@Result(name = "success", type = "json")})
    public String fixedarea_pageQuery() {
        //构造分页查询对象pageable
        Pageable pageable = new PageRequest(page - 1, rows);
        //构造条件查询对象
        Specification<FixedArea> specification = new Specification<FixedArea>() {
            @Override
            public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //创建保存条件集合对象
                List<Predicate> list = new ArrayList<>();
                //判断单表和多表查询
                //单表查询 所在单位模糊查询
                if (StringUtils.isNotBlank(fixedArea.getCompany())) {
                    Predicate p = cb.like(root.get("company").as(String.class), "%" + fixedArea.getCompany() + "%");
                    list.add(p);
                }
                //定区编码模糊查询
                if (StringUtils.isNotBlank(fixedArea.getId())) {
                    Predicate p2 = cb.like(root.get("id").as(String.class), "%" + fixedArea.getId() + "%");
                    list.add(p2);
                }
                //构造一个数组类型的泛型
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        // 调用业务层 ，返回 Page
        Page<FixedArea> pageData = fixed_areaService.findPageData(specification,
                pageable);
        // 将返回page对象 转换datagrid需要格式
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());
        // 将结果对象 压入值栈顶部
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    // 查询未关联定区列表
    @Action(value = "fixedArea_findNoAssociationCustomers", results = {@Result(name = "success", type = "json")})
    public String findNoAssociationCustomers() {
        // 使用webClient调用 webService接口
        Collection<? extends Customer> collection = WebClient
                .create("http://localhost:9090/crm_management/services/CustomerService/noassociationcustomers")
                .accept(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }

    // 查询关联当前定区的列表
    @Action(value = "fixedArea_findHasAssociationFixedAreaCustomers", results = {@Result(name = "success", type = "json")})
    public String findHasAssociationFixedAreaCustomers() {
        // 使用webClient调用 webService接口
        Collection<? extends Customer> collection = WebClient
                .create("http://localhost:9090/crm_management/services/CustomerService/associationfixedareacustomers/"
                        + fixedArea.getId()).accept(MediaType.APPLICATION_JSON)
                .type(MediaType.APPLICATION_JSON).getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }

    // 属性驱动
    private String[] customerIds;

    public void setCustomerIds(String[] customerIds) {
        this.customerIds = customerIds;
    }

    // 关联客户到定区
    @Action(value = "fixedArea_associationCustomersToFixedArea", results = {@Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html")})
    public String associationCustomersToFixedArea() {
        String customerIdStr = StringUtils.join(customerIds, ",");
        WebClient.create(
                "http://localhost:9090/crm_management/services/CustomerService"
                        + "/associationcustomerstofixedarea?customerIdStr="
                        + customerIdStr + "&fixedAreaId=" + fixedArea.getId()).put(
                null);
        return SUCCESS;
    }


}