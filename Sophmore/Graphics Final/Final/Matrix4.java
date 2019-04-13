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
	
	public static Matrix4 getOrthographic(float left, float right,
										  float top, float bottom,
										  float near, float far) {
		Matrix4 newMatrix = new Matrix4();
		newMatrix.data[0][0] = 2.0f / (right - left);
		newMatrix.data[3][0] = -(right + left) / (right - left);
		
		newMatrix.data[1][1] = 2.0f / (top - bottom);
		newMatrix.data[3][1] = -(top + bottom) / (top - bottom);
		
		newMatrix.data[2][2] = -2.0f / (far - near);
		newMatrix.data[3][2] = -(far + near) / (far - near);
		
		newMatrix.data[3][3] = 1.0f;
		
		return newMatrix;
	}
	
	public static Matrix4 getPerspective(float fovy, float aspect, float near,
										 float far) {
		Matrix4 newMatrix = new Matrix4();
		
		newMatrix.data[0][0] = 1.0f / (
			aspect * (float)Math.tan(Math.toRadians(fovy / 2)));
		
		newMatrix.data[1][1] = 1.0f / (float)Math.tan(Math.toRadians(fovy / 2));
		
		newMatrix.data[2][2] = -(far + near) / (far - near);	
		newMatrix.data[3][2] = -(2 * far * near) / (far - near);
		
		newMatrix.data[2][3] = -1.0f;
		
		return newMatrix;
	}
	
	public static Matrix4 getTranslation(Vector4 v) {
		return Matrix4.getTranslation(v.data[0], v.data[1], v.data[2]);
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
	
	public static Matrix4 getRotation(Vector4 v) {
		return Matrix4.getRotation(v.data[0], v.data[1], v.data[2]);
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
	
	public void setRow(int row, Vector4 v) {
		this.data[row][0] = v.data[0];
		this.data[row][1] = v.data[1];
		this.data[row][2] = v.data[2];
		this.data[row][3] = v.data[3];
	}
	
	public void setPosition(Vector4 pos) {
		this.data[3][0] = pos.data[0];
		this.data[3][1] = pos.data[1];
		this.data[3][2] = pos.data[2];
	}
	
	public void setBasis(Matrix4 basis) {
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 3; ++col) {
				this.data[row][col] = basis.data[row][col];
			}
		}
	}
	
	public Matrix4 getTranspose() {
		Matrix4 newMatrix = new Matrix4();
		
		for (int row = 0; row < 4; ++row) {
			for (int col = 0; col < 4; ++col) {
				newMatrix.data[col][row] = this.data[row][col];
			}
		}
		
		return newMatrix;
	}
	
	public Vector4 getPosition() {
		Vector4 newVector = new Vector4();
		newVector.data[0] = this.data[3][0];
		newVector.data[1] = this.data[3][1];
		newVector.data[2] = this.data[3][2];
		newVector.data[3] = 1.0f;
		
		return newVector;
	}
	
	public Matrix4 getBasis() {
		Matrix4 newMatrix = Matrix4.getIdentity();
		for (int row = 0; row < 3; ++row) {
			for (int col = 0; col < 3; ++col) {
				newMatrix.data[row][col] = this.data[row][col];
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
