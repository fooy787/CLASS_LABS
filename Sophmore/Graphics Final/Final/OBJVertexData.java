// Filename: OBJVertexData.java
// By: Andrew Holbrook
// Date: 9/26/2016

public class OBJVertexData {
	public float[] vertices;
	public int[] indices;
	public float[] textureCoordinates;
	
	public OBJVertexData() {
		this.vertices = new float[0];
		this.indices = new int[0];
		this.textureCoordinates = new float[0];
	}
}
