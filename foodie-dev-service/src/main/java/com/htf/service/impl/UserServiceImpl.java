package com.htf.service.impl;

import com.htf.mapper.UsersMapper;
import com.htf.pojo.Users;
import com.htf.service.UserService;
import com.htf.utils.DateUtil;
import com.htf.utils.MD5Utils;
import com.htf.utils.SexUtil;
import com.htf.vo.UsersVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private Sid sid;

    private static final String USER_IMG_URL = "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_trade.jpg";

    /**
     * 判断用户名存不存在
     * @param username
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {

        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username",username);

        Users result = usersMapper.selectOneByExample(userExample);

        return result == null ? false : true;
    }

    /**
     * 创建users对象
     * @param usersVO
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public UsersVO createUsers(UsersVO usersVO) {
        String usersId = sid.nextShort();
        Users users = new Users();

        users.setId(usersId);
        users.setUsername(usersVO.getUsername());
        try {
            users.setPassword(MD5Utils.getMD5Str(usersVO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //默认用户昵称和用户名相同
        users.setNickname(usersVO.getUsername());
        //默认头像
        users.setFace(USER_IMG_URL);
        //默认生日
        users.setBirthday(DateUtil.stringToDate("1970-01-01"));
        //默认性别,为保密
        users.setSex(SexUtil.SECRECY.type);
        //创建时间
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());

        usersMapper.insert(users);
        return convertVOFromPojo(users);
    }

    /**
     * 检索用户名和密码是否匹配，用于登录
     * @param username
     * @param password
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUsersForLogin(String username, String password) {

        Example userExample = new Example(Users.class);
        Example.Criteria userCriteria = userExample.createCriteria();
        userCriteria.andEqualTo("username",username);
        try {
            userCriteria.andEqualTo("password",MD5Utils.getMD5Str(password));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Users users = usersMapper.selectOneByExample(userExample);
        return users;
    }

    /**
     * 将pojo对象转为VO对象
     * @param users
     * @return
     */
    private UsersVO convertVOFromPojo(Users users){
        UsersVO usersVO = new UsersVO();

        BeanUtils.copyProperties(users,usersVO);
        return usersVO;
    }
}
