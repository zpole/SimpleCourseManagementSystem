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
import course.util.CheckedException;
import course.util.ErrorCode;
import course.util.MD5Util;
import course.util.ResultObject;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpole on 2016/11/20.
 */
@Controller
@RequestMapping(value = "/teacher")
@Api(value = "老师接口")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CourserService courserService;
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "/course/list")
    @ResponseBody
    @ApiOperation(value = "查找该老师的课程数据", httpMethod = "GET")
    public ResultObject getTeachers(HttpSession httpSession) {
        Teacher teacher = (Teacher) httpSession.getAttribute("teacher");
        List<Course> courses = courserService.selectByTeacherId(teacher.getId());
        List<CourseDto> result = new ArrayList<>();
        if (courses == null || courses.size() == 0) return new ResultObject(ErrorCode.ERROR_OK, courses);
        for (int i = 0; i < courses.size(); i++) {
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
                    StudentDto studentDto = new StudentDto(student, courseStudents.get(i).getGrade());
                    students.add(studentDto);
                }
            }
            courseDto.setStudents(students);
        }

        return new ResultObject(ErrorCode.ERROR_OK, result);
    }

    @Data
    public static class PWdRequest {
        String pwd;
        String oldPwd;
    }

    @RequestMapping(value = "/change/pwd")
    @ResponseBody
    @ApiOperation(value = "更改密码", httpMethod = "POST")
    public ResultObject changPwd(@RequestBody PWdRequest pwd, HttpSession httpSession) {
        Teacher teacher = (Teacher) httpSession.getAttribute("teacher");
        if (teacher == null) {
            return new ResultObject(ErrorCode.NO_PERMISSION);
        }
        teacher = teacherService.selectById(teacher.getId());
        if (StringUtils.isEmpty(pwd.getOldPwd())) {
            return new ResultObject(ErrorCode.NO_INPUT_OLDPWD);
        }
        if (teacher.getPwd().equals(MD5Util.generatePwd(pwd.getOldPwd()))) {
            teacher.setPwd(MD5Util.generatePwd(pwd.getPwd()));
            teacherService.saveTeacher(teacher);
            return new ResultObject(ErrorCode.ERROR_OK);
        } else {
            return new ResultObject(ErrorCode.PWD_NO_CORRECT);
        }
    }

    @Data
    public static class LoginRequest {
        int account;
        String pwd;
    }

    @RequestMapping(value = "/login")
    @ResponseBody
    @ApiOperation(value = "登录", httpMethod = "POST")
    public ResultObject login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        if (!StringUtils.isEmpty(loginRequest.getAccount()) && !StringUtils.isEmpty(loginRequest.getPwd())) {
            Teacher teacher = teacherService.selectByAccount(loginRequest.getAccount());
            if (teacher == null||teacher.getState()==1) {
                return new ResultObject(ErrorCode.PARAM_ERROR);
            }
            if (teacher.getPwd().equals(MD5Util.generatePwd(loginRequest.getPwd()))) {
                request.getSession().setAttribute("teacher", teacher);
                return new ResultObject(ErrorCode.ERROR_OK);
            } else {
                return new ResultObject(ErrorCode.PARAM_ERROR, "密码不正确");
            }
        }
        return new ResultObject(ErrorCode.PARAM_ERROR);
    }

    @RequestMapping(value = "/saveStudentGrade")
    @ResponseBody
    @ApiOperation(value = "修改学生分数", httpMethod = "POST")
    public ResultObject changePwd(@RequestBody CourseStudent courseStudent) {
        Course course = courserService.selectById(courseStudent.getCourserId());
        if (course == null) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "课程不存在");
        }
        Student student = studentService.selectById(courseStudent.getStudentId());
        if (student == null || student.getId() == 0) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "学生不存在");
        }
        if (courseStudent.getGrade() == null) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "请输入分数");
        }
        if (courseStudent.getGrade() != null && (courseStudent.getGrade() < 0 || courseStudent.getGrade() > 100)) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "分数不正确");
        }
        try {
            courserService.saveCourseStudent(courseStudent);
            return new ResultObject(ErrorCode.ERROR_OK);
        } catch (CheckedException exit) {
            return new ResultObject(exit.getErrorCode());
        }
    }


}
