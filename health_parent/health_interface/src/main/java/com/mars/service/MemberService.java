package com.mars.service;

import com.mars.pojo.Member;

import java.util.List;

/**
 * ClassName:MemberService
 * Package:com.mars.service
 * Description:
 *
 * @Date:2022/2/3 20:45
 * @Author:Mars
 */
public interface MemberService {

    Member findByTelephone(String telephone);

    void add(Member member);

    List<Integer> findMemberCountByMonths(List<String> months);
}
