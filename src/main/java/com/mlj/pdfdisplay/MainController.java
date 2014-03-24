package com.mlj.pdfdisplay;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/pdf")
public class MainController {

    private final String pdfLocation;
    private String encodedFileContents;

    public MainController(String pdfLocation) {
        this.pdfLocation = pdfLocation;
        encodedFileContents = Base64.encodeBase64String(readFile());
    }

    @RequestMapping(method = GET)
    @ResponseBody
    public String display() {
        return "http://192.168.7.137:8080/pdf/tos/1";
    }

    @RequestMapping(value = "/tos/{id}", method = GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void tos(HttpServletResponse response, @PathVariable int id) {
        streamFileToClent(response);
    }

    @RequestMapping(value = "/tos/{id}", method = HEAD)
    @ResponseBody
    public String contentType(@RequestHeader("User-Agent") String userAgent) {
        return "application/pdf";
    }

    private void streamFileToClent(HttpServletResponse response) {
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(Base64.decodeBase64(encodedFileContents));
            IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            IOUtils.closeQuietly(is);
        }
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
