package cn.itcast.bos.utils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.springframework.data.domain.Page;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

    protected T model;

    protected int page ;
    protected int rows ;


    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Override
    public T getModel() {
        return model;
    }

    public BaseAction() {
        try {
            // 1.获取BaseAction带有泛型的类型
            ParameterizedType type = (ParameterizedType) this.getClass()
                    .getGenericSuperclass();
            // System.out.println(type);
            // 2.获取其泛型
            Class modelClass = (Class) type.getActualTypeArguments()[0];
            // 3.创建对象
            model = (T) modelClass.newInstance();
            System.out.println(model);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void pushPageDataToValueStack(Page pageData) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", pageData.getTotalElements());
        map.put("rows", pageData.getContent());
        ActionContext.getContext().getValueStack().push(map);
    }
}
