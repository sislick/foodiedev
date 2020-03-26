package com.htf.vo;

import lombok.Data;

/**
 * 用于展示商品评价数量的VO
 */
@Data
public class CommentLevelCountsVO {
    /*总评数量*/
    private Integer totalCounts;
    /*好评数量*/
    private Integer goodCounts;
    /*中评数量*/
    private Integer normalCounts;
    /*差评数量*/
    private Integer badCounts;
}
