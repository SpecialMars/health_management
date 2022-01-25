package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mars.dao.CheckItemDao;
import com.mars.pojo.CheckItem;
import com.mars.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
}
