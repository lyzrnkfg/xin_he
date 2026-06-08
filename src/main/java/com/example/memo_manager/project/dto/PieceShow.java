package com.example.memo_manager.project.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class PieceShow implements Serializable {

    // 自定义serialVersionUID
    private static final long serialVersionUID = 987654752774867988L;

    private Integer pieceId;

    private Integer userId;

    private Integer processId;

    private Integer productId;

    private String useDate;

    private Integer useNum;

    private Integer depId;

    private String userName;

    private List<Department> depList;

    private List<Product> productList;

    private List<Process> processList;

}


