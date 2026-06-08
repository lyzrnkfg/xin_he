package com.example.memo_manager.project.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Process implements Serializable {

    // 自定义serialVersionUID
    private static final long serialVersionUID = 8768869876774867988L;

    private Integer processKeyId;

    private Integer processId;

    private Integer productId;

    private Integer processOldId;

    private Integer productOldId;

    private String processName;

    private Double unitPrice;

    private Double costTime;

    private String productName;

    private String productStyleName;

    private List<Product> productList;

}


