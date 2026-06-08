package com.example.memo_manager.project.mapper;

import com.example.memo_manager.project.dto.Department;
import com.example.memo_manager.project.dto.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StudentMapper {


    Student findStudentById(Integer id);

    List<Student> findStudentList();

    List<Department> findDepList();

    List<Student> searchStudentList(Student studentForm);

    void updateStudents(Student student);

    void insert(Student student);

    void update(Student student);

    void delete(Integer id);

    Integer findUserId(@Param("userName") String userName, @Param("userId") Integer userId);
}
