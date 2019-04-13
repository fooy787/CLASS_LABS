//Chase Williams
//Lab 2 part 2
import java.util.Random;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Model3D {
	public static void main(String[] args) {
		GLWindow window = GLWindow.getInstance(600, 600);
		// turn on depth testing
		glEnable(GL_DEPTH_TEST);
		
		glClearColor(0.25f, 0.25f, 1.0f, 0.0f);
		
		float[] verts = OBJReader.readVerticesFromOBJ("monkey.obj");
		
		// Setup vertex array object for storing vertex buffer object
		// binding points, which array attributes are enabled, etc.
		int vertexArrayObject = glGenVertexArrays();
		glBindVertexArray(vertexArrayObject);
		
		// Create a new vertex buffer object and check if it
		// was created without error.
		int positionVBO = glGenBuffers();
		if (positionVBO == -1) {
			System.err.println("ERROR HAPPENED, VBO NOT CREATED");
		}
		
		// Bind VBO to ARRAY_BUFFER target and copy our vertex
		// position data.
		glBindBuffer(GL_ARRAY_BUFFER, positionVBO);
		glBufferData(GL_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
		
		// point our position VBO to attribute array location 0
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		// enable vertex attribute array location 0
		glEnableVertexAttribArray(0);
		
		// setup color vertex buffer object
		int colorVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, colorVBO);
		
		// random color values
		Random r = new Random();
		float[] colorValues = new float[verts.length];
		for (int i = 0; i < verts.length; ++i)
			colorValues[i] = r.nextFloat();
		
		// bind and copy color data
		glBufferData(GL_ARRAY_BUFFER, colorValues, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		
		// enable location 1 (color data)
		glEnableVertexAttribArray(1);
		
		// setup index vbo and copy data
		int indexVBO = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVBO);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, verts, GL_STATIC_DRAW);
		
		// monkey's angle of rotation about the z-axis
		float angleZ = 0.0f;
		float angleY = 0.0f;
		float angleX = 0.0f;
		float angularVelocity = 360.0f / 1000.0f; // 360 degrees in 1 second
		float curPosX = 0.0f;
		float curPosY = 0.0f;
		float directionalVelocity = 30.0f /10000.0f; //30 Pixels per second
		
		long startTime = System.currentTimeMillis();
		
		// main loop
		while (!window.shouldClose()) {
			
			long stopTime = System.currentTimeMillis();
			long elapsedTime = stopTime - startTime;
			startTime = stopTime;
			
			// handle input
			if (window.isPressed(GLFW_KEY_Q)) {
				window.close();
			}
			//Angle Z
			if (window.isLeftMouseButtonPressed()) {
				angleZ += angularVelocity * elapsedTime;
				if (angleZ >= 360.0f)
					angleZ -= 360.0f;
			}
			if (window.isRightMouseButtonPressed()){
				angleZ -= angularVelocity * elapsedTime;
				if (angleZ <= 0.0f)
					angleZ += 360.0f;
			}
			//Angle Y
			if (window.isPressed(GLFW_KEY_A)) {
				angleY += angularVelocity * elapsedTime;
				if (angleY >= 360.0f)
					angleY -= 360.0f;
			}
			if (window.isPressed(GLFW_KEY_D)){
				angleY -= angularVelocity * elapsedTime;
				if (angleY <= 0.0f)
					angleY += 360.0f;
			}
			//Angle X
			if (window.isPressed(GLFW_KEY_W)) {
				angleX -= angularVelocity * elapsedTime;
				if (angleX >= 360.0f)
					angleX -= 360.0f;
			}
			if (window.isPressed(GLFW_KEY_S)){
				angleX += angularVelocity * elapsedTime;
				if (angleX <= 0.0f)
					angleX += 360.0f;
			}
			//Moving on X
			if (window.isPressed(GLFW_KEY_RIGHT)) {
				curPosX += directionalVelocity * elapsedTime;
			}
			if (window.isPressed(GLFW_KEY_LEFT)){
				curPosX -= directionalVelocity * elapsedTime;
			}
			//moving on Y
			if (window.isPressed(GLFW_KEY_DOWN)) {
				curPosY -= directionalVelocity * elapsedTime;
			}
			if (window.isPressed(GLFW_KEY_UP)){
				curPosY += directionalVelocity * elapsedTime;
			}			
			// update objects
			
			// clear screen
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			// render
			
			// bind VAO to render the monkey
			glBindVertexArray(vertexArrayObject);
			
			// set the model matrix to rotate and then translate
			// the model
			ShaderProgram.getDefaultProgram().setModelMatrix(
				Matrix4.getRotation(angleX, angleY, angleZ).multiply(
					Matrix4.getTranslation(curPosX, curPosY, 0.0f)));
			
			// draw monkey
			glDrawArrays(GL_TRIANGLES, 0, verts.length / 3);
			
			window.swapBuffers();
			window.pollEvents();
		}
	}
}