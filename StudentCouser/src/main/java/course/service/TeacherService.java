package course.service;

import course.enity.Teacher;
import course.util.CheckedException;

import java.util.List;

/**
 * Created by zpole on 2016/11/19.
 */
public interface TeacherService {
    void saveTeacher(Teacher teacher) throws CheckedException;

    List<Teacher> selectTeachers();

    Teacher selectByAccount(int account);

    List<Teacher> selectByName(String name);

    Teacher selectById(int teacherId);

    public void deleteTeacher(int id);
}
