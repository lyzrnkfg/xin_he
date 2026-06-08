package com.example.memo_manager.project.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class Student implements Serializable {

    // 自定义serialVersionUID
    private static final long serialVersionUID = 5438861245774867988L;

    private Integer userId;

    private String userName;

    private List<Department> depList;

    private String depName;

    private Integer depId;

    private List<Student> stuList;

}


