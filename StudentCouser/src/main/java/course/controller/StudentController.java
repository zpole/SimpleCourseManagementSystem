package course.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import course.enity.Course;
import course.enity.CourseStudent;
import course.enity.Student;
import course.enity.Teacher;
import course.service.CourserService;
import course.service.StudentService;
import course.service.TeacherService;
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

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/student")
@Api(value = "学生接口")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourserService courserService;
    @Autowired
    private TeacherService teacherService;

    @Data
    public static class LoginRequest {
        int account;
        String pwd;
    }

    @RequestMapping("/login")
    @ResponseBody
    @ApiOperation(value = "学生登录接口", httpMethod = "POST")
    public ResultObject login(@RequestBody LoginRequest loginRequest, HttpSession httpSession) {
        Student student = studentService.findStudentByNum(loginRequest.getAccount());
        if (student == null||student.getState()==1) {
            return new ResultObject(ErrorCode.PARAM_ERROR);
        }
        if (student.getPwd().equals(MD5Util.generatePwd(loginRequest.getPwd()))) {
            httpSession.setAttribute("student", student);
            return new ResultObject(ErrorCode.ERROR_OK);
        }
        return new ResultObject(ErrorCode.PARAM_ERROR);
    }

    @RequestMapping("/course/list")
    @ResponseBody
    @ApiOperation(value = "课程列表", httpMethod = "POST")
    public ResultObject coursesList(HttpSession httpSession) {
        Student student = (Student) httpSession.getAttribute("student");
        if (student != null) {
            List<CourseStudent> mData = courserService.selectByStudentId(student.getId());
            List<Course> result = new ArrayList<>();
            if (mData.size() > 0) {
                for (CourseStudent courseStudent : mData) {
                    Course course = courserService.selectById(courseStudent.getCourserId());
                    if (course == null) continue;
                    Teacher teacher = teacherService.selectById(course.getTeacherId());
                    if (teacher == null) continue;
                    course.setTeacher(teacher);
                    course.setGrade(courseStudent.getGrade());
                    result.add(course);
                }
            }
            return new ResultObject(ErrorCode.ERROR_OK, result);
        }
        return new ResultObject(ErrorCode.NO_PERMISSION);
    }

    @Data
    static class PWDRequest {
        String pwd;
        String oldPwd;
    }

    @ApiOperation(value = "更改密码接口", httpMethod = "POST")
    @RequestMapping(value = "/changePwd")
    @ResponseBody
    public ResultObject changePwd(HttpSession httpSession, @RequestBody PWDRequest pwdRequest) {
        Student student = (Student) httpSession.getAttribute("student");
        if (StringUtils.isEmpty(pwdRequest.getOldPwd()) || StringUtils.isEmpty(pwdRequest.getPwd())) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "参数不正确");
        }
        if (student.getPwd().equals(MD5Util.generatePwd(pwdRequest.getOldPwd()))) {
            student.setPwd(MD5Util.generatePwd(pwdRequest.getPwd()));
            studentService.saveStudent(student);
            return new ResultObject(ErrorCode.ERROR_OK);
        } else {
            return new ResultObject(ErrorCode.PARAM_ERROR, "密码不正确");
        }
    }

}
