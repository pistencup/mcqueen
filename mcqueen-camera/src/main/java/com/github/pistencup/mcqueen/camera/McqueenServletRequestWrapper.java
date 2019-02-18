package com.github.pistencup.mcqueen.camera;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;

public class McqueenServletRequestWrapper extends HttpServletRequestWrapper {

    private byte[] bodyBytes;

    McqueenServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() {

        final ByteArrayInputStream stream = new ByteArrayInputStream(bodyBytes);
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {}

            @Override
            public int read() {
                return stream.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}