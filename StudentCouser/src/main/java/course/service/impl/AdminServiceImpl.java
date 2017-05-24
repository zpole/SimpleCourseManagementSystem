package course.service.impl;

import course.dao.AdminMapper;
import course.dao.CourseMapper;
import course.dao.StudentMapper;
import course.dao.TeacherMapper;
import course.enity.Admin;
import course.service.AdminService;
import course.util.CheckedException;
import course.util.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * Created by zpole on 2016/11/19.
 */
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public void saveAdmin(Admin admin) {
        try {
            if (admin.getId() != null) {
                adminMapper.updateByPrimaryKeySelective(admin);
            } else {
                adminMapper.insertSelective(admin);
            }
        } catch (DuplicateKeyException duplication) {
            throw new CheckedException(ErrorCode.Exit_STUDENT);
        }
    }

    @Override
    public Admin selectByAccount(int account) {
        return adminMapper.selectByAccount(account);
    }

    @Override
    public void updateAccount(Admin admin) {
        if (admin.getId() != null) {
            adminMapper.updateByPrimaryKey(admin);
        } else {
            try {
                adminMapper.insertSelective(admin);
            } catch (DuplicateKeyException duplicationEx) {
                throw new CheckedException(ErrorCode.EXIT_ADMIN);
            }
        }

    }

}
