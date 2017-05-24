package course.dao;

import course.enity.CourseStudent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseStudentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CourseStudent record);

    int insertSelective(CourseStudent record);

    CourseStudent selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CourseStudent record);

    int updateByPrimaryKey(CourseStudent record);

    List<CourseStudent> selectAll(@Param("courseId") Integer courserId,
                                  @Param("studentId") Integer studentId,
                                  @Param("startGrade") Integer startGrade,
                                  @Param("endGrade") Integer endGrade);

    int deleteByCourseAndStudentId(@Param("courseId") int courseId, @Param("studentId") int studentId);

    CourseStudent selectStudentCourse(CourseStudent courseStudent);


    Float average(@Param("id") int id);

    int updateGrade(CourseStudent courseStudent);


}