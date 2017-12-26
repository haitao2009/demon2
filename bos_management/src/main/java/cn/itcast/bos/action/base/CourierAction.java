package cn.itcast.bos.action.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Created by Administrator on 2017/12/26.
 */
@Namespace("/")
@Actions
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class CourierAction extends ActionSupport implements ModelDriven<Courier>{
    //模型驱动
    private  Courier courier = new Courier();
    @Override
    public Courier getModel() {
        return courier;
    }
    //注入Courierservice
    @Autowired
    private CourierService courierService;

    //添加快递员方法
    @Action(value = "courier_save",results = {@Result(name="success",location = "./pages/base/courier.html",type = "redirect")})
    public String save(){
        courierService.save(courier);
        return SUCCESS;
    }
}
