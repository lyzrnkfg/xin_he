package com.example.memo_manager.project.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ProductionStats implements Serializable {

    // 自定义serialVersionUID
    private static final long serialVersionUID = 8768863571594867988L;

    // 主键ID
    private Integer productionStatsId;

    // 人均产量
    private BigDecimal avgOutputPerPerson;

    // 计划日产量
    private BigDecimal plannedDailyOutput;

    // 订单交付天数
    private BigDecimal orderDeliveryDays;

    // 人均单价
    private BigDecimal pricePerUnit;

    // 人均时间
    private BigDecimal timePerPerson;

    private Integer depId;

    private String depName;

    private String productName;

    private String productStyleName;

    private Integer productId;

    private String useDate;

    private BigDecimal peopleNumber;

    private BigDecimal workTime;

    private List<Product> productList;

    private List<Department> depList;

}


