package net.program.digest;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestThread extends Thread {

    private String fileName = "F:\\resource\\Netty\\geek_netty\\第一章： 初识 Netty：背景、现状与趋势.pdf";

    DigestThread() {
        new DigestThread(fileName);
    }

    DigestThread(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            DigestInputStream in = new DigestInputStream(fis, sha1);

            while (in.read() != -1) ;
            in.close();

            byte[] digest = sha1.digest();
            String digestStr = new String(digest, "utf-8");
            System.out.println(digestStr);
            System.out.println("- - - - - - - - ");

            StringBuilder result = new StringBuilder(fileName);
            result.append(": ").append(DatatypeConverter.printHexBinary(digest));
            System.out.println(result);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new DigestThread().start();
    }
}
