package Crown;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.HopcroftKarpBipartiteMatching;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * CrownReduction is a class that takes an undirected
 * subgraph and locates a crown structure based on
 * a certain set of criteria. This structure is then
 * removed from the graph and two output files are
 * created containing the vertices that belong in the
 * vertex cover from the crown and the rest of the graph
 * minus the crown.
 * 
 * @author Siddhartha
 *
 */
public class CrownReduction {
	
	UndirectedGraph<Integer, DefaultEdge> graph = 
            new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
	UndirectedGraph<Integer, DefaultEdge> subGraph = 
            new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
	UndirectedGraph<Integer, DefaultEdge> finalCrown = 
            new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
	
	Set<Integer> i = new HashSet<Integer>(new ArrayList<Integer>());
    Set<Integer> m = new HashSet<Integer>(new ArrayList<Integer>());
    Set<Integer> crownOutput = new HashSet<Integer>(new ArrayList<Integer>());
    Set<Integer> vertices = new HashSet<Integer>(new ArrayList<Integer>());
    Set<Integer> cVertices = new HashSet<Integer>(new ArrayList<Integer>());
    Set<Integer> pVertices = new HashSet<Integer>(new ArrayList<Integer>());
    
    Set<DefaultEdge> maximum;

	/**
	 * Main method that checks for a file argument and starts the
	 * algorithms.
	 * @param args array of command line inputs
	 */
	public static void main(String[] args) {
		long time = System.nanoTime();
		if (args.length != 1) {
			System.out.println("Usage: java CrownReduction <filename>");
		} else {
			CrownReduction crown = new CrownReduction();
			crown.startUI(args[0]);
		}
		System.out.println("Runtime = " + (System.nanoTime() - time));
	}

	/**
	 * Runs through the methods to write the final
	 * outputs to file
	 */
	public void startUI(String file) {
		Scanner input = getInputScanner(file);
		PrintStream crown = getOutputPrintStream("crownOutput.txt");
		PrintStream reduc = getOutputPrintStream("reductionOutput.txt");
		processInput(input);
		findSubGraph();
		hopcroftMatching();
		obtainCrown();
		removeCrown(crown, reduc);
	}
	
	/**
	 * Gets the input file and returns it.
	 * 
	 * @param name a String to obtain an input file.
	 * @return input the input file to be manipulated.
	 */
	public Scanner getInputScanner(String name) {
		Scanner input = null;	
			try {
				input = new Scanner(new File(name));
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			}
		return input;
	}
	
