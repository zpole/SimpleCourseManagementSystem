package course.service;

import course.enity.Admin;

/**
 * Created by zpole on 2016/11/19.
 */
public interface AdminService {

    public void saveAdmin(Admin admin);

    public Admin selectByAccount(int account);

    void updateAccount(Admin admin);

}

