package com.prm392_ass.ass_prm392.activity.authentication;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    // Hàm băm mật khẩu bằng SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Chuyển byte thành hex string
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b)); // mỗi byte -> 2 ký tự hex
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null; // hoặc throw exception tùy bạn xử lý
        }
    }
}

