package course.service;


import course.enity.Student;
import course.util.CheckedException;

import java.util.List;

public interface StudentService {

    void saveStudent(Student student) throws CheckedException;


    void deleteStudent(int studetId) throws CheckedException;

    List<Student> findStudent();


    /**
     * 通过学号查找学生
     *
     * @param num
     * @return
     * @throws CheckedException
     */
    Student findStudentByNum(int num) throws CheckedException;

    Student selectById(int studentId);


}
