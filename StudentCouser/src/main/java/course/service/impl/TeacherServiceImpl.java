package course.service.impl;

import course.dao.TeacherMapper;
import course.enity.Student;
import course.enity.Teacher;
import course.service.TeacherService;
import course.util.CheckedException;
import course.util.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zpole on 2016/11/19.
 */
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;


    @Override
    public void saveTeacher(Teacher teacher) throws CheckedException {
        try {
            if (teacher.getId() == null || teacher.getId() == 0) {
                teacherMapper.insertSelective(teacher);
            } else {
                teacherMapper.updateByPrimaryKeySelective(teacher);
            }
        } catch (DuplicateKeyException key) {
            throw new CheckedException(ErrorCode.EXIT_TEACHER);
        }
    }

    @Override
    public List<Teacher> selectTeachers() {

        return teacherMapper.selectAll();
    }

    @Override
    public Teacher selectByAccount(int account) {
        return teacherMapper.selectByAccount(account);

    }

    @Override
    public List<Teacher> selectByName(String name) {
        return teacherMapper.selectByName(name);
    }

    @Override
    public Teacher selectById(int teacherId) {
        return teacherMapper.selectByPrimaryKey(teacherId);
    }

    @Override
    public void deleteTeacher(int id) {
        Teacher teacher = teacherMapper.selectByPrimaryKey(id);
        if (teacher == null) return;
        if (teacher.getState() == Student.account_state_abnormal) return;
        teacher.setState((byte) Student.account_state_abnormal);
        teacherMapper.updateByPrimaryKey(teacher);
    }
}
