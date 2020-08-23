package net.program.callback;

import javax.xml.bind.DatatypeConverter;

/**
 * 静态回调
 */
public class CallBackDigestUserInterface {

    // 这里回调的回调静态方法 回调实例方法更为常见
    public static void receiveDigest(byte[] digest, String fileName) {
        StringBuilder result = new StringBuilder(fileName + ": ").append(DatatypeConverter.printHexBinary(digest));
        System.out.println(result.toString());
    }

    public static void main(String[] args) {
        CallBackDigest callBackDigest = new CallBackDigest("F:\\resource\\KKB\\作业\\20200715+白阳+SparkSql第一次课.docx");
        Thread th = new Thread(callBackDigest);
        th.start();
    }

}
