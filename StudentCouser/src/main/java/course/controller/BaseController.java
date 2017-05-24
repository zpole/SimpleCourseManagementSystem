package course.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import course.enity.Admin;
import course.enity.Student;
import course.enity.Teacher;
import course.util.ErrorCode;
import course.util.ResultObject;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by zpole on 2016/11/26.
 */
@Controller
@RequestMapping(value = "/user")
@Api(value = "退出接口")
public class BaseController {
    @RequestMapping("/exit")
    @ResponseBody
    @ApiOperation(value = "退出系统", httpMethod = "POST")
    public ResultObject exit(HttpSession httpSession) {
        Teacher teacher = (Teacher) httpSession.getAttribute("teacher");
        Student student = (Student) httpSession.getAttribute("student");
        Admin admin = (Admin) httpSession.getAttribute("admin");
        if (teacher == null && student == null && admin == null) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "用户没有登录");
        }
        httpSession.removeAttribute("teacher");
        httpSession.removeAttribute("student");
        httpSession.removeAttribute("admin");
        httpSession.invalidate();
        return new ResultObject(ErrorCode.ERROR_OK, "成功退出");
    }


    @Data
    static class UserInfo {
        private String info;
        private String name;
    }

    @RequestMapping("/info")
    @ResponseBody
    @ApiOperation(value = "获得当前用户信息", httpMethod = "POST")
    public ResultObject getUserInfo(HttpSession httpSession) {
        Teacher teacher = (Teacher) httpSession.getAttribute("teacher");
        Student student = (Student) httpSession.getAttribute("student");
        Admin admin = (Admin) httpSession.getAttribute("admin");
        if (teacher == null && student == null && admin == null) {
            return new ResultObject(ErrorCode.PARAM_ERROR, "用户没有登录");
        }
        UserInfo userInfo = new UserInfo();
        if (teacher != null) {
            userInfo.setInfo("老师");
            userInfo.setName(teacher.getName());
        } else if (student != null) {
            userInfo.setInfo("学生");
            userInfo.setName(student.getName());
        } else if (admin != null) {
            userInfo.setInfo("管理员");
            userInfo.setName("管理员");
        }
        return new ResultObject(ErrorCode.ERROR_OK, userInfo);
    }
}
