package com.example.memo_manager.project.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PieceRequest implements Serializable {

    // 自定义serialVersionUID
    private static final long serialVersionUID = 8768898752774867988L;

    private String userName;

    private Integer processId;

    private Integer productId;

    private String useDateFrom;

    private String useDateTo;

    private Integer depId;

    private List<Department> depList;

    private List<Product> productList;

}


