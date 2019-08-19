
/*
 *
 */
import java.net.URI;
import java.net.URISyntaxException;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.io.ExportException;

import utilpackage.JgraphTHelloWorld;

public class Main {

	/**
	 * The starting point for the demo.
	 *
	 * @param args ignored.
	 *
	 * @throws URISyntaxException if invalid URI is constructed.
	 * @throws                    org.jgrapht.io.ExportException
	 * @throws ExportException    if graph cannot be exported.
	 * @throws                    org.jgrapht.io.ExportException
	 */
	public static void main(String[] args) throws URISyntaxException, ExportException {
		Graph<String, DefaultEdge> stringGraph = JgraphTHelloWorld.createStringGraph();

		// note undirected edges are printed as: {<v1>,<v2>}
		System.out.println("-- toString output");
		System.out.println(stringGraph.toString());
		System.out.println();

		// create a graph based on URI objects
		Graph<URI, DefaultEdge> hrefGraph = JgraphTHelloWorld.createHrefGraph();

		// find the vertex corresponding to www.jgrapht.org
		URI start = hrefGraph.vertexSet().stream().filter(uri -> uri.getHost().equals("www.jgrapht.org")).findAny()
				.get();

		// perform a graph traversal starting from that vertex
		System.out.println("-- traverseHrefGraph output");
		JgraphTHelloWorld.traverseHrefGraph(hrefGraph, start);
		System.out.println();

		System.out.println("-- renderHrefGraph output");
		JgraphTHelloWorld.renderHrefGraph(hrefGraph);
		System.out.println();
	}
}
