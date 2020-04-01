package com.htf.service;

import com.htf.pojo.UserAddress;
import com.htf.vo.AddressVO;

import java.util.List;

public interface AddressService {

    /**
     * 根据用户id查询用户的收货地址列表
     * @param userId
     * @return
     */
    List<UserAddress> queryAll(String userId);

    /**
     * 用户新增地址
     * @param addressVO
     */
    void addNewUserAddress(AddressVO addressVO);

    /**
     * 用户修改地址
     * @param addressVO
     */
    void updateUserAddress(AddressVO addressVO);
}