	/**
	 * Initializes an output file and returns it.
	 * 
	 * @return output the output file to write to.
	 */
	public PrintStream getOutputPrintStream(String filename) {
		PrintStream output = null;
		String name = filename;

		try {
			output = new PrintStream(new File(name));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return output;
	}
	
	/**
	 * Processes the input file and populates the ArrayList.
	 * 
	 * @param input scanner that contains the file to process
	 */
	public void processInput(Scanner input) {
		int v1;
		int v2;
		while(input.hasNextLine()) {
			v1 = input.nextInt();
			v2 = input.nextInt();
			input.nextLine().trim();
			
			graph.addVertex(v1);
			graph.addVertex(v2);
			graph.addEdge(v1, v2);
		}
	}
	
	/**
	 * This method find the subgraph within the undirected graph
	 * that is made up of a set of independent edges and a set
	 * of independent vertices.
	 */
	public void findSubGraph() {
		Set<DefaultEdge> graphEdges = graph.edgeSet();
		vertices.addAll(graph.vertexSet());
		Iterator<DefaultEdge> itr = graphEdges.iterator();
		DefaultEdge e;
		int v1;
		int v2;
		while(itr.hasNext()) {
			e = itr.next();
			v1 = graph.getEdgeSource(e);
			v2 = graph.getEdgeTarget(e);
			
			if (vertices.contains(v1) && vertices.contains(v2)) {
				m.add(v1);
				m.add(v2);
				vertices.remove(v1);
				vertices.remove(v2);
			}
		}
		
		i = vertices;
		Iterator<Integer> itrInt = vertices.iterator();
		int independant;
		Set<DefaultEdge> indEdges;
		while(itrInt.hasNext()) {
			independant = itrInt.next();
			indEdges = graph.edgesOf(independant);
			itr = indEdges.iterator();
			while(itr.hasNext()) {
				e = itr.next();
				v1 = graph.getEdgeSource(e);
				v2 = graph.getEdgeTarget(e);
				
				subGraph.addVertex(v1);
				subGraph.addVertex(v2);
				subGraph.addEdge(v1, v2);
			}
		}
		
		Set<DefaultEdge> subEdges = subGraph.edgeSet();
		Set<Integer> subVertices = subGraph.vertexSet();
		itr = subEdges.iterator();
		while(itr.hasNext()) {
			e = itr.next();
			v1 = graph.getEdgeSource(e);
			v2 = graph.getEdgeTarget(e);
			if (!subVertices.contains(v1)) {
				subGraph.removeEdge(e);
				m.remove(v1);
			}
			if (!subVertices.contains(v2)) {
				subGraph.removeEdge(e);
				m.remove(v2);
			}
		}
	}
	
	/**
	 * Uses the Hopcroft Matching method to return the set of
	 * maximum matching edges between the two sets of input vertices. 
	 */
	public void hopcroftMatching() {
		HopcroftKarpBipartiteMatching<Integer, DefaultEdge> hkbm = 
				new HopcroftKarpBipartiteMatching<Integer, DefaultEdge>(subGraph, i, m);
		maximum = hkbm.getMatching();
		Iterator<DefaultEdge> itr = maximum.iterator();
		DefaultEdge e;
		while(itr.hasNext()) {
			e = itr.next();
			i.remove(subGraph.getEdgeSource(e));
			i.remove(subGraph.getEdgeTarget(e));
		}
	}
	
	/**
	 * Takes the set of edges from Hopcroft's method and uses
	 * it to locate the crown structure within the subgraph
	 */
	public void obtainCrown() {
		Set<DefaultEdge> neighbors;
		Iterator<Integer> itrInt = i.iterator();
		Integer io;
		Iterator<DefaultEdge> itr;
		DefaultEdge e;
		int v1;
		int v2;
		while(itrInt.hasNext()) {
			io = itrInt.next();
			neighbors = subGraph.edgesOf(io);
			itr = neighbors.iterator();
			while(itr.hasNext()) {
				e = itr.next();
				v1 = subGraph.getEdgeSource(e);
				v2 = subGraph.getEdgeTarget(e);
				finalCrown.addVertex(v1);
				finalCrown.addVertex(v2);
				finalCrown.addEdge(v1, v2);
				i.remove(io);
			}
			
			cVertices.addAll(finalCrown.vertexSet());
			itr = maximum.iterator();
			while(itr.hasNext()) {
				e = itr.next();
				v1 = subGraph.getEdgeSource(e);
				v2 = subGraph.getEdgeTarget(e);
				if (cVertices.contains(v1) && cVertices.contains(v2)) {
					//Do nothing
				} else {
					if (cVertices.contains(v1)) {
						finalCrown.addVertex(v2);
						finalCrown.addEdge(v1, v2);
						i.add(v2);
						crownOutput.add(v1);
					}
					if (cVertices.contains(v2)) {
						finalCrown.addVertex(v1);
						finalCrown.addEdge(v1, v2);
						i.add(v1);
						crownOutput.add(v2);
					}
				}
			}
			
			itrInt = i.iterator();
		}
	}
	
	/**
	 * Prints the vertices from the crown that are part of the
	 * vertex cover and the rest of the undirected graph minus
	 * the crown.
	 * @param c an output for the crown vertices in the vertex cover
	 * @param r an output for the graph minus the crown
	 */
	public void removeCrown(PrintStream c, PrintStream r) {
		pVertices.addAll(finalCrown.vertexSet());
		graph.removeAllVertices(pVertices);
		Iterator<Integer> itrInt = crownOutput.iterator();
		Integer io;
		while(itrInt.hasNext()) {
			io = itrInt.next();
			c.println(io);
		}
		
		int v1;
		int v2;
		Set<DefaultEdge> reduced = graph.edgeSet();
		Iterator<DefaultEdge> itr = reduced.iterator();
		DefaultEdge e;
		while(itr.hasNext()) {
			e = itr.next();
			v1 = subGraph.getEdgeSource(e);
			v2 = subGraph.getEdgeTarget(e);
			r.println(v1 + " " + v2);
		}
	}
}
