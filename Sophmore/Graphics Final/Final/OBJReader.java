// Filename: OBJReader.java
// By: Andrew Holbrook
// Date: 9/26/2016

import java.io.*;

import java.util.ArrayList;
import java.nio.FloatBuffer;

public class OBJReader {
	
	public static OBJVertexData readFromOBJ(String filename) {
		return OBJReader.readFromOBJ(filename, false);
	}
	
	// readFromOBJ will read all vertex and index data from an obj file
	public static OBJVertexData readFromOBJ(String filename, boolean flatten) {
		ArrayList<Float> vertexList = new ArrayList<Float>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		ArrayList<Float> uvList = new ArrayList<Float>();
		ArrayList<Integer> uvIndexList = new ArrayList<Integer>();
		
		try {
			BufferedReader reader = new BufferedReader(
				new FileReader(filename));
			
			String line = reader.readLine();
			while (line != null) {
				String[] tokens = line.split(" ");
				if (tokens[0].equals("v")) {
					vertexList.add(Float.valueOf(tokens[1]));
					vertexList.add(Float.valueOf(tokens[2]));
					vertexList.add(Float.valueOf(tokens[3]));
				} else if (tokens[0].equals("f")) {
					if (tokens.length != 4) {
						reader.close();
						System.err.println("Error reading model : " +
							"only triangulated meshes are supported");
						return null;
					}
					
					if (tokens[1].contains("/")) {
						for (int i = 1; i <= 3; ++i) {
							String[] indices = tokens[i].split("/");
							indexList.add(Integer.valueOf(indices[0]) - 1);
							uvIndexList.add(Integer.valueOf(indices[1]) - 1);
						}
					} else {
						indexList.add(Integer.valueOf(tokens[1]) - 1);
						indexList.add(Integer.valueOf(tokens[2]) - 1);
						indexList.add(Integer.valueOf(tokens[3]) - 1);
					}
					
				} else if (tokens[0].equals("vt")) {
					uvList.add(Float.valueOf(tokens[1]));
					uvList.add(Float.valueOf(tokens[2]));
				}
				
				line = reader.readLine();
			}
			
			reader.close();
			
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		
		OBJVertexData vertexData = new OBJVertexData();
		
		if (flatten) {
			int i = 0;
			vertexData.vertices = new float[indexList.size() * 3];
			for (Integer index : indexList) {
				vertexData.vertices[i] = vertexList.get(index * 3);
				vertexData.vertices[i + 1] = vertexList.get(index * 3 + 1);
				vertexData.vertices[i + 2] = vertexList.get(index * 3 + 2);
				i += 3;
			}
			
			i = 0;
			vertexData.textureCoordinates = new float[uvIndexList.size() * 2];
			for (Integer index : uvIndexList) {
				vertexData.textureCoordinates[i] = uvList.get(index * 2);
				vertexData.textureCoordinates[i + 1] = uvList.get(index * 2 + 1);
				i += 2;
			}
			
		} else {
			vertexData.vertices = new float[vertexList.size()];
			vertexData.indices = new int[indexList.size()];
			vertexData.textureCoordinates = new float[uvList.size()];
			
			// copy data into the OBJVertexData object
			for (int i = 0; i < vertexList.size(); ++i)
				vertexData.vertices[i] = vertexList.get(i);
			for (int i = 0; i < indexList.size(); ++i)
				vertexData.indices[i] = indexList.get(i);
			for (int i = 0; i < uvList.size(); ++i)
				vertexData.textureCoordinates[i] = uvList.get(i);
		}
		
		return vertexData;
	}
	
	// readVerticesFromOBJ will read all vertex data from an obj file,
	// flattening the data into an array
	public static float[] readVerticesFromOBJ(String filename) {
		ArrayList<Float> vertexList = new ArrayList<Float>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		
		try {
			BufferedReader reader = new BufferedReader(
				new FileReader(filename));
			
			String line = reader.readLine();
			while (line != null) {
				String[] tokens = line.split(" ");
				if (tokens[0].equals("v")) {
					vertexList.add(Float.valueOf(tokens[1]));
					vertexList.add(Float.valueOf(tokens[2]));
					vertexList.add(Float.valueOf(tokens[3]));
				} else if (tokens[0].equals("f")) {
					if (tokens.length != 4) {
						reader.close();
						System.err.println("Error reading model : " +
							"only triangulated meshes are supported");
						return null;
					}
					
					indexList.add(Integer.valueOf(tokens[1]) - 1);
					indexList.add(Integer.valueOf(tokens[2]) - 1);
					indexList.add(Integer.valueOf(tokens[3]) - 1);
				}
				
				line = reader.readLine();
			}
			
			reader.close();
			
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
		
		// flatten data for non-indexed rendering
		int i = 0;
		float[] vertices = new float[indexList.size() * 3];
		for (Integer index : indexList) {
			vertices[i] = vertexList.get(index * 3);
			vertices[i + 1] = vertexList.get(index * 3 + 1);
			vertices[i + 2] = vertexList.get(index * 3 + 2);
			i += 3;
		}
		
		return vertices;
	}
}
