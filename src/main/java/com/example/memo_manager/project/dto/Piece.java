package com.example.memo_manager.project.dto;

import java.math.BigDecimal;
import lombok.Data;

import java.io.Serializable;

@Data
public class Piece implements Serializable {

    // 自定义serialVersionUID
    private static final long serialVersionUID = 8761223752774867988L;

    private Integer pieceId;

    private Integer userId;

    private String userName;

    private Integer processId;

    private String processName;

    private Integer productId;

    private String productName;

    private String productStyleName;

    private String useDate;

    // 日期完成的件数
    private Integer useNum;

    // 部门
    private Integer depId;

    private String depName;

    // 单价
    private BigDecimal unitPrice;

    // 时间
    private BigDecimal costTime;

    // 工序工资
    private String priceProcess;

    // 工序时间
    private String timeProcess;

    // 总工资
    private String priceAllSum;

    // 总时间
    private String timeAllSum;

}


