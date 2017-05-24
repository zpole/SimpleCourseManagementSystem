package course.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import course.enity.*;
import course.service.AdminService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpole on 2016/11/22.
 */
@RequestMapping("/admin")
@Controller
@Api(value = "admin-api", description = "有关admin操作的接口")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CourserService courserService;

    @Data
    public static class LoginRequest {
        private int account;
        private String pwd;
    }

    @RequestMapping("/login")
    @ResponseBody
    @ApiOperation(value = "管理员登录接口", httpMethod = "POST")
    public ResultObject login(@RequestBody LoginRequest loginRequest, HttpSession httpSession) {
        Admin admin = adminService.selectByAccount(loginRequest.getAccount());
        if (admin == null) {
            return new ResultObject(ErrorCode.PARAM_ERROR);
        }
        if (admin.getPwd().equals(MD5Util.generatePwd(loginRequest.getPwd()))) {
            httpSession.setAttribute("admin", admin);
            return new ResultObject(ErrorCode.ERROR_OK);
        }
        return new ResultObject(ErrorCode.PARAM_ERROR);
    }


    @RequestMapping("/changePwd")
    @ApiOperation(value = "更改密码", httpMethod = "POST")
    @ResponseBody
    public ResultObject changePwd(HttpSession httpSession, @RequestBody TeacherController.PWdRequest request) {
        Admin admin = (Admin) httpSession.getAttribute("admin");
        if (StringUtils.isEmpty(request.getPwd()) || StringUtils.isEmpty(request.getOldPwd())) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "输入密码");
        }
        if (admin.getPwd().equals(MD5Util.generatePwd(request.getOldPwd()))) {
            admin.setPwd(MD5Util.generatePwd(request.getPwd()));
        } else {
            return new ResultObject(ErrorCode.PARAM_ERROR, "密码不正确");
        }
        try {
            adminService.saveAdmin(admin);
            return new ResultObject(ErrorCode.ERROR_OK);
        } catch (CheckedException ex) {
            return new ResultObject(ex.getErrorCode());
        }
    }

    @RequestMapping("/saveTeacher")
    @ResponseBody
    @ApiOperation(value = "新建老师", httpMethod = "POST")
    public ResultObject saveTeacher(@RequestBody Teacher teacher) {
        if (teacher == null) {
            return new ResultObject(ErrorCode.PARAM_ERROR);
        }
        if (teacher.getId() == null || teacher.getId() == 0) {
            if (StringUtils.isEmpty(teacher.getAccount())) {
                return new ResultObject(ErrorCode.PARAM_ERROR, "请输入账号");
            }
            if (StringUtils.isEmpty(teacher.getPwd())) {
                return new ResultObject(ErrorCode.PARAM_ERROR, "请输入密码");
            }
            if (StringUtils.isEmpty(teacher.getName())) {
                return new ResultObject(ErrorCode.PARAM_ERROR, "请老师密码");
            }
        }
        if (!StringUtils.isEmpty(teacher.getPwd())) {
            teacher.setPwd(MD5Util.generatePwd(teacher.getPwd()));
        }
        try {
            teacherService.saveTeacher(teacher);
            return new ResultObject(ErrorCode.ERROR_OK);
        } catch (CheckedException ex) {
            return new ResultObject(ex.getErrorCode());
        }
    }

    @Data
    static class AddStudentPost {
        Integer id;
        String sex;
        String icon;
        String pwd;
        String name;
        int number;

    }

    @RequestMapping("/saveStudent")
    @ResponseBody
    @ApiOperation(value = "新建学生", httpMethod = "POST")
    public ResultObject saveStudent(@RequestBody AddStudentPost addStudentPost) {
        if (addStudentPost == null) {
            return new ResultObject(ErrorCode.ERROR_OK);
        }
        Student student = new Student();
        if (addStudentPost.getId() == null || addStudentPost.getId() == 0) {
            if (StringUtils.isEmpty(addStudentPost.getNumber())) {
                return new ResultObject(ErrorCode.PARAM_ERROR, "请输入账号");
            }
            if (StringUtils.isEmpty(addStudentPost.getPwd())) {
                return new ResultObject(ErrorCode.PARAM_ERROR, "请输入密码");
            }
            if (StringUtils.isEmpty(addStudentPost.getName())) {
                return new ResultObject(ErrorCode.PARAM_ERROR, "请输入学生姓名");
            }

            if (StringUtils.isEmpty(addStudentPost.getSex())) {
                return new ResultObject(ErrorCode.PARAM_ERROR, "请输入性别");
            }
        }
        if(!StringUtils.isEmpty(addStudentPost.getSex())){
            if (addStudentPost.getSex().equals("男")) {
                student.setSex((byte) 1);
            } else if (addStudentPost.getSex().equals("女")) {
                student.setSex((byte) 2);
            } else {
                return new ResultObject(ErrorCode.PARAM_ERROR, "性别只有男和女");

            }
        }
        if (!StringUtils.isEmpty(addStudentPost.getPwd())) {
            addStudentPost.setPwd(MD5Util.generatePwd(addStudentPost.getPwd()));
        }
        addStudentPost.setIcon("");
        student.setPwd(addStudentPost.getPwd());
        student.setName(addStudentPost.getName());
        student.setIcon("");
        student.setNumber(addStudentPost.getNumber());
        student.setId(addStudentPost.getId());
        try {
            studentService.saveStudent(student);
            return new ResultObject(ErrorCode.ERROR_OK);
        } catch (CheckedException ex) {
            return new ResultObject(ex.getErrorCode());
        }
    }

    @Data
    static class CouserRequest {
        int id;
        String name;
        int teacherId;
    }

    @RequestMapping(value = "/saveCourse")
    @ResponseBody
    @ApiOperation(value = "新建课程", httpMethod = "POST")
    public ResultObject saveCourse(@RequestBody CouserRequest couserRequest) {
        if (couserRequest == null) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "参数不正确");
        }
        if (StringUtils.isEmpty(couserRequest.getName()) || couserRequest.getTeacherId() < 0) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "参数不正确");
        }
        Teacher teacher = teacherService.selectById(couserRequest.getTeacherId());
        if (teacher == null || teacher.getState() == 1) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "老师不存在");
        }
        try {
            Course course = new Course();
            course.setId(couserRequest.getId());
            course.setTeacherId(couserRequest.getTeacherId());
            course.setName(couserRequest.getName());
            courserService.saveCourser(course);
            return new ResultObject(ErrorCode.ERROR_OK);
        } catch (CheckedException checkedEx) {
            return new ResultObject(checkedEx.getErrorCode());
        }
    }

    @RequestMapping(value = "/deleteCourse/{id}")
    @ResponseBody
    @ApiOperation(value = "删除课程", httpMethod = "POST")
    public ResultObject saveCourse(@PathVariable int id) {
        Course course = courserService.selectById(id);
        if (course == null) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "不存在该课程或者已经被删除");
        }
        courserService.deleteCouser(id);
        return new ResultObject(ErrorCode.ERROR_OK);
    }

    @RequestMapping(value = "/deleteTeacher/{id}")
    @ResponseBody
    @ApiOperation(value = "删除老师", httpMethod = "POST")
    public ResultObject deleteTeacher(@PathVariable int id) {
        Teacher teacher = teacherService.selectById(id);
        if (teacher == null || teacher.getState() == 1) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "老师已经被删除");
        }
        teacher.setState((byte) 1);
        teacherService.saveTeacher(teacher);
        return new ResultObject(ErrorCode.ERROR_OK);
    }

    @Data
    static class StudentCourse {
        int courseId;
        int studentId;
    }

    @RequestMapping(value = "/addStudentCourse")
    @ResponseBody
    @ApiOperation(value = "管理员给课程添加学生", httpMethod = "POST")
    public ResultObject addStudentCourse(@RequestBody StudentCourse studentCourse) {
        if (studentCourse.getCourseId() <= 0 || studentCourse.getStudentId() <= 0) {
            return new ResultObject(ErrorCode.PARAM_ERROR);
        }
        Course course = courserService.selectById(studentCourse.getCourseId());
        if (course.getTeacherId() == null || course.getTeacherId() == 0)
            return new ResultObject(ErrorCode.PARAM_ERROR, "请先选择老师");
        if (course == null) return new ResultObject(ErrorCode.PARAM_ERROR, "课程已经被删除");
        CourseStudent courseStudent = new CourseStudent();
        courseStudent.setStudentId(studentCourse.getStudentId());
        courseStudent.setCourserId(studentCourse.getCourseId());
        try {
            courserService.saveCourseStudent(courseStudent);
            return new ResultObject(ErrorCode.ERROR_OK);
        } catch (CheckedException ex) {
            return new ResultObject(ex.getErrorCode());
        }
    }

    @RequestMapping(value = "/deleteStudent/{id}")
    @ResponseBody
    @ApiOperation(value = "删除学生", httpMethod = "POST")
    public ResultObject deleteStudent(@PathVariable int id) {
        Student student = studentService.selectById(id);
        if (student == null || student.getState() == 1) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "学生已经被删除");
        }
        student.setState((byte) 1);
        studentService.saveStudent(student);
        return new ResultObject(ErrorCode.ERROR_OK);
    }



    @RequestMapping(value = "/deleteCourseStudent")
    @ResponseBody
    @ApiOperation(value = "删除学生课程接口", httpMethod = "POST")
    public ResultObject deleteCourseStudent(@RequestParam("courseId") int courseId,@RequestParam("studentId")int studentId) {
        courserService.
                deleteStudentCourser(courseId,studentId);
        return new ResultObject(ErrorCode.ERROR_OK);
    }

    @Data
    static class StudentResponse {
        private int id;
        private String name;
        private int number;
        private String sex;
    }

    @RequestMapping(value = "/studentList")
    @ResponseBody
    @ApiOperation(value = "学生列表", httpMethod = "POST")
    public ResultObject showStudentList() {
        List<Student> mData = studentService.findStudent();
        List<StudentResponse> result = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getState() == 0) {
                Student student = mData.get(i);
                StudentResponse studentResponse = new StudentResponse();
                studentResponse.setName(student.getName());
                studentResponse.setId(student.getId());
                studentResponse.setNumber(student.getNumber());
                if (student.getSex() == 1) {
                    studentResponse.setSex("男");
                } else {
                    studentResponse.setSex("女");
                }
                result.add(studentResponse);
            }
        }
        return new ResultObject(ErrorCode.ERROR_OK, result);
    }

    @RequestMapping(value = "/teacherList")
    @ResponseBody
    @ApiOperation(value = "老师列表", httpMethod = "POST")
    public ResultObject showTeacherList() {
        List<Teacher> mData = teacherService.selectTeachers();
        List<Teacher> result = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            mData.get(i).setPwd(null);
            if (mData.get(i).getState() == 0) {
                result.add(mData.get(i));
            }
        }
        return new ResultObject(ErrorCode.ERROR_OK, result);
    }

}
