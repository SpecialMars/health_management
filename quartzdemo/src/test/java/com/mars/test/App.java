package com.mars.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ClassName:App
 * Package:com.mars.test
 * Description:
 *
 * @Date:2022/1/27 21:38
 * @Author:Mars
 */
public class App {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring-jobs.xml");
    }
}
