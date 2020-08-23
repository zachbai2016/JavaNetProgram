package net.program.thread;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ReturnDigest extends Thread {

    private String fileName = "F:\\resource\\Netty\\geek_netty\\第一章： 初识 Netty：背景、现状与趋势.pdf";
    private byte[] digest;
    private final static Object lock = new Object();

    ReturnDigest() {
        new ReturnDigest(fileName);
    }

    ReturnDigest(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {

            FileInputStream fis = new FileInputStream(fileName);
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            DigestInputStream in = new DigestInputStream(fis, sha1);

            byte[] bytes = new byte[1024];
            synchronized (lock) {   // 锁对象不能为空
                while (in.read(bytes, 0, bytes.length) != -1) ;
                in.close();

                digest = sha1.digest();
                System.err.println("end  digest.");
                lock.notifyAll(); // 进锁对象 和 调用锁的notify对象不是同一个 会报IllegalMonitorStateException 简称IMSE
                // 即使调用了notify 只能说明唤醒了其他等待的线程  主线程由原来的 wait 变成了 monitor，
                // 但是由于 当前thread-0 还没有退出这个monitor 的 monitor exit， 所以其他线程仍然无法进入 monitor enter
                // 还没有退出monitor exit, 虽然wait的线程被唤醒了， 但是仍然无法进入 monitor enter
            }   // 在这里退出monitor exit，其他被唤醒的线程中的某一个人，才能由monitor 修改锁的状态 进入monitor enter, 剩余被唤醒的线程 继续在 monitor

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReturnDigest returnDigest = new ReturnDigest();
        returnDigest.start();

        returnDigest.join();    // 相当于调用了 synchronized(returnDigest) {returnDigest.wait();}
        // Thread.sleep(100);
        System.out.println("returnDigest - " + returnDigest.getDigest());
    }

    // 阻塞式获取计算结果
    private String getDigest() throws InterruptedException {
        synchronized (lock) {
            if (digest == null) {
                System.out.println("digest == null, into wait.");
                lock.wait();    // 在进入monitor enter 后， 如果资源为空，则进入wait 状态，等到资源被另一个线程计算好后，执行notify 或 notifyAll 的时候，
                // 这个wait的线程 先转变成 monitor 状态，再次成功拿到锁之后才会重新进入running 状态
                System.out.println("wait end.");
            }
            System.out.println("digest != null");
            return new StringBuilder(fileName).append(DatatypeConverter.printHexBinary(digest)).toString();
        }
    }
}
