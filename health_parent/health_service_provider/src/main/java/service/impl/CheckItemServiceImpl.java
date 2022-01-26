package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mars.dao.CheckItemDao;
import com.mars.entity.PageResult;
import com.mars.entity.QueryPageBean;
import com.mars.pojo.CheckItem;
import com.mars.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName:CheckItemServiceImpl
 * Package:service.impl
 * Description:检查项管理
 * @Date:2022/1/24 10:27
 * @Author:Mars
 */
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService{

    // 注入Dao
    @Autowired
    private CheckItemDao checkItemDao;

    /**
     * 添加检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        // 获取当前页
        Integer currentPage = queryPageBean.getCurrentPage();
        // 获取当前页面的总记录数
        Integer pageSize = queryPageBean.getPageSize();
        // 获取查询条件
        String queryString = queryPageBean.getQueryString();

        // 完成分页查询，通过mybatis框架提供的分页助手插件完成
        PageHelper.startPage(currentPage,pageSize);

        // 调用dao中的selectByConfition方法获取到条件查询的值
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        // 通过page对象的得到当前页面总记录数和总行数
        long total = page.getTotal();
        List<CheckItem> rows = page.getResult();

        return new PageResult(total,rows);
    }


    @Override
    public void deleteById(Integer id) {
        // 检查当前检查项是否和检查组有关联
        long count = checkItemDao.findCountByCheckItemId(id);
        if(count > 0){
            // 说明有检查项被检查组关联，不能被删除
            new RuntimeException();
        }
        checkItemDao.deleteById(id);
    }
}
