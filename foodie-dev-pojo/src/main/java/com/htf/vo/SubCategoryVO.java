package com.htf.vo;

import lombok.Data;

import java.util.List;

/**
 * 三级分类
 */
@Data
public class SubCategoryVO {

    /*id*/
    private Integer subId;

    /*名称*/
    private String subName;

    /*类型*/
    private String subType;

    /*对应二级分类的id*/
    private Integer subFatherId;

    private List<SubCategoryVO> subCatList;
}
