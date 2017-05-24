package course.util;


/**
 * Created by zpole on 2016/11/19.
 */
public enum ErrorCode {
    ERROR_OK(100, "正确请求"),
    Exit_STUDENT(400, "已经存在学号"),
    EXIT_COURSE(401, "已经存在该课程"),
    EXIT_TEACHER(406, "已经存在该老师"),
    NO_INPUT_OLDPWD(402, "请输入原来的密码"),
    EXIT_ADMIN(403, "已经存在改账号"),
    PARAM_ERROR(404, "参数不正确"),
    PWD_NO_CORRECT(405, "原密码不正确"),
    NO_PERMISSION(500, "没有权限"),
    INNER_ERROR(502, "内部错误"),
    NO_LOGIN(504, "没有登录"),
    NO_PIC(510, "不是图片格式"),
    EXIT_STUDENT_COURSE(501, "已经选择该课程");

    ErrorCode(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public ErrorCode findCode(int errorCode) {
        ErrorCode[] codes = values();
        for (ErrorCode code : codes) {
            if (code.errorCode == errorCode) {
                return code;
            }
        }
        return null;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.msg;
    }

    private int errorCode;
    private String msg;
}
