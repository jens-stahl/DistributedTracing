package de.stahl;

import de.stahl.model.Graph;
import de.stahl.model.Node;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DistributedTracingTest {

    @Test
    void testDistributedTracingInputFilePresent() {
        assertNotNull(DistributedTracing.readInputGraphFromTextFile());
    }

    @Test
    void testBuildGraphFromInputFile()   {
        String inputGraphString = DistributedTracing.readInputGraphFromTextFile();
        Graph graph = new Graph(inputGraphString);
        assertNotNull(graph);
        assertTrue(graph.getGraphToplogy().size()==5);
    }

    @Test
    //Make sure latency from A to B is correct with 5:
    void evaluateLatency()   {
        Graph graph = new Graph(DistributedTracing.readInputGraphFromTextFile());
        int latency = graph.evaluateLatency(new Node("A"), new Node("B"));
        assertEquals(latency,5);
    }

}
