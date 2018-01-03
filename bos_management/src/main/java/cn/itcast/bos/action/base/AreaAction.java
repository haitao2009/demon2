package cn.itcast.bos.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.base.PinYin4jUtils;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/27.
 */
@Actions
@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class AreaAction extends ActionSupport implements ModelDriven<Area> {
    //模型驱动
    private Area area = new Area();

    @Override
    public Area getModel() {
        return area;
    }

    //接受上传文件
    private File file;
    private String fileFileName;
    private String fileContentType;

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    //注入AreaService;
    @Autowired
    private AreaService areaService;

    //批量区域数据导入
    @Action(value = "area_batchImport", results = {@Result(name = "success", type = "json")})
    public String batchImport() {
        String msg = "";
        try {
            //判断传入的表格文件格式 然后解析
            Workbook workbook = null;
            if (fileFileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } else if (fileFileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }
            //1.创建对象
            List<Area> areas = new ArrayList<Area>();
            //2.获取sheet页
            Sheet sheet = workbook.getSheetAt(0);
            //3.获取row行
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                //获取每一行
                Row row = sheet.getRow(i);
                //获取每一行的单元格cell和其value值
                String id = row.getCell(0).getStringCellValue();
                String province = row.getCell(1).getStringCellValue();
                String city = row.getCell(2).getStringCellValue();
                String district = row.getCell(3).getStringCellValue();
                String postcode = row.getCell(4).getStringCellValue();

                // 基于pinyin4j生成城市编码和简码
                province = area.getProvince();
                city = area.getCity();
                district = area.getDistrict();

                province = province.substring(0, province.length() - 1);
                city = city.substring(0, city.length() - 1);
                district = district.substring(0, district.length() - 1);
                // 简码
                String[] headArray = PinYin4jUtils.getHeadByString(province + city
                        + district);
                StringBuffer buffer = new StringBuffer();
                for (String headStr : headArray) {
                    buffer.append(headStr);
                }
                String shortcode = buffer.toString();
                area.setShortcode(shortcode);
                // 城市编码
                String citycode = PinYin4jUtils.hanziToPinyin(city, "");
                area.setCitycode(citycode);

                //创建有参构造方法
                Area area = new Area(id, province, city, district, postcode);
                //将遍历得到的数据存入list集合
                areas.add(area);
            }
            //调用业务层保存数据
            areaService.saveBatch(areas);
            //上传成功
            msg = "上传成功";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "上传失败";
        }
        //将返回结果压入值栈
        ActionContext.getContext().getValueStack().push(msg);
        return SUCCESS;
    }

    //区域分页查询代码功能
    //属性注入
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    @Action(value = "area_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        //构造分页查询对象pageable
        Pageable pageable = new PageRequest(page - 1, rows);
        //构造条件查询对象
        Specification<Area> specification = new Specification<Area>() {
            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //创建保存条件集合对象
                List<Predicate> list = new ArrayList<>();
                //添加条件
                if (StringUtils.isNotBlank(area.getProvince())) {
                    //根据省份查询like%%
                    Predicate p = cb.like(root.get("province").as(String.class), "%" + area.getProvince() + "%");
                    list.add(p);
                }
                if (StringUtils.isNotBlank(area.getCity())) {
                    //根据城市模糊查询
                    Predicate p2 = cb.like(root.get("city").as(String.class), "%" + area.getCity() + "%");
                    list.add(p2);
                }
                if (StringUtils.isNotBlank(area.getDistrict())) {
                    Predicate p3 = cb.like(root.get("district").as(String.class), "%" + area.getDistrict() + "%");
                    list.add(p3);
                }
                //构造一个数组类型的泛型
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        //调用业务层完成条件分页查询功能
        Page<Area> pageDate = areaService.findPageDate(specification, pageable);
        //将查询到的结果集数据，封装车行datagrid需要的json格式
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageDate.getTotalElements());
        result.put("rows", pageDate.getContent());
        //压入值栈返回
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    //区域保存功能操作
    @Action(value = "area_save",results = {@Result(name = "success",location =  "./pages/base/area.html", type = "redirect")})
    public  String area_save(){
        //调用业务层
        areaService.area_save(area);
        return SUCCESS;
    }

    //查询所有区域方法
    @Action(value = "area_findAll",results = {@Result(name = "success",type = "json")})
    public String findAll(){
        List<Area> areas = areaService.findAll();
        ActionContext.getContext().getValueStack().push(areas);
        return SUCCESS;
    }
}
