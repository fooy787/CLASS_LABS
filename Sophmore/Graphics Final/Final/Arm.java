import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Arm {
	public int vertexArrayObject;
	public int positionVBO;
	public int bodyVAO;
	public int bodyPositionVBO;
	public float L0;
	public float L1;
	public float L2;
	public float speed = 1.0f / 1000.0f;
	public Vector4 position;
	public Vector4 velocity;
	public Vector4 orientation;
	public float shoulderAngle;
	public float elbowAngle;
	public float[] vertices;
	public float[] bodyVertices;
	public Vector4[] colors;
	float deltaAngle = 360.0f / 250.0f;
	boolean punching = false;
	public ShaderProgram program = null;
	public float oriY = 90.0f;
	
	public Arm() {
		vertexArrayObject = glGenVertexArrays();
		glBindVertexArray(vertexArrayObject);
		
		vertices = OBJReader.readVerticesFromOBJ("arm.obj");
		
		positionVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, positionVBO);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glEnableVertexAttribArray(0);
		
		L0 = 4f;
		L1 = 1.4f;
		L2 = 0f;
		shoulderAngle = -90.0f;
		elbowAngle = 90.0f;
		position = new Vector4(-3f, -3.0f, -5.0f, 1);
		velocity = new Vector4();
		orientation = new Vector4(0, oriY, 0, 0);
		
		colors = new Vector4[] {
			new Vector4(0, 0, 1, 1),
			new Vector4(0, 0, 1, 1),
			new Vector4(0, 0, 1, 1)
		};
		
		bodyVAO = glGenVertexArrays();
		glBindVertexArray(bodyVAO);
		
		bodyVertices = OBJReader.readVerticesFromOBJ("body.obj");
		bodyPositionVBO = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, bodyPositionVBO);
		glBufferData(GL_ARRAY_BUFFER, bodyVertices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		glEnableVertexAttribArray(0);
		
		glBindVertexArray(0);
	}
	
	public void setColor(Vector4[] newColors){
		colors = newColors;
	}
	
	public void setOrientation(float newY){
		oriY += newY;
	}
	
	public void setPunch(boolean punch) {
		punching = punch;
	}
	
	public void setPosition(Vector4 cPosition){
		position = cPosition;
	}
	
	public void update(float elapsedTime) {
		if (punching) {
			shoulderAngle += deltaAngle * elapsedTime;
			if (shoulderAngle > 0) shoulderAngle = 0.0f;
			elbowAngle -= deltaAngle * elapsedTime;
			if (elbowAngle < 0) elbowAngle = 0.0f;
		} else {
			shoulderAngle -= deltaAngle * elapsedTime;
			if (shoulderAngle < -90) shoulderAngle = -90.0f;
			elbowAngle += deltaAngle * elapsedTime;
			if (elbowAngle > 90) elbowAngle = 90.0f;
		}
		
		this.position = this.position.add(this.velocity.mul(elapsedTime));
	}
	
	public void render() {
		glBindVertexArray(vertexArrayObject);
		
		Matrix4 Tworld_robot = Matrix4.getRotation(orientation).multiply(
			Matrix4.getTranslation(position));
		
		Matrix4 Trobot_shoulder = Matrix4.getRotation(
			0, 0, shoulderAngle).multiply(Matrix4.getTranslation(0, L0, 1.31105f));
		
		Matrix4 Tshoulder_elbow = Matrix4.getRotation(
			0, 0, elbowAngle).multiply(Matrix4.getTranslation(L1, 0, 0));
		
		Matrix4 Tworld_shoulder = Trobot_shoulder.multiply(Tworld_robot);
		Matrix4 Tworld_elbow = Tshoulder_elbow.multiply(Tworld_shoulder);
		
		program.setModelMatrix(
			Tworld_shoulder);
		
		program.setVector4(
			colors[0], "inColor");
		
		glDrawArrays(GL_TRIANGLES, 0, vertices.length / 3);
		
		program.setVector4(
			colors[1], "inColor");
		
		program.setModelMatrix(
			Tworld_elbow);
		
		glDrawArrays(GL_TRIANGLES, 0, vertices.length / 3);
		
		glBindVertexArray(bodyVAO);
		
		program.setVector4(
			colors[2], "inColor");
		
		program.setModelMatrix(Tworld_robot);
		
		glDrawArrays(GL_TRIANGLES, 0, bodyVertices.length / 3);
		
		glBindVertexArray(0);
	}
}
