package de.stahl;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

public class DistributedTracing {

    private static final Logger log = Logger.getLogger( DistributedTracing.class.getName() );

    public static void main(String[] args) {
        System.out.println(readInputGraphFromTextFile());
    }

    protected static String readInputGraphFromTextFile() {
        try {
            Path path = Paths.get("src/main/resources/InputGraph.txt");
            return Files.readString(path);
        } catch (IOException e) {
            log.log(Level.SEVERE,e.getMessage(),e);
        }
        return null;
    }
}
