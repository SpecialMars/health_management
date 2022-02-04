package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mars.constant.RedisConstant;
import com.mars.dao.SetmealDao;
import com.mars.entity.PageResult;
import com.mars.entity.QueryPageBean;
import com.mars.pojo.Setmeal;
import com.mars.service.SetmealService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:SetmealServiceImpl
 * Package:service.impl
 * Description:
 *
 * @Date:2022/1/27 14:01
 * @Author:Mars
 */
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    // 从属性文件中读取要生成的html对应的目录
    @Value("${out_put_path}")
    private String outPutPath;

    @Override
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 向t_setmeal表中添加套餐
        setmealDao.add(setmeal);

        if (checkgroupIds != null && checkgroupIds.length > 0) {
            // 绑定套餐和检查组的多对多关系
            setSetmealAndCheckGroup(setmeal.getId(), checkgroupIds);
        }
        if (setmeal.getImg() != null) {
            //将图片名称保存到Redis
            savePic2Redis(setmeal.getImg());
        }
        // 当添加套餐后需要重新生成动态页面（套餐列表）、（套餐详情）
        generateMobileStaticHtml();
    }

    // 生成当前方法所需的静态页面
    public void generateMobileStaticHtml() {
        // 在生成静态页面之前需要查询数据（准备数据）
        List<Setmeal> list = setmealDao.getSetmeal();
        // 需要生成套餐列表静态页面
        generateMobileSetmealListHtml(list);
        // 需要生成套餐详情静态页面(多个)
        generateMobileSetmealDetailHtml(list);
    }

    // 需要生成套餐列表静态页面
    private void generateMobileSetmealListHtml(List<Setmeal> list) {
        Map<String, List<Setmeal>> map = new HashMap<>();
        map.put("setmealList", list);
        generateHtml("mobile_setmeal.ftl","m_setmeal.html", map);
    }

    // 需要生成套餐详情静态页面(多个)
    private void generateMobileSetmealDetailHtml(List<Setmeal> list) {
        for (Setmeal setmeal : list) {
            Map map = new HashMap();
            // 使用联查才能获取到所有数据
            map.put("setmeal", this.findSetmealById(setmeal.getId()));
            generateHtml("mobile_setmeal_detail.ftl", "setmeal_detail_" + setmeal.getId() + ".html", map);
        }
    }

    // 用于生成静态页面
    public void generateHtml(String templateName, String htmlPageName, Map map) {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            out = new FileWriter(new File(outPutPath + "/" + htmlPageName));
            template.process(map, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null){
                try {
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //将图片名称保存到Redis
    private void savePic2Redis(String pic) {
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, pic);
    }

    //绑定套餐和检查组的多对多关系
    private void setSetmealAndCheckGroup(Integer setmealId, Integer[] checkgroupIds) {
        for (Integer checkgroupId : checkgroupIds) {
            Map<String, Integer> map = new HashMap<>();
            map.put("setmealId", setmealId);
            map.put("checkgroupId", checkgroupId);
            setmealDao.setSetmealAndCheckGroup(map);
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        String queryString = queryPageBean.getQueryString();
        Page<Setmeal> page = setmealDao.findPage(queryString);

        long total = page.getTotal();
        List<Setmeal> rows = page.getResult();

        return new PageResult(total, rows);
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Integer> findCheckGroupIdsBysetmealId(Integer id) {
        return setmealDao.findCheckGroupIdsBySetmealId(id);
    }

    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {

        // 根据套餐id删除检查组id，删除中间数据（清除原有关联）
        setmealDao.deleteAssociation(setmeal.getId());

        // 向中间表(t_setmeal_checkgroup)插入数据（建立套餐和检查组的关联关系）
        if (checkGroupIds != null && checkGroupIds.length > 0) {
            for (Integer checkGroupId : checkGroupIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("setmealId", setmeal.getId());
                map.put("checkgroupId", checkGroupId);
                setmealDao.setSetmealAndCheckGroups(map);
            }
        }
        //
        setmealDao.edit(setmeal);
        generateMobileStaticHtml();

    }

    @Override
    public void deleteById(Integer id) {
        setmealDao.deleteCheckGroupBySetmealId(id);
        setmealDao.deleteById(id);
        generateMobileStaticHtml();
    }

    @Override
    public List<Setmeal> getSetmeal() {
        return setmealDao.getSetmeal();
    }

    @Override
    public Setmeal findSetmealById(Integer id) {
        return setmealDao.findSetmealById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {

        return setmealDao.findSetmealCount();
    }
}
