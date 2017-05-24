package course.aop;

import course.enity.Admin;
import course.enity.Student;
import course.enity.Teacher;
import course.util.ErrorCode;
import course.util.ResultObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by misskey on 2016/11/22.
 */
@Component
@Aspect
@Slf4j
public class PermissionAop {

    @Pointcut("within(course.controller.*)")
    public void checkPermission() {

    }

    /**
     * 检查相应的权限
     */
    @Around("checkPermission()")
    public Object record(ProceedingJoinPoint point) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
        Student student = (Student) request.getSession().getAttribute("student");
        Admin admin = (Admin) request.getSession().getAttribute("admin");
        String requestUrl = request.getRequestURI();
        if (requestUrl.contains("/login")) {
            try {
                return point.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return new ResultObject(ErrorCode.INNER_ERROR);
            }
        }
        if (requestUrl.contains("/admin")) {
            if (admin == null) {
                return new ResultObject(ErrorCode.NO_LOGIN, "账号没有登陆");
            }
            try {
                return point.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return new ResultObject(ErrorCode.INNER_ERROR);
            }
        }
        if (requestUrl.contains("/teacher/")) {
            if (teacher == null) {
                return new ResultObject(ErrorCode.NO_LOGIN, "账号没有登陆");
            }
            try {
                return point.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return new ResultObject(ErrorCode.INNER_ERROR);
            }
        }
        if (requestUrl.contains("/student")) {
            if (student == null) {
                return new ResultObject(ErrorCode.NO_LOGIN, "账号没有登陆");
            }
            try {
                return point.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                return new ResultObject(ErrorCode.INNER_ERROR);
            }
        }
        try {
            return point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return new ResultObject(ErrorCode.INNER_ERROR);
        }
    }
}
