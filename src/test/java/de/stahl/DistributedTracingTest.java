package de.stahl;

import de.stahl.model.Graph;
import de.stahl.model.Node;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DistributedTracingTest {

    String inputGraphString =null;
    Graph graph = null;

    @BeforeAll
    void loadFileAndGraph() {
        inputGraphString = DistributedTracing.readInputGraphFromTextFile();
        graph = new Graph(inputGraphString);
        assertNotNull(inputGraphString);
        assertNotNull(graph);
        assertTrue(graph.getGraphToplogy().size()==5);
    }

    @Test
    //Make sure latency from A to B is 5:
    void evaluateLatency()   {
        int latency = graph.evaluateOverallLatency(new Node("A"), new Node("B"));
        assertEquals(latency,5);
    }

    @Test
    //Make sure latency from A to C is 9:
    void evaluateLatency1()   {
        int latency = graph.evaluateOverallLatency(new Node("A"), new Node("B"), new Node("C"));
        assertEquals(latency,9);
    }

    @Test
    //Make sure latency from A to D is 5:
    void evaluateLatency2()   {
        int latency = graph.evaluateOverallLatency(new Node("A"), new Node("D"));
        assertEquals(latency,5);
    }

    @Test
    //Make sure latency from A,D,C is 13:
    void evaluateLatency3()   {
        int latency = graph.evaluateOverallLatency(new Node("A"), new Node("D"),new Node("C"));
        assertEquals(latency,13);
    }

    @Test
    //Make sure latency from A,E,B,C,D is 22:
    void evaluateLatency4()   {
        int latency = graph.evaluateOverallLatency(new Node("A"), new Node("E"),new Node("B"),new Node("C"),new Node("D"));
        assertEquals(latency,22);
    }

    @Test
    //Result of A,E,D is "NO SUCH TRACE":
    void evaluateLatency5()   {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            graph.evaluateOverallLatency(new Node("A"), new Node("E"),new Node("D"));
        });
        assert(exception.getMessage().equals("NO SUCH TRACE"));
    }

    @Test
    //Result of C,C with max 3 hops should be 2
    void evaluateRouteCount6()   {
        List<Stack> allConnectionPaths = graph.evaluateEdges(new Node("C"), new Node("C"), 3, null);
        List<List<Node>> informationAboutRoutes = graph.getNodeListsFromEdges(allConnectionPaths);
        assertEquals(2, informationAboutRoutes.size());
    }

    @Test
    //Result of A,C with exactly 4 hops should be 3
    void evaluateRouteCount7()   {
        List<Stack> allConnectionPaths = graph.evaluateEdges(new Node("A"), new Node("C"), null, 4);
        List<List<Node>> informationAboutRoutes = graph.getNodeListsFromEdges(allConnectionPaths);
        assertEquals(3,informationAboutRoutes.size());
    }

    @Test
    //shortest latency of A,C
    void evaluateRouteCount8()   {
        List<Stack> allConnectionPaths = graph.evaluateEdges(new Node("A"), new Node("C"), null, null);
        int minimumLatency = graph.getMinimumLatency(allConnectionPaths);
        assertEquals(9,minimumLatency);
    }

    @Test
    //shortest latency of B,C
    void evaluateRouteCount9()   {
        List<Stack> allConnectionPaths = graph.evaluateEdges(new Node("B"), new Node("B"), 10, null);
        int minimumLatency = graph.getMinimumLatency(allConnectionPaths);
        assertEquals(9,minimumLatency);
    }

    @Test
    //average latency of C,C with less then 30
    void evaluateRouteCount10()   {
        List<Stack> allConnectionPaths = graph.evaluateEdges(new Node("C"), new Node("C"), null, null);
        int count = graph.countPathsWithMaxLatency(allConnectionPaths,30);
        assertEquals(7,count);
    }
}
