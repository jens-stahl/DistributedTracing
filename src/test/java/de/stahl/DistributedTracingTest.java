package de.stahl;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DistributedTracingTest {

    @Test
    void testDistributedTracingInputFilePresent() {
        assertNotNull(DistributedTracing.readInputGraphFromTextFile());
    }
}
