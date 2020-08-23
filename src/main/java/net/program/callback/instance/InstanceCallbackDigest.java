package net.program.callback.instance;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// 实例回调
public class InstanceCallbackDigest implements Runnable {

    // 文件名
    String fileName;

    // 回调对象
    InstanceCallbackDigestUserInterface callback;

    // 构造方法中将回调实例作为成员变量初始化
    public InstanceCallbackDigest(String fileName, InstanceCallbackDigestUserInterface callback) {
        this.fileName = fileName;
        this.callback = callback;
    }

    public InstanceCallbackDigest(String fileName) {
        this.fileName = fileName;
    }

    public void run() {

        DigestInputStream in = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            in = new DigestInputStream(new BufferedInputStream(new FileInputStream(fileName)), md);
            byte[] bytes = new byte[1024];
            // 读取
            while (in.read(bytes) != -1) ;
            // 完成计算后 通过实例回调
            callback.reveiveDigest(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
