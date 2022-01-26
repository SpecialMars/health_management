package com.mars.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mars.constant.MessageConstant;
import com.mars.entity.PageResult;
import com.mars.entity.QueryPageBean;
import com.mars.entity.Result;
import com.mars.pojo.CheckItem;
import com.mars.service.CheckItemService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName:CheckitemController
 * Package:com.mars.controller
 * Description:
 *
 * @Date:2022/1/23 22:17
 * @Author:Mars
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    // 查找服务
    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     *
     * @param checkItem
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {

        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            // 服务调用失败
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }

        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 检查项分页查询
     *
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        PageResult pageResult = checkItemService.pageQuery(queryPageBean);
        return pageResult;
    }

    /**
     * 删除检查项数据方法
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            checkItemService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 根据id获取表单数据，用于回显
     *
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {

        try {
            CheckItem checkItem = checkItemService.findById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkItem);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    /**
     * 根据id修改表单数据
     *
     * @param checkItem
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }
}
