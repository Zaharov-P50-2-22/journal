package com.mpt.journal.service;


import com.mpt.journal.entity.StudentEntity;
import com.mpt.journal.model.StudentModel;

import java.util.List;

public interface StudentService {
    public List<StudentModel> findAllStudent();
    public StudentModel addStudent(StudentModel student);
    public StudentModel updateStudent(StudentModel student);
    public List<StudentModel> searchStudents(String keyword);
    public List<StudentModel> filterStudents(String firstName, String lastName, String middleName);
    public void deleteStudent(int id);
    public void deleteStudents(List<Integer> ids);
    public void logicalDeleteStudent(int id);
    public void logicalDeleteStudents(List<Integer> ids);
}

