package com.htf.service;

import com.htf.pojo.Users;
import com.htf.vo.UsersVO;

public interface UserService {

    /**
     * 判断用户名存不存在
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 创建用户
     * @param usersVO
     * @return
     */
    public Users createUsers(UsersVO usersVO);

    /**
     * 检索用户名和密码是否匹配，用于登录
     * @param username
     * @param password
     * @return
     */
    public Users queryUsersForLogin(String username, String password);
}
