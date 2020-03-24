package com.htf.service;

import com.htf.pojo.Carousel;

import java.util.List;

public interface CarouseService {

    /**
     * 查询所有轮播图列表
     * @param isShow
     * @return
     */
    public List<Carousel> quertAll(Integer isShow);
}
