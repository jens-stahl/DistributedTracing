package de.stahl.util;

import de.stahl.DistributedTracing;

import javax.print.URIException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    private static final Logger log = Logger.getLogger(Util.class.getName());

    public static String readInputGraphFromTextFile() {
        try {
            InputStream inputStream = Util.class.getResourceAsStream("/InputGraph.txt");
            return readFromInputStream(inputStream);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

    private static String readFromInputStream(InputStream inputStream)
            throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            return br.readLine();
        }
    }

}
