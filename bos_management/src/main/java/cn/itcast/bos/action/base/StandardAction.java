package cn.itcast.bos.action.base;

import cn.itcast.bos.domain.base.Standard;
import cn.itcast.bos.service.base.StandardService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/23.
 */
@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {
    //驱动模型实例化
    private Standard standard = new Standard();

    @Override
    public Standard getModel() {
        return standard;
    }

    //注入standardservice
    @Autowired
    private StandardService standardService;

    //保存方法
    @Action(value = "standard_save", results = {@Result(name = "success", type = "redirect", location = "./pages/base/standard.html")}
    )
    public String save() {
        standardService.save(standard);
        return SUCCESS;
    }

    //属性驱动
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    //分页列表查询
    @Action(value = "standard_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        //创建pageable对象调用业务层，查询数据结果
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Standard> pageDate = standardService.findPageData(pageable);

        // /返回客户端数据 需要total和rows
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageDate.getNumberOfElements());
        result.put("rows", pageDate.getContent());

        //将map转换成json数据返回，使用sturts2-json-plug插件
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    //查询所有收派标准方法
    @Action(value = "standard_findAll", results = {@Result(name = "success", type = "json")})
    public String findAll() {
        List<Standard> standards = standardService.findAll();
        ActionContext.getContext().getValueStack().push(standards);
        return SUCCESS;
    }
}
