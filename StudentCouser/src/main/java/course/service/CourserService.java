package course.service;

import course.enity.Course;
import course.enity.CourseStudent;
import course.util.CheckedException;

import java.util.List;

/**
 * Created by zpole on 2016/11/19.
 */
public interface CourserService {
    public void saveCourser(Course course) throws CheckedException;


    public void saveStudentCourser(int courseId, int studentId) throws CheckedException;

    public List<Course> selectAllCourse();


    public List<CourseStudent> selectGrade(int courserId, int startGrade, int endGrade);


    public int saveCourseStudent(CourseStudent courseStudent);

    public void deleteStudentCourser(int courserId, int studentId);

    public List<Course> selectByTeacherId(int teacherId);


    public List<CourseStudent> selectByCourseId(int courseId);

    public List<CourseStudent> selectByGrade(int courseId, int startGrade, int endGrade);


    public List<CourseStudent> selectByStudentId(int studentId);


    public Course selectById(int courserId);


    public int deleteCouser(int couserId);


    public Float avg(int courserId);


}
