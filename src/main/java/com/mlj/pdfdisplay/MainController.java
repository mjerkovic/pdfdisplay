package com.mlj.pdfdisplay;

import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/pdf")
public class MainController {

    private final String pdfLocation;

    public MainController(String pdfLocation) {
        this.pdfLocation = pdfLocation;
    }

    @RequestMapping(method = GET)
    @ResponseBody
    public String display() {
        return encodeBase64String(readFile());
    }

    private byte[] readFile() {
        File file = new File(pdfLocation);
        byte[] bytes = new byte[(int)file.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(bytes);
            fis.close();

        } catch (IOException e) {
            return bytes;
        }
        finally {
            if (fis != null) {
                try {
                fis.close();
                } catch (IOException ioe) {}
            }
        }
        return bytes;
    }

}
