// Filename: Coin.java
// By: Andrew Holbrook
// Date: 11/14/2016

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Coin {
	public Matrix4 modelMatrix;
	public int vao;
	public int posVBO;
	public int uvVBO;
	public int texOBJ;
	public BufferedImage texImage;
	public Camera camera = null;
	
	public Coin() {
		this.modelMatrix = Matrix4.getIdentity();
		
		try {
			File f = new File("surf.png");
			this.texImage = ImageIO.read(f);
		} catch (Exception e) {
			System.err.println("NO");
		}
		
		this.init();
	}
	
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	
	public void update() {
		if (camera != null) {
			Vector4 camPos = camera.getPosition();
			Vector4 coinPos = this.modelMatrix.getPosition();
			Vector4 newNormal = camPos.sub(coinPos);
			if (newNormal.length() > 0) {
				newNormal.normalize();
				float angle = (float)Math.toDegrees(Math.atan2(
					newNormal.data[0], newNormal.data[2]));
				
				this.modelMatrix = Matrix4.getRotation(0, angle, 0).multiply(
					Matrix4.getTranslation(this.modelMatrix.getPosition()));
			}
		}
	}
	
	public void render() {
		glBindVertexArray(this.vao);
		glBindTexture(GL_TEXTURE_2D, this.texOBJ);
		
		ShaderProgram.getDefaultProgram().setModelMatrix(this.modelMatrix);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		glDisable(GL_BLEND);
		
		glBindVertexArray(0);
	}
	
	private void init() {
		this.vao = glGenVertexArrays();
		glBindVertexArray(this.vao);
		
		this.posVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, this.posVBO);
		
		float[] verts = {
			-1, 1, 0,
			-1, -1, 0,
			1, -1, 0,
			
			1, -1, 0,
			1, 1, 0,
			-1, 1, 0
		};
		
		glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		
		this.uvVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, this.uvVBO);
		
		float[] uvs = {
			0, 0,
			0, 1,
			32.0f / this.texImage.getWidth(), 1,
			
			32.0f / this.texImage.getWidth(), 1,
			32.0f / this.texImage.getWidth(), 0,
			0, 0
		};
		
		glBufferData(GL_ARRAY_BUFFER, uvs, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(1);
		
		glActiveTexture(GL_TEXTURE0);
		
		this.texOBJ = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, this.texOBJ);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.texImage.getWidth(),
			this.texImage.getHeight(), 0, GL_BGRA, GL_UNSIGNED_BYTE,
			this.texImage.getRGB(0, 0, this.texImage.getWidth(),
				this.texImage.getHeight(), null, 0, this.texImage.getWidth()));
		
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
			GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
			GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,
			GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,
			GL_REPEAT);
	}
}
