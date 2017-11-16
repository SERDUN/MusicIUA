package dmitriiserdun.gmail.com.musickiua.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dmitro on 16.11.17.
 */

public class SplitStream {
    private InputStream firstInputStream;
    private InputStream seconInputStream;
    private OutputStream[] outStreams;

    public SplitStream(InputStream originInputStream, OutputStream inputStreamFirst, OutputStream seconInputStream) {
        try {
            outStreams = new OutputStream[2];
            outStreams[0] = new ByteArrayOutputStream();
            outStreams[1] = new ByteArrayOutputStream();
            splitStream(originInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void splitStream(InputStream originInputStream) throws IOException {
        int count;
        int progress = 0;
        byte data[] = new byte[1048576];

        while ((count = originInputStream.read()) != -1) {
            
            outStreams[0].write(count);
            outStreams[1].write(count);
            outStreams[0].flush();
            outStreams[1].flush();

        }
    }

    public InputStream getFirstInputStream() {
        return firstInputStream;
    }

    public InputStream getSeconInputStream() {
        return seconInputStream;
    }
}
