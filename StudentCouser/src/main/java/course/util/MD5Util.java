package course.util;

import java.security.MessageDigest;

/**
 * Created by zpole on 2016/8/15.
 */
public class MD5Util {

    public final static String MD5(String s) {
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int val = ((int) md[i]) & 0xff;
                if (val < 16)
                    sb.append("0");
                sb.append(Integer.toHexString(val));

            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public final static String generatePwd(String origilPwd) {
        return MD5(origilPwd + "course");
    }


    public static void main(String[] args) {
        String md =generatePwd("123123");
        System.out.println(md);
    }
}
