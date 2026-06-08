package com.example.memo_manager.project.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class PieceInsert implements Serializable {

    // 自定义serialVersionUID
    private static final long serialVersionUID = 8761223752774867988L;

    private Integer userId;

    private Integer processId;

    private Integer productId;

    private String useDate;

    private Integer useNum;

}


