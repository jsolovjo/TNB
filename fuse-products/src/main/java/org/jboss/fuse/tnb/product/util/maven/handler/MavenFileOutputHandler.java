package org.jboss.fuse.tnb.product.util.maven.handler;

import java.io.Closeable;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Path;

public class MavenFileOutputHandler implements MavenOutputHandler, Closeable {

    private PrintWriter in;
    private Path file;

    public MavenFileOutputHandler(Path outputFile) throws IOException {
        this.in = new PrintWriter(new FileWriter(outputFile.toFile()), true);
        this.file = outputFile;
    }

    @Override
    public void consumeLine(String s) throws IOException {
        in.println(s);
    }

    @Override
    public Reader getOutput() {
        try {
            return new FileReader(file.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Can't find the log file", e);
        }
    }

    @Override
    public void close() throws IOException {
        in.close();
    }
}
