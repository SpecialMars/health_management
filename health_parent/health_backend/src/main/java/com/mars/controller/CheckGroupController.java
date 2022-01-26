package com.mars.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.mars.constant.MessageConstant;
import com.mars.entity.Result;
import com.mars.pojo.CheckGroup;
import com.mars.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup,checkitemIds);
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
}
