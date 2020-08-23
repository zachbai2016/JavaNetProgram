package net.program.sync;

import net.program.digest.DigestRunnable;
import net.program.digest.DigestThread;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class NoSyncOutprint implements Runnable {

    private String fileName;


    NoSyncOutprint(String fileName) {
        this.fileName = fileName;
    }

    public void run() {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            DigestInputStream in = new DigestInputStream(fis, sha1);

            byte[] bytes = new byte[1024];
            while (in.read(bytes, 0, bytes.length) != -1) ;
            in.close();

            byte[] digest = sha1.digest();
            String digestStr = new String(digest, "utf-8");
            System.out.println(digestStr);
            System.out.println("- - - - - - - - ");

            // 使用StringBuilder
//            StringBuilder result = new StringBuilder(fileName);
//            result.append(": ").append(DatatypeConverter.printHexBinary(digest));
//            System.out.println(result);
            System.out.printf(DatatypeConverter.printHexBinary(digest));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new NoSyncOutprint("F:\\resource\\Netty\\geek_netty\\第二章： Netty 源码：从“点”（领域知识）的角度剖析.pdf")).start();
        new Thread(new NoSyncOutprint("F:\\resource\\Netty\\网络编程笔记.md")).start();
//        new Thread(new NoSyncOutprint(fileName)).start();
//        new Thread(new NoSyncOutprint(fileName)).start();
    }
}
