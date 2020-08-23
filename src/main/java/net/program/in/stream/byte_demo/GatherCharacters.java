package net.program.in.stream.byte_demo;

import net.program.out.stream.bytes_demo.GenerateCharacters;

import java.io.IOException;
import java.io.InputStream;

public class GatherCharacters {

    public static void main(String[] args) throws IOException, InterruptedException {
        gactherCharacters(System.in);
    }

    static void gactherCharacters(InputStream in) throws IOException, InterruptedException {
        byte[] bytes = new byte[10];
        int read, i = 0;
        while ((read = in.read()) != -1) {
            bytes[i++] = (byte) (read > 0 ? read : read + 256);
        }
        in.close();
    }

}
