package com.joker.pacific.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by joker on 17-10-31.
 */

public class Md5 {
    public static String getMd5(String txt) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(txt.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Can not get MD5 message digest!!!", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Can not support encoding utf8!!!", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    public static String getMd5(byte txt[]) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(txt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Can not get MD5 message digest!!!", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static String getMd5(File file) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            if (in != null) {
                MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(byteBuffer);
                byte[] hash = md5.digest();
                StringBuilder hex = new StringBuilder(hash.length * 2);
                for (byte b : hash) {
                    if ((b & 0xFF) < 0x10) hex.append("0");
                    hex.append(Integer.toHexString(b & 0xFF));
                }
                return hex.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
}
