// Filename: Matrix4.java
// By: Andrew Holbrook
// Date: 9/19/2016 

public class Matrix4 {
	public float[][] data;

	public static Matrix4 getIdentity() {
		Matrix4 newMatrix = new Matrix4();

		newMatrix.data[0][0] = 1.0f;
		newMatrix.data[1][1] = 1.0f;
		newMatrix.data[2][2] = 1.0f;
		newMatrix.data[3][3] = 1.0f;
		
		return newMatrix;
	}

	public static Matrix4 getTranslation(float dx, float dy, float dz) {
		Matrix4 newMatrix = Matrix4.getIdentity();

		newMatrix.data[3][0] = dx;
		newMatrix.data[3][1] = dy;
		newMatrix.data[3][2] = dz;

		return newMatrix;
	}
	
	public static Matrix4 getScale(float sx, float sy, float sz) {
		Matrix4 newMatrix = Matrix4.getIdentity();
		
		newMatrix.data[0][0] = sx;
		newMatrix.data[1][1] = sy;
		newMatrix.data[2][2] = sz;
		
		return newMatrix;
	}
	
	public static Matrix4 getRotation(float ax, float ay, float az) {
		return Matrix4.getRotationX(ax).multiply(
			Matrix4.getRotationY(ay)).multiply(
				Matrix4.getRotationZ(az));
	}
	
	public static Matrix4 getRotationX(float angle) {
		Matrix4 newMatrix = Matrix4.getIdentity();
		
		float cosAngle = (float)Math.cos(Math.toRadians(angle));
		float sinAngle = (float)Math.sin(Math.toRadians(angle));
		
		// x and y components of new y-axis
		newMatrix.data[1][1] = cosAngle;
		newMatrix.data[1][2] = sinAngle;

		// x and y components of new z-axis
		newMatrix.data[2][1] = -sinAngle;
		newMatrix.data[2][2] = cosAngle;
		
		return newMatrix;
	}

	public static Matrix4 getRotationY(float angle) {
		Matrix4 newMatrix = Matrix4.getIdentity();

		float cosAngle = (float)Math.cos(Math.toRadians(angle));
		float sinAngle = (float)Math.sin(Math.toRadians(angle));
		
		// x and y components of new x-axis
		newMatrix.data[0][0] = cosAngle;
		newMatrix.data[0][2] = -sinAngle;

		// x and y components of new y-axis
		newMatrix.data[2][0] = sinAngle;
		newMatrix.data[2][2] = cosAngle;

		return newMatrix;
	}
	
	public static Matrix4 getRotationZ(float angle) {
		Matrix4 newMatrix = Matrix4.getIdentity();

		float cosAngle = (float)Math.cos(Math.toRadians(angle));
		float sinAngle = (float)Math.sin(Math.toRadians(angle));
		
		// x and y components of new x-axis
		newMatrix.data[0][0] = cosAngle;
		newMatrix.data[0][1] = sinAngle;

		// x and y components of new y-axis
		newMatrix.data[1][0] = -sinAngle;
		newMatrix.data[1][1] = cosAngle;

		return newMatrix;
	}

	public Matrix4() {
		this.init();
	}

	public String toString() {
		String tmpStr = "";
		for (int row = 0; row < 4; ++row) {
			for (int column = 0; column < 4; ++column) {
				tmpStr += this.data[row][column] + " ";
			}

			tmpStr += "\n";
		}

		return tmpStr;
	}
	
	public Matrix4 multiply(Matrix4 m) {
		Matrix4 newMatrix = new Matrix4();
		
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				for (int k = 0; k < 4; ++k) {
					newMatrix.data[i][j] += this.data[i][k] * m.data[k][j];
				}
			}
		}
		
		return newMatrix;
	}

	private void init() {
		this.data = new float[4][4];
		
		// initialize matrix to zero
		for (int row = 0; row < 4; ++row) {
			for (int column = 0; column < 4; ++column) {
				this.data[row][column] = 0.0f;
			}
		}
	}
}
