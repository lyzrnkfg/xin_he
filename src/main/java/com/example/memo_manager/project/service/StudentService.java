package com.example.memo_manager.project.service;

import com.example.memo_manager.project.dto.Department;
import com.example.memo_manager.project.dto.Student;
import com.example.memo_manager.project.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 根据 id 查询 Student
     * @param id
     * @return
     */
    public Student findStudentById(Integer id) {
        return studentMapper.findStudentById(id);
    }

    public List<Student> findStudentList() {
        return studentMapper.findStudentList();
    }

    public List<Student> findSearch(Student studentForm) {
        return studentMapper.searchStudentList(studentForm);
    }

    public void updateStudents(Student studentForm) {
        List<Student> studentList = studentForm.getStuList();
        for (Student student : studentList){
            studentMapper.updateStudents(student);
        }
    }

    public List<Department> findDepList() {
        return studentMapper.findDepList();
    }

    public void save(Student student) {
        studentMapper.insert(student);
    }

    public void update(Student student) {
        studentMapper.update(student);
    }

    public void delete(Integer id) {
        studentMapper.delete(id);
    }

    public Integer findUserId(String userName, Integer userId) {
        return studentMapper.findUserId(userName, userId);
    }
}
