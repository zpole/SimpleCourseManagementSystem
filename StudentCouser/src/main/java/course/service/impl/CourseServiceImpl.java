package course.service.impl;

import course.dao.CourseMapper;
import course.dao.CourseStudentMapper;
import course.enity.Course;
import course.enity.CourseStudent;
import course.service.CourserService;
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
public class CourseServiceImpl implements CourserService {
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CourseStudentMapper courseStudentMapper;

    @Override
    public void saveCourser(Course course) throws CheckedException {
        try {
            if (course.getId() != null && course.getId() > 0) {
                courseMapper.updateByPrimaryKeySelective(course);
            } else {
                courseMapper.insertSelective(course);
            }
        } catch (DuplicateKeyException dup) {
            throw new CheckedException(ErrorCode.EXIT_COURSE);

        }
    }

    @Override
    public void saveStudentCourser(int courseId, int studentId) throws CheckedException {
        try {
            CourseStudent courseStudent = new CourseStudent();
            courseStudent.setState(null);
            courseStudent.setCourserId(courseId);
            courseStudent.setStudentId(studentId);
            courseStudentMapper.insertSelective(courseStudent);
        } catch (DuplicateKeyException e) {
            throw new CheckedException(ErrorCode.EXIT_STUDENT_COURSE);
        }
    }

    @Override
    public List<Course> selectAllCourse() {
        return courseMapper.selectAll();
    }


    @Override
    public List<CourseStudent> selectGrade(int courseId, int startGrade, int endGrade) {
        return courseStudentMapper.selectAll(courseId, null, startGrade, endGrade);
    }

    @Override
    public int saveCourseStudent(CourseStudent courseStudent) {
        try {
            CourseStudent old = courseStudentMapper.selectStudentCourse(courseStudent);
            if (old != null) {
                return courseStudentMapper.updateGrade(courseStudent);
            } else {
                return courseStudentMapper.insertSelective(courseStudent);
            }
        } catch (DuplicateKeyException exit) {
            throw new CheckedException(ErrorCode.EXIT_STUDENT_COURSE);
        }
    }

    @Override
    public void deleteStudentCourser(int courserId, int studentId) {
        courseStudentMapper.deleteByCourseAndStudentId(courserId, studentId);
    }

    @Override
    public List<Course> selectByTeacherId(int teacherId) {
        return courseMapper.selectByTeacherId(teacherId);
    }

    @Override
    public List<CourseStudent> selectByCourseId(int courseId) {
        return courseStudentMapper.selectAll(courseId, null, null, null);
    }

    @Override
    public List<CourseStudent> selectByGrade(int courseId, int startGrade, int endGrade) {
        return courseStudentMapper.selectAll(courseId, null, startGrade, endGrade);
    }

    @Override
    public List<CourseStudent> selectByStudentId(int studentId) {
        return courseStudentMapper.selectAll(null, studentId, null, null);
    }

    @Override
    public Course selectById(int courserId) {
        return courseMapper.selectByPrimaryKey(courserId);
    }

    @Override
    public int deleteCouser(int couserId) {
        return courseMapper.deleteByPrimaryKey(couserId);
    }

    @Override
    public Float avg(int courserId) {
        return courseStudentMapper.average(courserId);
    }


}
