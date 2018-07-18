package com.jjs.base.pay;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 说明：
 * Created by jjs on 2018/7/18.
 */

public class WxMD5 {

    public static String md5(String str) {
        if(str == null) {
            return "";
        } else {
            MessageDigest messageDigest = null;

            try {
                messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.reset();
                messageDigest.update(str.getBytes("UTF-8"));
            } catch (NoSuchAlgorithmException var5) {
                var5.printStackTrace();
            } catch (UnsupportedEncodingException var6) {
                var6.printStackTrace();
            }

            if(messageDigest == null) {
                return null;
            } else {
                byte[] byteArray = messageDigest.digest();
                StringBuffer md5StrBuff = new StringBuffer();

                for(int i = 0; i < byteArray.length; ++i) {
                    if(Integer.toHexString(255 & byteArray[i]).length() == 1) {
                        md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
                    } else {
                        md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
                    }
                }

                return md5StrBuff.toString();
            }
        }
    }

    public static String md5(byte[] target, int len) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(target, 0, len);
        } catch (NoSuchAlgorithmException var6) {
            var6.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();

        for(int i = 0; i < byteArray.length; ++i) {
            if(Integer.toHexString(255 & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
            }
        }

        return md5StrBuff.toString();
    }

    public static String getFileMD5(File file) {
        if(!file.isFile()) {
            return null;
        } else {
            MessageDigest digest = null;
            FileInputStream in = null;
            byte[] buffer = new byte[1024];

            try {
                digest = MessageDigest.getInstance("MD5");
                in = new FileInputStream(file);

                while(true) {
                    int len;
                    if((len = in.read(buffer, 0, 1024)) == -1) {
                        in.close();
                        break;
                    }

                    digest.update(buffer, 0, len);
                }
            } catch (Exception var6) {
                var6.printStackTrace();
                return null;
            }

            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        }
    }

    public static Map<String, String> getDirMD5(File file, boolean listChild) {
        if(!file.isDirectory()) {
            return null;
        } else {
            HashMap map = new HashMap();
            File[] files = file.listFiles();

            for(int i = 0; i < files.length; ++i) {
                File f = files[i];
                if(f.isDirectory() && listChild) {
                    map.putAll(getDirMD5(f, listChild));
                } else {
                    String md5 = getFileMD5(f);
                    if(md5 != null) {
                        map.put(f.getPath(), md5);
                    }
                }
            }

            return map;
        }
    }
}
