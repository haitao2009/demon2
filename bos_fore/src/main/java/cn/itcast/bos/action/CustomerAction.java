package cn.itcast.bos.action;

import cn.itcast.bos.utils.BaseAction;
import cn.itcast.bos.utils.SmsUtils;
import cn.itcast.crm.domain.Customer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;


/**
 * Created by Administrator on 2018/1/3.
 */
public class CustomerAction extends BaseAction<Customer> {

    @Action(value = "customer_sendSms")
    public String sendSms() throws UnsupportedEncodingException {
        //手机号保存在Customer对象中;
        //生成短信验证码
        String randomCode = RandomStringUtils.randomNumeric(4);
        //将短信验证码保存到session中
        ServletActionContext.getRequest().getSession().setAttribute(model.getTelephone(), randomCode);
        System.out.println("手机验证码为：" + randomCode);
        // 编辑短信内容
        final String msg = "尊敬的用户您好，本次获取的验证码为：" + randomCode
                + ",服务电话：4006184000";

        //调用SMS服务发送短信
        //String result = SmsUtils.sendSmsByHTTP(model.getTelephone(),msg);
        String result = "0000123456789";
        if (result.startsWith("000")) {
            //发送成功
            return NONE;
        } else {
            //发送失败
            throw new RuntimeException("短信发送失败,信息码" + result);
        }
    }

    // 属性驱动
    private String checkcode;

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Action(value = "customer_regist", results = {
            @Result(name = "success", type = "redirect", location = "signup-success.html"),
            @Result(name = "input", type = "redirect", location = "signup.html")})
    public String regist() {
        // 先校验短信验证码，如果不通过，调回注册页面
        // 从session获取 之前生成验证码
        String checkcodeSession = (String) ServletActionContext.getRequest()
                .getSession().getAttribute(model.getTelephone());
        if (checkcodeSession == null || !checkcodeSession.equals(checkcode)) {
            System.out.println("短信验证码错误...");
            // 短信验证码错误
            return INPUT;
        }
        // 调用webService 连接CRM 保存客户信息
        WebClient
                .create("http://localhost:9002/crm_management/services"
                        + "/customerService/customer")
                .type(MediaType.APPLICATION_JSON).post(model);
        System.out.println("客户注册成功...");
        return SUCCESS;
    }
}
