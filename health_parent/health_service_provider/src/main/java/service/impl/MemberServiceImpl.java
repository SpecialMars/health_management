package service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.mars.dao.MemberDao;
import com.mars.pojo.Member;
import com.mars.service.MemberService;
import com.mars.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * ClassName:MemberServiceImpl
 * Package:service.impl
 * Description:
 *
 * @Date:2022/2/3 20:54
 * @Author:Mars
 */
@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password != null){
            // 使用MD5将密码进行明文保密
            password = MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }
}
