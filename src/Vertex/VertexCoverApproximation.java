package Vertex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

/**
 * VertexCoverApproximation is a class that runs two different
 * algorithms. Algorithm2 removes edges from the highest order
 * vertex until all edges are removed. Algorithm3 removes the
 * first edge it encounters and all edges associated with those
 * vertices.
 * 
 * @author Siddhartha
 *
 */
public class VertexCoverApproximation {
	
	ArrayList<Edge> edges = new ArrayList<Edge>();
	ArrayList<Integer> output = new ArrayList<Integer>();
	int size = 0;
	
	/**
	 * Main method that checks for two arguments and starts the
	 * algorithms.
	 * @param args array of command line inputs
	 */
	public static void main(String[] args) {
		long time = System.nanoTime();
		if (args.length != 2) {
			System.out.println("Usage: java VertexCoverApproximation <filename> [0|1]");
		} else {
			VertexCoverApproximation vCover = new VertexCoverApproximation();
			vCover.startUI(args[0], args[1]);
		}
		System.out.println("Runtime = " + (System.nanoTime() - time));
	}

	/**
	 * Runs through the methods to write the final
	 * output to file
	 */
	public void startUI(String file, String option) {
		Scanner input = getInputScanner(file);
		PrintStream output = getOutputPrintStream();
		processInput(input);
		if (option.equals("0")) {
			algorithm2();
		} else if (option.equals("1")) {
			algorithm3();
		} else {
			System.out.println("Usage: java VertexCoverApproximation <filename> [0|1]");
		}
		finalPrint(output);
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
		String name = "approxOutput.txt";

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
	 * This method returns the vertex that contains the most
	 * edges in the list.
	 * 
	 * @return vertex an integer that has the most edges
	 */
	public int hiVertex() {
		int[] tally = new int[size];
		int vertex = 0;
		Edge e;
		Iterator<Edge> itr = edges.iterator();
		while(itr.hasNext()) {
			e = itr.next();
			tally[e.getV1()]++;
			tally[e.getV2()]++;
		}
		for (int i = 0; i < tally.length; i++) {
			if (tally[vertex] < tally[i]) {
				vertex = i;
			}
		}
		return vertex;
	}
	
	/**
	 * This method removes all edge with the input vertex
	 * @param v the vertex which will be removed along with all
	 * associated edges
	 */
	public void removeVertex(int v) {
		Edge e;
		Iterator<Edge> itr1 = edges.iterator();
		while(itr1.hasNext()) {
			e = itr1.next();
			if (e.getV1() == v || e.getV2() == v) {
				itr1.remove();
			}
		}
	}
	
	/**
	 * This method finds the cover vertex list by removing
	 * the highest order edges.
	 */
	public void algorithm2() {
		int v;
		while(!edges.isEmpty()) {
			v = hiVertex();
			output.add(v);
			removeVertex(v);
		}
	}
	
	/**
	 * This method finds the cover vertex list by removing
	 * the first edge it sees and the edges associated with
	 * those vertices.
	 */
	public void algorithm3() {
		int v1;
		int v2;
		while(!edges.isEmpty()) {
			v1 = edges.get(0).getV1();
			v2 = edges.get(0).getV2();
			output.add(v1);
			output.add(v2);
			removeVertex(v1);
			removeVertex(v2);
		}
	}
	
	/**
	 * Prints the vertex cover list to a file.
	 * @param o PrintStream prints to file.
	 */
	public void finalPrint(PrintStream o) {
		Collections.sort(output);
		int v;
		Iterator<Integer> itr2 = output.iterator();
		while(itr2.hasNext()) {
			v = itr2.next();
			o.println(v);
		}
	}
}
