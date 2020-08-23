package net.program.callback.obsver;

import javax.xml.bind.DatatypeConverter;

public class BCallbackDigestUserInterface implements DigestCallback {

    byte[] digest;
    String fileName;

    public BCallbackDigestUserInterface(String fileName) {
        this.fileName = fileName;
    }

    public void receiveDigest(byte[] digest) {
        this.digest = digest;
        System.out.println(this);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(getClass().getSimpleName() +" - " + fileName + " : ");
        if (digest != null) {
            result.append(DatatypeConverter.printHexBinary(digest));
        } else {
            result.append("digest is not available");
        }
        return result.toString();
    }

}
