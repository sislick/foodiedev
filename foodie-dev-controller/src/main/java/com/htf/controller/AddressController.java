package com.htf.controller;

import com.htf.error.BusinessException;
import com.htf.error.EmBusinessError;
import com.htf.pojo.UserAddress;
import com.htf.response.ResponseJSONResult;
import com.htf.service.AddressService;
import com.htf.utils.MobileEmailUtils;
import com.htf.vo.AddressVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("address")
@Api(value = "地址相关API", tags = "地址相关API")
@CrossOrigin(origins = {"*"}, allowCredentials = "true")
public class AddressController {
    /**
     * 用户在确认订单页面，可以针对收货地址做如下操作：
     * 1.查询用户的所有收货地址列表
     * 2.新增收货地址
     * 3.删除收货地址
     * 4.修改收货地址
     * 5.设置默认地址
     */

    @Autowired
    private AddressService addressService;

    @PostMapping("/list")
    @ApiOperation(value = "根据用户id查询收货地址列表", notes = "根据用户id查询收货地址列表", httpMethod = "POST")
    public ResponseJSONResult list(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId){
        List<UserAddress> userAddressList = addressService.queryAll(userId);

        return ResponseJSONResult.create(userAddressList);
    }

    @PostMapping("/add")
    @ApiOperation(value = "用户新增地址", notes = "用户新增地址", httpMethod = "POST")
    public ResponseJSONResult add(
            @ApiParam(name = "", value = "", required = true)
            @RequestBody AddressVO addressVO) throws BusinessException{
        ResponseJSONResult result = this.checkAddress(addressVO);
        if(result.getStatus() == "success"){
            addressService.addNewUserAddress(addressVO);
        }

        return ResponseJSONResult.create(null);
    }

    @PostMapping("/update")
    @ApiOperation(value = "用户修改地址", notes = "用户修改地址", httpMethod = "POST")
    public ResponseJSONResult update(
            @ApiParam(name = "addressVO", value = "用户地址VO对象", required = true)
            @RequestBody AddressVO addressVO) throws BusinessException{
        if(StringUtils.isBlank(addressVO.getAddressId())){
            throw new BusinessException(EmBusinessError.ADDRESS_UPDATE_ERROR);
        }

        ResponseJSONResult result = this.checkAddress(addressVO);
        if(result.getStatus() == "success"){
            addressService.updateUserAddress(addressVO);
        }

        return ResponseJSONResult.create(null);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "用户删除地址", notes = "用户删除地址", httpMethod = "POST")
    public ResponseJSONResult delete(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址ID", required = true)
            @RequestParam String addressId) throws BusinessException{
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            throw new BusinessException(EmBusinessError.ADDRESS_DELETE_ERROR);
        }

        addressService.deleteUserAddress(userId, addressId);

        return ResponseJSONResult.create(null);
    }

    @PostMapping("/setDefalut")
    @ApiOperation(value = "用户设置默认地址", notes = "用户设置默认地址", httpMethod = "POST")
    public ResponseJSONResult setDefalut(
            @ApiParam(name = "userId", value = "用户ID", required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId", value = "地址ID", required = true)
            @RequestParam String addressId) throws BusinessException{
        if(StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            throw new BusinessException(EmBusinessError.ADDRESS_SETDEFAULT_ERROR);
        }

        addressService.updateUserAddressToBeDefault(userId, addressId);

        return ResponseJSONResult.create(null);
    }

    private ResponseJSONResult checkAddress(AddressVO addressVO) throws BusinessException{
        String receiver = addressVO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            throw new BusinessException(EmBusinessError.ADDRESS_USER_NOT_NULL);
        }
        if (receiver.length() > 12) {
            throw new BusinessException(EmBusinessError.ADDRESS_USER_NAME_TOO_LONG);
        }

        String mobile = addressVO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            throw new BusinessException(EmBusinessError.ADDRESS_USER_MOBILE_NOT_NULL);
        }
        if (mobile.length() != 11) {
            throw new BusinessException(EmBusinessError.ADDRESS_USER_MOBILE_LENGTH);
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            throw new BusinessException(EmBusinessError.ADDRESS_USER_MOBILE_FORMAT);
        }

        String province = addressVO.getProvince();
        String city = addressVO.getCity();
        String district = addressVO.getDistrict();
        String detail = addressVO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            throw new BusinessException(EmBusinessError.ADDRESS_INFO_NOT_NULL);
        }

        return ResponseJSONResult.create(null);
    }
}
