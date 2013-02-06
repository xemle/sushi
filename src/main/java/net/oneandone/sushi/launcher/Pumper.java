package net.oneandone.sushi.launcher;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class Pumper extends Thread {
    public static Pumper create(Object streamOrReader, Object streamOrWriter, boolean flushDest, boolean closeDest, String encoding) {
        if (streamOrWriter instanceof OutputStream) {
            try {
                streamOrWriter = new OutputStreamWriter((OutputStream) streamOrWriter, encoding);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }
        if (streamOrReader instanceof InputStream) {
            try {
                streamOrReader = new InputStreamReader((InputStream) streamOrReader, encoding);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }
        }
        return new Pumper((Reader) streamOrReader, (Writer) streamOrWriter, flushDest, closeDest);
    }

    private Throwable exception;
    private final char[] buffer;
    private final Reader src;
    private final Writer dest;
    private final boolean flushDest;
    private final boolean closeDest;

    public Pumper(Reader src, Writer dest, boolean flushDest, boolean closeDest) {
        this.buffer = new char[1024];
        this.src = src;
        this.dest = dest;
        this.flushDest = flushDest;
        this.closeDest = closeDest;
        setDaemon(true);
    }


    @Override
    public void run() {
        try {
            runUnchecked();
        } catch (Throwable e) {
            exception = e;
            return;
        }
    }

    private void runUnchecked() throws IOException {
        int len;

        while (true) {
            len = src.read(buffer);
            if (len == -1) {
                if (closeDest) {
                    dest.close();
                } else {
                    dest.flush();
                }
                return;
            }
            dest.write(buffer, 0, len);
            if (flushDest) {
                dest.flush();
            }
        }
    }

    public void finish(Launcher launcher) throws Failure {
        try {
            join();
        } catch (InterruptedException e) {
            throw new Interrupted(e);
        }
        if (exception != null) {
            if (exception instanceof IOException) {
                throw new Failure(launcher, (IOException) exception);
            } else if (exception instanceof Error) {
                throw (Error) exception;
            } else if (exception instanceof RuntimeException) {
                throw (RuntimeException) exception;
            } else {
                throw new IllegalStateException(exception);
            }
        }
    }
}
