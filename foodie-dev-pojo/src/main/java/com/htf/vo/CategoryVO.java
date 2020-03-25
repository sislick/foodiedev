package com.htf.vo;

import lombok.Data;

import java.util.List;

/**
 * 二级分类
 */
@Data
public class CategoryVO {

    /*id*/
    private Integer id;

    /*名称*/
    private String name;

    /*类型*/
    private String type;

    /*对应一级分类的id*/
    private Integer fatherId;

    /*三级分类vo list*/
    private List<SubCategoryVO> subCatList;
}
