package com.htf.service;

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
    public UsersVO createUsers(UsersVO usersVO);
}
