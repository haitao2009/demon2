package cn.itcast.bos.action.base;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Created by Administrator on 2017/12/23.
 */
@Namespace("/")
@Actions
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard>{
   //驱动模型实例化
    private Standard standard = new Standard();
    @Override
    public Standard getModel() {
        return standard;
    }
    //注入standardservice
    @Autowired
    private StandardService  standardService;



    //保存方法
    @Action(value = "standard_save",results = {@Result(name="success",type = "redirect",location = "./pages/base/standard.html")}
    )
    public String save(){
        System.out.println("添加派送页面提交信息");
        standardService.sava(standard);
        return SUCCESS;
    }
}
