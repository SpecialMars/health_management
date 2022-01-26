package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mars.dao.CheckGroupDao;
import com.mars.pojo.CheckGroup;
import com.mars.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:CheckGroupServiceImpl
 * Package:service.impl
 * Description:
 *
 * @Date:2022/1/26 20:14
 * @Author:Mars
 */
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    /**
     * 新增检查组，同时关联检查项
     *
     * @param checkGroup:检查组
     * @param checkitemIds:检查组中各检查项的id，用来关联
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        // 新增检查组
        checkGroupDao.add(checkGroup);
        // 设置检查组和检查项的多对多的关联关系
        Integer checkGroupId = checkGroup.getId();
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkGroupId", checkGroupId);
                map.put("checkitemId", checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
