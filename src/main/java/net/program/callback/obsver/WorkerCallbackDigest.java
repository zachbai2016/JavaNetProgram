package net.program.callback.obsver;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// 多个实例回调
public class WorkerCallbackDigest implements Runnable {

    private String fileName;

    private Set<DigestCallback> listeners = new HashSet<DigestCallback>(10);

    public WorkerCallbackDigest(String fileName) {
        this.fileName = fileName;
    }

    public void addListener(DigestCallback listener) {
        listeners.add(listener);
    }

    public void run() {
        DigestInputStream in = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            in = new DigestInputStream(new BufferedInputStream(new FileInputStream(fileName)), md);
            byte[] bytes = new byte[1024];
            // 读取
            while (in.read(bytes) != -1) ;
            // 实例回调
            tellResult(md.digest());
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

    // 批量回调
    void tellResult(byte[] digest) {
        if (listeners.isEmpty()) {
            return;
        }
        for (DigestCallback listener : listeners) {
            listener.receiveDigest(digest);
        }
    }

}
