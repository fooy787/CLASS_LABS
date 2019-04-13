// Filename: ShaderProgram.java
// By: Andrew Holbrook
// Date: 9/19/2016

import java.io.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {
	
	private static final String DEFAULT_VERTEX_SHADER_SOURCE = "shader.vert";
	private static final String DEFAULT_FRAGMENT_SHADER_SOURCE = "shader.frag";
	private static ShaderProgram _defaultShaderProgram = null;
	
	private int _shaderProgram;
	private int _vertexShaderObject;
	private int _fragmentShaderObject;
	
	public static boolean initDefaultProgram() {
		_defaultShaderProgram = new ShaderProgram();
		
		if (!_defaultShaderProgram.setVertexShaderSource(
			DEFAULT_VERTEX_SHADER_SOURCE)) return false;
		
		if (!_defaultShaderProgram.setFragmentShaderSource(
			DEFAULT_FRAGMENT_SHADER_SOURCE)) return false;
		
		if (!_defaultShaderProgram.compileAndLink()) return false;
		
		return true;
	}
	
	public static ShaderProgram getDefaultProgram() {
		return _defaultShaderProgram;
	}
	
	public static void useDefaultProgram() {
		_defaultShaderProgram.use();
	}
	
	public static void cleanupDefaultProgram()
	{
		_defaultShaderProgram.cleanup();
	}
	
	public ShaderProgram() {
		_shaderProgram = glCreateProgram();
		_vertexShaderObject = glCreateShader(GL_VERTEX_SHADER);
		_fragmentShaderObject = glCreateShader(GL_FRAGMENT_SHADER);
	}
	
	public int getGLProgram() {
		return _shaderProgram;
	}
	
	public boolean setVertexShaderSource(String sourceFilename) {
		return this.setShaderSource(_vertexShaderObject, sourceFilename);
	}
	
	public boolean setFragmentShaderSource(String sourceFilename) {
		return this.setShaderSource(_fragmentShaderObject, sourceFilename);
	}
	
	public boolean setModelMatrix(Matrix4 m) {
		float[] flat_data = new float[16];
		int k = 0;
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				flat_data[k] = m.data[j][i];
				k++;
			}
		}
		
		return this.setModelMatrix(flat_data);
	}
	
	public boolean setModelMatrix(float[] value) {
		int modelMatrixLocation = glGetUniformLocation(
			_shaderProgram,
			"ModelMatrix");
		
		if (modelMatrixLocation == -1)
			return false;
		
		glUniformMatrix4fv(modelMatrixLocation, true, value);
		return true;
	}
	
	public void use() {
		glUseProgram(_shaderProgram);
	}
	
	public boolean compileAndLink() {
		
		// compile vertex shader
		if (!this.compileShader(_vertexShaderObject)) {
			System.err.println(glGetShaderInfoLog(_vertexShaderObject));
			return false;
		}
		
		// compile fragment shader
		if (!this.compileShader(_fragmentShaderObject)) {
			System.err.println(glGetShaderInfoLog(_fragmentShaderObject));
			return false;
		}
		
		// attach shader objects to program
		glAttachShader(_shaderProgram, _vertexShaderObject);
		glAttachShader(_shaderProgram, _fragmentShaderObject);
		
		// link
		if (!this.linkProgram(_shaderProgram)) {
			System.err.println(glGetProgramInfoLog(_shaderProgram));
			return false;
		}
		
		return true;
	}
	
	public void cleanup() {
		glUseProgram(0); // ensure that no program is currently in use
		glDeleteShader(_vertexShaderObject);
		glDeleteShader(_fragmentShaderObject);
		glDeleteProgram(_shaderProgram);
	}
	
	private boolean linkProgram(int shaderProgram) {
		glLinkProgram(shaderProgram);
		
		int[] result = new int[]{GL_TRUE};
		glGetProgramiv(_shaderProgram, GL_LINK_STATUS, result);
		return result[0] == GL_TRUE ? true : false;
	}
	
	private boolean setShaderSource(int shaderObject, String sourceFilename) {
		String sourceString = this.fileToString(sourceFilename);
		if (sourceString == null)
			return false;
		
		glShaderSource(shaderObject, sourceString);
		return true;
	}
	
	private boolean compileShader(int shaderObject) {
		glCompileShader(shaderObject);
		
		int[] result = new int[]{GL_TRUE};
		glGetShaderiv(shaderObject, GL_COMPILE_STATUS, result);
		return result[0] == GL_TRUE ? true : false;
	}
	
	// reads a file and returns its contents as a String
	private String fileToString(String filename) {
		try {
			File file = new File(filename);
			FileInputStream fileInputStream = new FileInputStream(file);
			
			byte[] buffer = new byte[(int)file.length()];
			fileInputStream.read(buffer);
			fileInputStream.close();
			
			return new String(buffer);
			
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
}
