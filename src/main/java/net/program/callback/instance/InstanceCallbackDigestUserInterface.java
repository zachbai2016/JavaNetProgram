package net.program.callback.instance;

import javax.xml.bind.DatatypeConverter;

public class InstanceCallbackDigestUserInterface {

    private String fileName;
    private byte[] digest;

    // 尝试在构造函数里 把自己注册到两外一个task中去，
    public InstanceCallbackDigestUserInterface(String fileName) throws InterruptedException {

        // 将当前对象注册到工作线程
        InstanceCallbackDigest callbackDigest = new InstanceCallbackDigest(fileName, this);
        Thread th = new Thread(callbackDigest);
        th.start();

        Thread.sleep(5000L);


        this.fileName = fileName;
    }

    // 避免在构造函数中启动新的线程
    // 工作线程回调之前 当前线程初始化这个对象可能还没完成，就会报NPE
    // 避免在构造函数中 启动新的线程 将自己注册出去 因为工作线程可能工作特别快以至于回调的时候 被注册过去的对象在构造函数里还没有完成初始化
    // 回调的时候是使用这个初始化对象的方法，调用方法时可能会报NPE
    void calculateDigest() throws InterruptedException {

    }

    void reveiveDigest(byte[] digest) {
        this.digest = digest;
        System.out.println(this);
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(fileName + " : ");
        if (digest != null) {
            result.append(DatatypeConverter.printHexBinary(digest));
        } else {
            result.append("digest is not available");
        }
        return result.toString();
    }


    public static void main(String[] args) throws InterruptedException {
        // 实例初始化完成后
        InstanceCallbackDigestUserInterface digestUserInterface = new InstanceCallbackDigestUserInterface("F:\\resource\\KKB\\spark\\源码\\初探spark内核.md");
        // 调用自己的方法，把自己注册过去
        // digestUserInterface.calculateDigest();
    }


}











