package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mars.dao.CheckGroupDao;
import com.mars.entity.PageResult;
import com.mars.entity.QueryPageBean;
import com.mars.pojo.CheckGroup;
import com.mars.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();// 得到当前页
        Integer pageSize = queryPageBean.getPageSize();// 得到页面大小
        String queryString = queryPageBean.getQueryString();// 得到查询条件

        // 使用分页助手
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.selectByCondition(queryString);

        long total = page.getTotal();
        List<CheckGroup> rows = page.getResult();

        return new PageResult(total, rows);
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {

        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {

        //根据检查组id删除中间表数据（清理原有关联关系）
        checkGroupDao.deleteAssociation(checkGroup.getId());
        //向中间表(t_checkgroup_checkitem)插入数据（建立检查组和检查项关联关系）
        setCheckGroupAndCheckItem(checkGroup.getId(), checkItemIds);
        //更新检查组基本信息
        checkGroupDao.edit(checkGroup);
    }

    private void setCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkItemIds) {
        if (checkItemIds != null && checkItemIds.length > 0) {
            for (Integer checkItemId : checkItemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", checkItemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public void deleteById(Integer id) {
        checkGroupDao.deleteCheckGroupAndCheckItemById(id);
        checkGroupDao.deleteById(id);
    }
}
