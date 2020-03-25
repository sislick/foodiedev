package com.htf.vo;

import com.htf.pojo.Items;
import com.htf.pojo.ItemsImg;
import com.htf.pojo.ItemsParam;
import com.htf.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

@Data
public class ItemInfoVO {

    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;

}
