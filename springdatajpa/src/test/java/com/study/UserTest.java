package com.study;

import com.study.dao.UserDao;
import com.study.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserTest{
    @Autowired
    private UserDao userDao;

    /**
     * 增加新的数据
     */
    @Test
    public void testSave(){
        User user =new User();
        user.setUsername("马老师");
        userDao.save(user);
    }

    /**
     * 根据条件进行模糊查询
     */
    @Test
    public void testSpecifications() {
        Specification<User> spec = new Specification<User>() {
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(root.get("username").as(String.class),"%老师%");
            }
        };
        List<User> list = userDao.findAll(spec);
        System.out.println(list);
    }

    /**
     * 根据条件查询后分页
     */
    @Test
    public void testPage() {
        //构造查询条件
        Specification<User> spec = new Specification<User>() {
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.like(root.get("username").as(String.class), "%老师%");
            }
        };

        /**
         * 构造分页参数
         * 		Pageable : 接口
         * 			PageRequest实现了Pageable接口，调用构造方法的形式构造
         * 				第一个参数：页码（从0开始）
         * 				第二个参数：每页查询条数
         */
        Pageable pageable = new PageRequest(0, 2);

        /**
         * 分页查询，封装为Spring Data Jpa 内部的page bean
         * 		此重载的findAll方法为分页方法需要两个参数
         * 			第一个参数：查询条件Specification
         * 			第二个参数：分页参数
         */
        Page<User> page = userDao.findAll(spec,pageable);
        //打印出分页后的数据
        List<User> content = page.getContent();
        System.out.println(content);
    }
}
