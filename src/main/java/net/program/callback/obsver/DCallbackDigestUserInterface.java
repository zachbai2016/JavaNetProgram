package net.program.callback.obsver;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DCallbackDigestUserInterface implements DigestCallback {

    private String fileName;
    private byte[] digest;
    private Set<DigestCallback> partners = new HashSet<DigestCallback>(10);

    public DCallbackDigestUserInterface(String fileName) {
        this.fileName = fileName;
        partners.add(this);
    }

    public void receiveDigest(byte[] digest) {
        this.digest = digest;
        System.out.println(this);
    }

    public void addPartner(DigestCallback parter) {
        partners.add(parter);
    }

    public void addPartners(List<DigestCallback> parters) {
        partners.addAll(parters);
    }

    void calculateDigest() {
        // 将当前对象注册到工作线程
        WorkerCallbackDigest worker = new WorkerCallbackDigest(fileName);
        for (DigestCallback partner: partners) {
            worker.addListener(partner);
        }
        Thread th = new Thread(worker);
        th.start();
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


    public static void main(String[] args) {
        String fileName = "F:\\resource\\KKB\\spark\\源码\\初探spark内核.md";

        ACallbackDigestUserInterface aInstance = new ACallbackDigestUserInterface(fileName);
        BCallbackDigestUserInterface bInstance = new BCallbackDigestUserInterface(fileName);
        DCallbackDigestUserInterface dInstance = new DCallbackDigestUserInterface(fileName);

        dInstance.addPartner(aInstance);
        dInstance.addPartner(bInstance);
        dInstance.addPartner(dInstance);

        dInstance.calculateDigest();
    }


}












