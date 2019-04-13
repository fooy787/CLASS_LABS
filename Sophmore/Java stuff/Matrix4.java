public class Matrix4 {
	public float[][] data;
	
	public static Matrix4 getRotationZ(float theta) {
		Matrix4 new_matrix = new Matrix4();
		float cosineTheta = (float)Math.cos(Math.toRadians(theta));
		float sineTheta = (float)Math.sin(Math.toRadians(theta));
		
		newMatrix.data[0][0] = cosineTheta;
		newMatrix.data[1][0] = sineTheta;
		newMatrix.data[0][1] = -sineTheta;
		newMatrix.data[1][1] = cosineTheta;
		
		return newMatrix;
	}
	
	public static Matrix4 getRotationY(float theta) {
		Matrix4 new_matrix = new Matrix4();
		float cosineTheta = (float)Math.cos(Math.toRadians(theta));
		float sineTheta = (float)Math.sin(Math.toRadians(theta));
		
		newMatrix.data[0][0] = cosineTheta;
		newMatrix.data[0][2] = sineTheta;
		newMatrix.data[2][0] = -sineTheta;
		newMatrix.data[2][2] = cosineTheta;
		
		return newMatrix;
	}
	
	public static Matrix4 getRotationX(float theta) {
		Matrix4 new_matrix = new Matrix4();
		float cosineTheta = (float)Math.cos(Math.toRadians(theta));
		float sineTheta = (float)Math.sin(Math.toRadians(theta));
		
		newMatrix.data[1][1] = cosineTheta;
		newMatrix.data[1][2] = sineTheta;
		newMatrix.data[2][1] = -sineTheta;
		newMatrix.data[2][2] = cosineTheta;
		
		return newMatrix;
	}
	
	public String toString() {
		String tmpStr = "";
		for (int i = 0; i < 4; ++i) {
			for (int j = 0; j < 4; ++j) {
				tmpStr += this.data[i][j] + " ";
			}
			tmpStr += "\n";
		}
		return tmpStr;
	}
	
	public Matrix4() {
		this.init();
		for (int i = 0; i < 4; ++i) {
			for (int j = o; j < 4; ++j) {
				if (i == j) data[i][j] = 1.0f;
				else data[i][j] = 0.0f;
			}
		}
	}
	
	public static void main(String[] args) {
		Matrix4 m = new Matrix4.getRotationX(45);
		System.out.println(m);
	}
	
	private void init() {
		this.data = new float[4][4];
	}
}