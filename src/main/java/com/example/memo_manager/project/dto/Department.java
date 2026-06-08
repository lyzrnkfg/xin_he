package com.example.memo_manager.project.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class Department implements Serializable {

    // 自定义serialVersionUID
    private static final long serialVersionUID = 5438861245774867518L;

    private Integer depId;

    private String depName;
}


