package com.example.memo_manager.project.dto;

import java.math.BigDecimal;
import lombok.Data;

import java.io.Serializable;

@Data
public class Product implements Serializable {

    // 自定义serialVersionUID
    private static final long serialVersionUID = 1238869876774867988L;

    private Integer productId;

    private String productName;

    private String productStyleName;

    private BigDecimal totleNum;

    private BigDecimal totalPrice;

    private BigDecimal totalTime;
}


