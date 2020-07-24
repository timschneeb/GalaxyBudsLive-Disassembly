package com.github.penfeizhou.animation.loader;

import com.github.penfeizhou.animation.io.FileReader;
import com.github.penfeizhou.animation.io.Reader;
import java.io.File;
import java.io.IOException;

public class FileLoader implements Loader {
    private final File mFile;
    private Reader mReader;

    public FileLoader(String str) {
        this.mFile = new File(str);
    }

    public synchronized Reader obtain() throws IOException {
        return new FileReader(this.mFile);
    }
}
