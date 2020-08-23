package net.program.out.stream.byte_demo;


import java.io.IOException;
import java.io.OutputStream;

public class GenerateCharacters {


    public static void main(String[] args) throws IOException, InterruptedException {
        generateCharacters(System.out);
    }

    // ASCII码一共128个字符 每个字符由8位(正好是一个字节)二进制组成
    // 33 - 126 之间为可打印字符
    // 可打印字符的数量为94个
    // 约定每行打印72个
    // 需要打印 多少行？
    static void generateCharacters(OutputStream out) throws IOException, InterruptedException {


        // index = 33; 即第34个字符为 !
        int firstPrintableCharIndex = 33;

        // index = 33 ~ 126 均为可打印字符 127为最后一个字符：删除符
        int numberOfPrintableChars = 94;

        // 每行打印72个字符
        int numberOfCharsPerLine = 72;

        int start = firstPrintableCharIndex;

        int count = 0;
        while (true) {
            if (count > 3) {
                break;
            }
            for (int i = start; i < (start + numberOfCharsPerLine); i++) {
                // index 在33 ~126 之间可打印
                // index 在 0 ~ 93 之间循环打印， 再加上在ASCII中的起始偏移量 33
                Thread.sleep(125);
                int index = ((i - firstPrintableCharIndex) % numberOfPrintableChars) + firstPrintableCharIndex;
                out.write(index);   // 每次输出一个字符(字节) 由于TCP IP Ethernet 头部都会有网络流量的开销，因此TCP/IP各层都会实现缓存。
                // 也就是说 经过一定的积累 或者 一小段时间以后，才会将网卡中缓存的数据发送出去。
                // 如果我们想一次发送多个字节的数据 可以使用write(byte[] bytes) 这样的方法来提高写入效率
                // out.write('\r');
                out.flush();
            }

            // out.write('\r');    // 回车
            out.write('\n');    // 换行
            // System.out 中的 OutputStream 在写字符时，遇到\n才flush， 因此，当我们不写\n 或 不调用flush方法时，数据并不会及时的被flush走
            start = (((start + 1) - firstPrintableCharIndex) % numberOfPrintableChars) + firstPrintableCharIndex;
            count++;
            Thread.sleep(10L);
        }

    }


}
