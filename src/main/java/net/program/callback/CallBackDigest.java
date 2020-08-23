package net.program.callback;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CallBackDigest implements Runnable {

    String fileName;
    byte[] digest;

    public CallBackDigest(String fileName) {
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
            digest = md.digest();
            // 静态回调
            CallBackDigestUserInterface.receiveDigest(digest, fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
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
