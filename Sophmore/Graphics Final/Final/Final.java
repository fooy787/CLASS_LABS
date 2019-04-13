//Graphics 2801 Final lab
//Chase Williams

import java.util.Random;
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

public class Final {
	
	public static double returnDistance(Vector4 pos1, Vector4 pos2){
		return Math.sqrt((pos2.data[0] - pos1.data[0])*(pos2.data[0] - pos1.data[0]) + (pos2.data[2] - pos1.data[2])*(pos2.data[2] - pos1.data[2]));
	}
	
	public static void main(String[] args) {
		
		BufferedImage chestImg = null;
		try {
			File f = new File("treasure_chest/treasure_chest.png");
			chestImg = ImageIO.read(f);
			
		} catch (Exception e) {
			System.err.println("NO");
		}
		
		GLWindow window = GLWindow.getInstance(800, 600);
		
		glActiveTexture(GL_TEXTURE0);
		
		int texobj = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texobj);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, chestImg.getWidth(),
			chestImg.getHeight(), 0, GL_BGRA, GL_UNSIGNED_BYTE,
			chestImg.getRGB(0, 0, chestImg.getWidth(), chestImg.getHeight(),
				null, 0, chestImg.getWidth()));
		
		glGenerateMipmap(GL_TEXTURE_2D);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
			GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
			GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S,
			GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T,
			GL_REPEAT);
		
		// turn on depth testing
		glEnable(GL_DEPTH_TEST);
		
		// turn on backface culling
		glEnable(GL_CULL_FACE);
		
		// set background color
		glClearColor(0.25f, 0.25f, 1.0f, 0.0f);
		
		ShaderProgram.getDefaultProgram().setProjectionMatrix(
			Matrix4.getPerspective(60, window.getAspect(), 1, 10));
		
		long startTime = System.currentTimeMillis();
		
		int vao = glGenVertexArrays();
		glBindVertexArray(vao);
		
		// load vertex data for non-indexed rendering
		OBJVertexData data = OBJReader.readFromOBJ(
			"treasure_chest/treasure_chest.obj", true);
		
		int positionVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, positionVBO);
		
		glBufferData(GL_ARRAY_BUFFER, data.vertices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		
		int uvVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, uvVBO);
		
		glBufferData(GL_ARRAY_BUFFER, data.textureCoordinates, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(1);
		
		float[] floorVerts = new float[] {-10.0f, -3.0f -10.0f, -10.0f, -3.0f, 10.0f, 10.0f, -3.0f, -10.0f, 10.0f, -3.0f, 10.0f, 10.0f, -3.0f, -10.0f, -10.0f, -3.0f, 10.0f};
		
		int floorVAO = glGenVertexArrays();
		glBindVertexArray(floorVAO);
		int floorVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, floorVBO);
		glBufferData(GL_ARRAY_BUFFER, floorVerts, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		
		
		float angle = 0.0f;
		
		Camera camera = new Camera();
		float dpos = 1.0f / 500.0f;
		float camYRot = 0.0f;
		Vector4 camPosition = camera.getPosition();
		Vector4[] colorRed = new Vector4[]{new Vector4(1, 0, 0, 1),
			new Vector4(1, 0, 0, 1),
			new Vector4(1, 0, 0, 1)};
		
		Arm arm = new Arm();
		
		Arm robotOne = new Arm();
		robotOne.setColor(colorRed);
		
		Arm robotTwo = new Arm();
		robotTwo.setColor(colorRed);
		
		ShaderProgram colorOnlyShader = new ShaderProgram();
		colorOnlyShader.setVertexShaderSource("colorshader.vert");
		colorOnlyShader.setFragmentShaderSource("colorshader.frag");
		if (!colorOnlyShader.compileAndLink()) {
			System.exit(0);
		}
		
		arm.program = colorOnlyShader;
		robotOne.program = colorOnlyShader;
		robotTwo.program = colorOnlyShader;
		Vector4 chestPos = new Vector4(0, -3, -5, 1);

		robotOne.setPosition(new Vector4(-7, -3, -7, 1));
		robotTwo.setPosition(new Vector4(8, -3, -9, 1));
		
		double distancetoRobot1;
		double distancetoRobot2;
		double distancetoChest;
		
		int curRobots = 2;

		
		// main loop
		while (!window.shouldClose()) {
			
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			startTime = stopTime;
			
			// handle input
			if (window.isPressed(GLFW_KEY_ESCAPE)) {
				window.close();
			}
			
			if (window.isPressed(GLFW_KEY_A)) {
				Vector4 camPos = camera.getPosition();
				Vector4 camDir = camera.getLeft();
				camDir = camDir.multiply(dpos * elapsedTime);
				camera.setPosition(camPos.add(camDir));
			}
			
			if (window.isPressed(GLFW_KEY_D)) {
				Vector4 camPos = camera.getPosition();
				Vector4 camDir = camera.getRight();
				camDir = camDir.multiply(dpos * elapsedTime);
				camera.setPosition(camPos.add(camDir));
			}
			
			if (window.isPressed(GLFW_KEY_W)) {
				Vector4 camPos = camera.getPosition();
				Vector4 camDir = camera.getForward();
				camDir = camDir.multiply(dpos * elapsedTime);
				camera.setPosition(camPos.add(camDir));
			}
			
			if (window.isPressed(GLFW_KEY_S)) {
				Vector4 camPos = camera.getPosition();
				Vector4 camDir = camera.getBackward();
				camDir = camDir.multiply(dpos * elapsedTime);
				camera.setPosition(camPos.add(camDir));
			}
			
			
			if (window.isPressed(GLFW_KEY_LEFT)) {
				camYRot += (360.0f / 2000.0f) * elapsedTime;
				while (camYRot >= 360) camYRot -= 360;
				
				camera.setBasis(Matrix4.getRotation(0, camYRot, 0));
				arm.setOrientation(camYRot);
			}
			
			if (window.isPressed(GLFW_KEY_RIGHT)) {
				camYRot -= (360.0f / 2000.0f) * elapsedTime;
				while (camYRot < 0) camYRot += 360;
				
				camera.setBasis(Matrix4.getRotation(0, camYRot, 0));
				arm.setOrientation(camYRot);
			}
			
			if (window.isPressed(GLFW_KEY_SPACE)) {
				arm.setPunch(true);
			} else {
				arm.setPunch(false);
			}
			
			// update objects
			Vector4 armPosition = new Vector4(camPosition.getX(), camPosition.getY() - 4, camPosition.getZ() - 1, 0.0f);
			camPosition = camera.getPosition();
			
			arm.setPosition(armPosition);
			arm.update(elapsedTime);
			
			distancetoChest = returnDistance(armPosition, chestPos);
			distancetoRobot1 = returnDistance(armPosition, robotOne.position);
			distancetoRobot2 = returnDistance(armPosition, robotTwo.position);
			
			if(arm.punching && distancetoRobot1 <= 3.75f){
				robotOne.setPosition(new Vector4(-100.0f, -100.0f, -100.0f, 1));
				curRobots--;
			}
			
			if(arm.punching && distancetoRobot2 <= 3.75f){
				robotTwo.setPosition(new Vector4(-100.0f, 1100.0f, -100.0f, 1));
				curRobots--;
			}
			
			if(arm.punching && curRobots == 0 && distancetoChest <= 2.5f){
				window.close();
			}
			
			// clear buffers
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			// render
			
			arm.program.use();
			arm.program.setViewMatrix(camera.getViewMatrix());
			arm.program.setProjectionMatrix(
				Matrix4.getPerspective(60, window.getAspect(), 1, 10));
			arm.render();
			
			robotOne.program.use();
			robotOne.program.setViewMatrix(camera.getViewMatrix());
			robotOne.program.setProjectionMatrix(
				Matrix4.getPerspective(60, window.getAspect(), 1, 10));
			robotOne.render();
			
			robotTwo.program.use();
			robotTwo.program.setViewMatrix(camera.getViewMatrix());
			robotTwo.program.setProjectionMatrix(
				Matrix4.getPerspective(60, window.getAspect(), 1, 10));
			robotTwo.render();
			
			ShaderProgram.useDefaultProgram();
			
			glBindVertexArray(vao);
			glBindTexture(GL_TEXTURE_2D, texobj);
			
			
			ShaderProgram.getDefaultProgram().setViewMatrix(
				camera.getViewMatrix());
			
			ShaderProgram.getDefaultProgram().setModelMatrix(
				Matrix4.getRotation(0, angle, 0).multiply(
					Matrix4.getTranslation(chestPos)));
			
			glDrawArrays(GL_TRIANGLES, 0, data.vertices.length / 3);
			
			glBindTexture(GL_TEXTURE_2D, texobj);
			
			glBindVertexArray(floorVAO);
			ShaderProgram.getDefaultProgram().setModelMatrix(
				Matrix4.getIdentity());
			glDrawArrays(GL_TRIANGLES, 0, floorVerts.length / 3);
			
			// swap buffers and poll for new events
			window.swapBuffers();
			window.pollEvents();
		}
	}
}