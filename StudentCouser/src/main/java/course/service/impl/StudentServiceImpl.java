package course.service.impl;

import course.dao.StudentMapper;
import course.enity.Student;
import course.service.StudentService;
import course.util.CheckedException;
import course.util.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpole on 2016/11/19.
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public void saveStudent(Student student) throws CheckedException {
        try {
            if (student.getId() != null&& student.getId()>0) {
                studentMapper.updateByPrimaryKeySelective(student);
            } else {
                studentMapper.insertSelective(student);
            }
        } catch (DuplicateKeyException e) {
            throw new CheckedException(ErrorCode.Exit_STUDENT);
        }

    }


    @Override
    public void deleteStudent(int studentId) throws CheckedException {
        Student student = studentMapper.selectByPrimaryKey(studentId);
        if (student == null) return;
        if (student.getState() == Student.account_state_abnormal) {
            return;
        }
        student.setState((byte) Student.account_state_abnormal);
        studentMapper.updateByPrimaryKey(student);
    }

    @Override
    public List<Student> findStudent() {
        List<Student> mData = studentMapper.selectAll();
        if (mData == null) return new ArrayList<>(0);
        return mData;
    }


    @Override
    public Student findStudentByNum(int num) {
        return studentMapper.selectByNum(num);
    }

    @Override
    public Student selectById(int studentId) {
        return studentMapper.selectByPrimaryKey(studentId);
    }
}
