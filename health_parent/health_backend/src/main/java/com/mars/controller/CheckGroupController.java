package com.mars.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mars.constant.MessageConstant;
import com.mars.entity.PageResult;
import com.mars.entity.QueryPageBean;
import com.mars.entity.Result;
import com.mars.pojo.CheckGroup;
import com.mars.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName:CheckGroupController
 * Package:com.mars.controller
 * Description:
 *
 * @Date:2022/1/26 19:55
 * @Author:Mars
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加方法
     *
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup, checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

        return checkGroupService.pageQuery(queryPageBean);
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id) {

        CheckGroup checkGroup = checkGroupService.findById(id);
        if (checkGroup != null) {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        }

        return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
    }

    @RequestMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(Integer id) {

        try {
            List<Integer> checkItemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemIds);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds){

        try {
            checkGroupService.edit(checkGroup,checkitemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }

        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/delete")
    public Result deleteById(Integer id){
        try {
            checkGroupService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }

        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
