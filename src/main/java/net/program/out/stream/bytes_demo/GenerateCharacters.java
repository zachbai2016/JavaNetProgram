package net.program.out.stream.bytes_demo;


import java.io.IOException;
import java.io.OutputStream;

public class GenerateCharacters {


    public static void main(String[] args) throws IOException, InterruptedException {
        // System.out 默认是不会将我们单次写入的数据 执行flush操作 直到碰见 \n
        generateCharacters(System.out);
    }

    // 每次输出一个字符(字节) 由于TCP IP Ethernet 头部都会有网络流量的开销，因此TCP/IP各层都会实现缓存。
    // 也就是说 经过一定的积累 或者 一小段时间以后，才会将网卡中缓存的数据发送出去。
    // 如果我们想一次发送多个字节的数据 可以使用write(byte[] bytes) 这样的方法来提高写入效率
    public static void generateCharacters(OutputStream out) throws IOException, InterruptedException {

        int firstPrintableCharIndex = 33;
        int numberOfPrintableChars = 94;
        int numberOfCharsPerLine = 72;
        int start = firstPrintableCharIndex;

        byte[] line;

        int count = 0;
        while (true) {
            line = new byte[numberOfCharsPerLine + 2];   // 2 表示\r \n
            if (count > 5) {
                break;
            }
            int i;
            for (i = start; i < (start + numberOfCharsPerLine); i++) {
                // 这里是start 不是 firstPrintableCharIndex 否则只能写一次正确， 其余两次错写漏泄 剩余的写会报数组下标越界
                // 原因就是start是移动的偏移量 而firstPrintableCharIndex是固定的偏移量 写的下的时候不会报错 写不下就会报错
                line[i - start] = (byte) (((i - firstPrintableCharIndex) % numberOfPrintableChars) + firstPrintableCharIndex);
            }
            line[72] = '\r';    // 回车
            if (i % 2 == 0) {
                line[73] = '\n';    // todo 这里只会对偶数次输入的数据做flush 而奇数次写入的数据会丢失
            }
            // line[73] = '\n';    // 换行 todo 屏蔽掉 \n试试 这里很奇妙 不写 \n 时数据时不会被flush走的， 因为System.ou 中的PrintOutStream 遇到 \n 才执行 flush 操作
            // 这就会出现一个很奇怪的现象：我们不写 \n 数据就一直不会被送走
            out.write(line);    // 一次写入一个字节数组到网卡当中 而网卡内部是设置了缓冲 不一定会立即将我们的数据发送出去
            // 写了这么多实际上是写入了对应设备的缓冲区当中，例如网卡的缓冲区，磁盘对应的的缓冲区，并没有将数据真正的通过网卡送走或是写入到磁盘。
            // 硬件中有缓冲区，软件层面也有缓冲区，就是Buffer*，硬件层面的缓冲区就是设备的缓冲区。
            // todo 将写入的数据及时送走很重要
            // out.flush();        // 有 \n 时, 这里即使不写 也会被flush走, 但是如果没有flush 也没有 \n 数据就不会被flush走 因此会造成错误
            start = (((start + 1) - firstPrintableCharIndex) % numberOfPrintableChars) + firstPrintableCharIndex;
            count++;
            Thread.sleep(1000L);
        }
        out.close();    // close方法会关闭文件句柄 或 端口
        // 如果这个流来自于connection， 那么close这个流 也会关闭connection
        // socket channel jdbc 都很常见
        // try - with resources
        // try - with resources 中 try 块中申明的变量，不需要再在finally 中做关闭， 只要这些变量的类实现了AutoCloseable 接口， JVM会帮我们自动关闭。


    }


}
