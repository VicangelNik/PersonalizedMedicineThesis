/*
 *
 */
/*package utilpackage;

import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.net.URI;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.WeightedPseudograph;
import org.jgrapht.io.DOTExporter;
import org.jgrapht.io.GraphExporter;

import smile.graph.Graph.Edge;

public class JGraphT<T> {

	private Class<T> vertexType;

	public JGraphT(Class<T> vertexClass) {
		vertexType = vertexClass;
		String vertex = this.vertexType.getName();
	}

	public WeightedPseudograph<this.vertexType, DefaultEdge> createWeightedPseudograph(smile.graph.Graph smileGraph) {
		Graph<Integer, DefaultEdge> weightedPseudograph = new WeightedPseudograph<>(DefaultEdge.class);
		for (Edge edge : smileGraph.getEdges()) {
			weightedPseudograph.addEdge(edge.v1, edge.v2);
			weightedPseudograph.setEdgeWeight(edge.v1, edge.v2, edge.weight);
		}
		return (WeightedPseudograph<Integer, DefaultEdge>) weightedPseudograph;
	}

	public void exportGraph(Graph graph) {
		Integer vertexIdProvider = new Integer(vertexIdProvider);
		Integer vertexLabelProvider = new Integer(vertexLabelProvider);
		GraphExporter<URI, DefaultEdge> exporter = new <>(vertexIdProvider, vertexLabelProvider, null);
		Writer writer = new StringWriter();
		exporter.exportGraph(graph, writer);
		System.out.println(writer.toString());
	}
}
*/