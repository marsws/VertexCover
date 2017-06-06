package Clique;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import Vertex.Edge;

/**
 * VertexCoverVerification is a class that runs two different
 * files, one that contains the graph and another that contains
 * candidate solutions. The program then checks the candidate
 * solutions to see if they are a vertex cover of size k.
 * 
 * @author Siddhartha
 *
 */
public class VertexCoverVerification {

	ArrayList<Edge> edges = new ArrayList<Edge>();
	ArrayList<Integer> vertexList = new ArrayList<Integer>();
	String verificationMessage = "yes";
	
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Usage: java VertexCoverApproximation <graph> <candidate solution> [k]");
		} else {
			VertexCoverVerification ver = new VertexCoverVerification();
			ver.startUI(args[0], args[1], args[2]);
		}
	}
	
	/**
	 * Runs through the methods to verify solutions
	 * output to file
	 * 
	 * @param graph the graph file
	 * @param answer the candidate solution file
	 * @param option the size of the vertex cover, k
	 */
	public void startUI(String graph, String answer, String option) {
		Scanner input = getInputScanner(graph);
		Scanner ans = getInputScanner(answer);
		processInput(input);
		int k = Integer.parseInt(option);
		
		while (ans.hasNextLine()) {
			Scanner line = new Scanner(ans.nextLine());
			vertexList = processAns(line);
			if (k != vertexList.size()) {
				verificationMessage = "no";
			} else {
				verifyAnswer();
			}
			System.out.println(verificationMessage);
			verificationMessage = "yes";
		}
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
		}
	}
	
	/**
	 * Processes the answer line into an array list
	 * @param line Scanner that contains a line of input
	 * @return ans an array list of vertices
	 */
	public ArrayList<Integer> processAns(Scanner line) {
		ArrayList<Integer> ans = new ArrayList<Integer>();
		
		while (line.hasNextInt()) {
			ans.add(line.nextInt());
		}
		return ans;
	}
	
	/**
	 * Verifies if the candidate solution is correct
	 */
	public void verifyAnswer() {
		for (int i = 0; i < edges.size(); i++) {
			for (int j = 0; j < vertexList.size(); j++) {
				if (edges.get(i).getV1() == vertexList.get(j) ||
					edges.get(i).getV2() == vertexList.get(j)) {
					
					edges.get(i).setFlag(true);
				}
			}
		}
		
		for (int i = 0; i < edges.size(); i++) {
			if (!edges.get(i).getFlag()) {
				verificationMessage = "no";
			} else {
				edges.get(i).setFlag(false);
			}
		}
	}
}
