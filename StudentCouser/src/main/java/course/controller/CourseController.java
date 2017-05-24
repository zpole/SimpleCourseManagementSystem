package course.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import course.dto.CourseDto;
import course.dto.StudentDto;
import course.enity.Course;
import course.enity.CourseStudent;
import course.enity.Student;
import course.enity.Teacher;
import course.service.CourserService;
import course.service.StudentService;
import course.service.TeacherService;
import course.util.ErrorCode;
import course.util.ResultObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zpole on 2016/11/26.
 */
@RequestMapping(value = "/course")
@Controller
@Api(value = "课程列表")
public class CourseController {
    @Autowired
    private CourserService courserService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;


    @Data
    static class StudentPost {
        Integer id;
        String sex;
        String icon;
        String pwd;
        String name;
        int number;

    }

    @RequestMapping(value = "/list")
    @ResponseBody
    @ApiOperation(value = "课程集合", httpMethod = "POST")
    public ResultObject listCourse() {

        List<Course> courses = courserService.selectAllCourse();
        List<CourseDto> result = new ArrayList<>();
        if (courses == null || courses.size() == 0) return new ResultObject(ErrorCode.ERROR_OK, courses);
        for (int i = 0; i < courses.size(); i++) {
            Teacher teacher = teacherService.selectById(courses.get(i).getTeacherId());
            if (teacher == null || teacher.getState() == 1) {
                continue;
            } else {
                teacher.setPwd(null);
                teacher.setAccount(null);
                courses.get(i).setTeacher(teacher);
            }
            CourseDto courseDto = new CourseDto();
            courseDto.setTeacher(teacher);
            courseDto.setId(courses.get(i).getId());
            courseDto.setName(courses.get(i).getName());
            result.add(courseDto);
            List<StudentDto> students = new ArrayList<>();
            List<CourseStudent> courseStudents = courserService.selectByCourseId(courses.get(i).getId());
            if (courseStudents == null || courseStudents.size() == 0) continue;
            if (courseStudents == null) continue;
            for (int j = 0; j < courseStudents.size(); j++) {
                Student student = studentService.selectById(courseStudents.get(j).getStudentId());
                if (student != null && student.getState() == 0) {
                    StudentDto studentDto = new StudentDto(student, courseStudents.get(j).getGrade());
                    students.add(studentDto);
                }
            }
            courseDto.setStudents(students);
        }
        return new ResultObject(ErrorCode.ERROR_OK, result);
    }

    @Data
    class GradeReponse {
        private int studentId;
        private int grade;
        private String courseName;
    }

    @RequestMapping(value = "/{id}/grades")
    @ResponseBody
    @ApiOperation(value = "课程分数统计", httpMethod = "GET")
    public ResultObject grades(@PathVariable int id) {
        Course course = courserService.selectById(id);
        if (course == null) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "课程不存在");
        }

        Map<String, Object> gradeMap = new HashMap<>();
        List<CourseStudent> failGrade = courserService.selectByGrade(id, 1, 59);
        gradeMap.put("0-59", failGrade.size());
        List<CourseStudent> pass = courserService.selectByGrade(id, 60, 69);
        gradeMap.put("60-69", pass.size());
        List<CourseStudent> normal = courserService.selectByGrade(id, 70, 79);
        gradeMap.put("70-79", normal.size());
        List<CourseStudent> excellent = courserService.selectByGrade(id, 80, 89);
        gradeMap.put("80-89", excellent.size());
        List<CourseStudent> good = courserService.selectByGrade(id, 90, 99);
        gradeMap.put("90-99", good.size());
        List<CourseStudent> prefect = courserService.selectByGrade(id, 100, 100);
        gradeMap.put("100", prefect.size());
        gradeMap.put("avg", courserService.avg(course.getId()) == null ? 0 : courserService.avg(course.getId()));
        return new ResultObject(ErrorCode.ERROR_OK, gradeMap);

    }


    @RequestMapping(value = "/grade/search")
    @ApiOperation(value = "根据课程搜索分数", httpMethod = "GET")
    @ResponseBody
    public ResultObject searchGrade(@RequestParam(value = "id") int id, @RequestParam(value = "start_Grade") int startGrade,
                                    @RequestParam(value = "end_grade") int endgrade) {
        List<CourseStudent> searchGrade = courserService.selectByGrade(id, startGrade, endgrade);
        Course course = courserService.selectById(id);
        if (course == null) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "参数不正确");
        }
        List<GradeReponse> gradeReponses = new ArrayList<>();
        for (int i = 0; i < searchGrade.size(); i++) {
            Student student = studentService.selectById(searchGrade.get(i).getStudentId());
            if (student == null || student.getState() == 1) {
                continue;
            }
            GradeReponse gradeReponse = new GradeReponse();
            gradeReponse.setCourseName(course.getName());
            gradeReponse.setGrade(searchGrade.get(i).getGrade());
            gradeReponse.setStudentId(student.getId());
            gradeReponses.add(gradeReponse);
        }
        return new ResultObject(ErrorCode.ERROR_OK, gradeReponses);
    }

}
