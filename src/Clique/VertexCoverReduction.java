package Clique;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import Vertex.Edge;

/**
 * VertexCoverReduction is a class that runs a graph file
 * to check if it has a vertex cover of size k. It
 * reduces the problem to finding a clique of size k
 * to verify if the answer exists. 
 * 
 * @author Siddhartha
 *
 */
public class VertexCoverReduction {

	ArrayList<Edge> edges = new ArrayList<Edge>();
	ArrayList<Edge> invertedEdges = new ArrayList<Edge>();
	ArrayList<Integer> vertexList = new ArrayList<Integer>();
	int size = 0;
	
	public static void main(String[] args) {
		long time = System.nanoTime();
		if (args.length != 2) {
			System.out.println("Usage: java VertexCoverApproximation <graph> [k]");
		} else {
			VertexCoverReduction reduc = new VertexCoverReduction();
			reduc.startUI(args[0], args[1]);
		}
		System.out.println("Runtime = " + (System.nanoTime() - time));
	}
	
	/**
	 * Runs through the methods to write the final
	 * output to file
	 * 
	 * @param file the input file
	 * @param option the size of the vertex cover, k
	 */
	public void startUI(String file, String option) {
		Scanner input = getInputScanner(file);
		PrintStream output = getOutputPrintStream();
		processInput(input);
		countVertex();
		completeGraph();
		complementGraph();
		finalPrint(output);
		//int k = Integer.parseInt(option);
		//CliqueDecision.decide("invertedGraph.txt", k);
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
	public PrintStream getOutputPrintStream() {
		PrintStream output = null;
		String name = "edgeListFilename.txt";

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
		while (input.hasNextLine()) {
			v1 = input.nextInt();
			v2 = input.nextInt();
			input.nextLine().trim();
			Edge e = new Edge(v1, v2);
			edges.add(e);
			if (size < v1) {
				size = v1;
			}
			if (size < v2) {
				size = v2;
			}
		}
		size++;
	}
	
	/**
	 * This method creates an array list of all of the vertices
	 * in the graph
	 */
	public void countVertex() {
		int[] tally = new int[size];
		Edge e;
		Iterator<Edge> itr = edges.iterator();
		while (itr.hasNext()) {
			e = itr.next();
			tally[e.getV1()] = 1;
			tally[e.getV2()] = 1;
		}
		for (int i = 0; i < tally.length; i++) {
			if (tally[i] != 0) {
				vertexList.add(i);
			}
		}
	}
	
	/**
	 * Creates the complete graph where all of the vertices
	 * are connected to each other
	 */
	public void completeGraph() {
		for (int i = 0; i < vertexList.size(); i++) {
			for (int j = i + 1; j < vertexList.size(); j++) {
				Edge e = new Edge(i, j);
				invertedEdges.add(e);
			}
		}
	}
	
	/**
	 * Compares two edges to see if they are equal
	 * @param e1 an edge to compare
	 * @param e2 an edge to compare
	 * @return true if equal, otherwise false
	 */
	public boolean edgeCompare(Edge e1, Edge e2) {
		if (e1.getV1() != e2.getV1()) {
			return false;
		}
		if (e1.getV2() != e2.getV2()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Creates the complement graph to the original input graph
	 */
	public void complementGraph() {
		Edge e;
		Iterator<Edge> itr = invertedEdges.iterator();
		while (itr.hasNext()) {
			e = itr.next();
			for (int i = 0; i < edges.size(); i++) {
				if (edgeCompare(e, edges.get(i))) {
					itr.remove();
				}
			}
		}
	}
	
	/**
	 * Prints the inverted graph edges to a file.
	 * @param o PrintStream prints to file.
	 */
	public void finalPrint(PrintStream o) {
		Edge e;
		Iterator<Edge> itr = invertedEdges.iterator();
		while(itr.hasNext()) {
			e = itr.next();
			o.println(e.getV1() + " " + e.getV2());
		}
	}
}
