package Vertex;

/**
 * Edge class is a data object that stores two vertices
 * in it. Always places the lower vertex in the first slot.
 * 
 * @author Siddhartha
 *
 */
public class Edge {
	
	private int v1;
	private int v2;
	private boolean flag;
	
	/**
	 * This method makes a data object Edge that
	 * hold two fields v1, and v2 for vertexes.
	 * @param v1 vertex 1
	 * @param v2 vertex 2
	 */
	public Edge(int v1, int v2) {
		if (v1 > v2) {
			int n;
			n = v2;
			v2 = v1;
			v1 = n;
		}
		
		this.v1 = v1;
		this.v2 = v2;
		flag = false;
	}
	
	/**
	 * Returns vertex 1
	 * @return v1 vertex 1
	 */
	public int getV1() {
		return v1;
	}
	
	/**
	 * Returns vertex 2
	 * @return v2 vertex 2
	 */
	public int getV2() {
		return v2;
	}
	
	/**
	 * Returns the flag status
	 * @return flag true if flag is set, otherwise false
	 */
	public boolean getFlag() {
		return flag;
	}
	
	/**
	 * Sets the flag to true or false
	 * @param f true if flag needs to be set, otherwise false
	 */
	public void setFlag(boolean f) {
		flag = f;
	}
}
