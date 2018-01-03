package cn.itcast.bos.action.base;


import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubareaService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import javax.annotation.Resources;
import javax.persistence.criteria.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/28.
 */
@Namespace("/")
@ParentPackage("json-default")
@Controller
@Scope("prototype")
public class SubareaAction extends ActionSupport implements ModelDriven<SubArea> {
    private SubArea subArea = new SubArea();

    @Override
    public SubArea getModel() {
        return subArea;
    }

    //注入subareaService
    @Autowired
    private SubareaService subareaService;

    //管理分区保存功能
    @Action(value = "subarea_save", results = {@Result(name = "success", location = "./pages/base/sub_area.html", type = "redirect")})
    public String subarea_save() {
        //调用业务层
        subareaService.save(subArea);
        return SUCCESS;
    }

    //条件分页查询分区数据
    //属性注入
    private int page;
    private int rows;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }


    @Action(value = "subarea_pageQuery", results = {@Result(name = "success", type = "json")})
    public String subarea_pageQuery() {
        //构造分页查询对象pageable
        Pageable pageable = new PageRequest(page - 1, rows);
        //构造条件查询对象
        Specification<SubArea> specification = new Specification<SubArea>() {
            @Override
            public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //创建保存条件集合对象
                List<Predicate> list = new ArrayList<>();
                //多表查询
                Join<String, Object> area = root.join("area", JoinType.INNER);
                //判断单表和多表查询
                if (StringUtils.isNotBlank(subArea.getKeyWords())) {
                    Predicate p = cb.equal(root.get("keyWords").as(String.class), subArea.getKeyWords());
                    list.add(p);
                }
                //多表查询
                if (subArea.getArea() != null && StringUtils.isNotBlank(subArea.getArea().getProvince())) {
                    Predicate p1 = cb.like(
                            area.get("province").as(String.class), "%"
                                    + subArea.getArea().getProvince() + "%");
                    list.add(p1);
                }
                if (subArea.getArea() != null
                        && StringUtils.isNotBlank(subArea.getArea()
                        .getCity())) {
                    Predicate p2 = cb.like(
                            area.get("city").as(String.class), "%"
                                    + subArea.getArea().getCity() + "%");
                    list.add(p2);
                }
                if (subArea.getArea() != null
                        && StringUtils.isNotBlank(subArea.getArea()
                        .getDistrict())) {
                    Predicate p3 = cb.like(
                            area.get("district").as(String.class), "%"
                                    + subArea.getArea().getDistrict() + "%");
                    list.add(p3);
                }

                Join<String, Object> fixarea = root.join("fixedArea", JoinType.INNER);
                if (subArea.getFixedArea() != null
                        && StringUtils.isNotBlank(subArea.getFixedArea()
                        .getId())) {
                    Predicate p4 = cb.like(
                            fixarea.get("id").as(String.class), "%"
                                    + subArea.getFixedArea().getId() + "%");
                    list.add(p4);
                }

                //构造一个数组类型的泛型
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        // 调用业务层 ，返回 Page
        Page<SubArea> pageData = subareaService.findPageData(specification,
                pageable);
        // 将返回page对象 转换datagrid需要格式
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageData.getTotalElements());
        result.put("rows", pageData.getContent());
        // 将结果对象 压入值栈顶部
        ActionContext.getContext().getValueStack().push(result);
        return SUCCESS;
    }

    //分区上传表格
    private File file;
    private String fileFileName;

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setFile(File file) {
        this.file = file;
    }
    @Action(value = "subArea_batchImport",results =@Result(name = "success",type = "json"))
    public String importXls(){
        String msg="";
        try {
            Workbook workbook = null;
            // 0.判断后缀名
            if (fileFileName.endsWith(".xls")) {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } else if (fileFileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }
            List<SubArea> subAreas = new ArrayList<SubArea>();
            //创建workbook对象
            // 2.获取Sheet页
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                SubArea subArea = new SubArea();
                // 获取每一行
                Row row = sheet.getRow(i);
                // 获取每一个单元格 cell 并且获取值
                String id = row.getCell(0).getStringCellValue();
                subArea.setId(id);

                //定区fixed表获取
                String fixedId = row.getCell(1).getStringCellValue();
                //创建定区对象获取id
                FixedArea fixedArea = new FixedArea();
                fixedArea.setId(fixedId);
                subArea.setFixedArea(fixedArea);

                //区域id area获取
                String areaId = row.getCell(2).getStringCellValue();
                //创建区域对象获取区域编码
                Area area = new Area();
                area.setId(areaId);
                subArea.setArea(area);

                //获取表格中分区关键字
                String keyWords = row.getCell(3).getStringCellValue();
                subArea.setKeyWords(keyWords);
                //获取表格中的起始号和结束号码
                String startNum = row.getCell(4).getStringCellValue();
                subArea.setStartNum(startNum);
                String endNum = row.getCell(5).getStringCellValue();
                subArea.setEndNum(endNum);
                //获取表格单双号
                String single = row.getCell(6).getStringCellValue();

                subArea.setSingle(single.charAt(0));
                //获取位置信息
                String assistKeyWords = row.getCell(7).getStringCellValue();
                subArea.setAssistKeyWords(assistKeyWords);
                subAreas.add(subArea);
            }
            // 保存数据
           subareaService.saveimportXls(subAreas);
            //成功！！！
            msg = "上传成功！";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "上传失败！";
        }
        //将结果压入值栈
        ActionContext.getContext().getValueStack().push(msg);
        return SUCCESS;
    }
}
