// Filename: Vector4.java
// By: Andrew Holbrook
// Date: 9/19/2016 

public class Vector4 {
	public float[] data;

	public Vector4() {
		this.init();
		for (int i = 0; i < 4; ++i) this.data[i] = 0.0f;
	}

	public Vector4(float x, float y, float z) {
		this.init();
		this.data[0] = x;
		this.data[1] = y;
		this.data[2] = z;
		this.data[3] = 0.0f;
	}
	
	public float getX(){
		return this.data[0];
	}
	
	public float getY(){
		return this.data[1];
	}
	
	public float getZ(){
		return this.data[2];
	}

	public Vector4(float x, float y, float z, float w) {
		this.init();
		this.data[0] = x;
		this.data[1] = y;
		this.data[2] = z;
		this.data[3] = w;
	}

	public String toString() {
		String tmpStr = "";
		for (int i = 0; i < 4; ++i) tmpStr += this.data[i] + " ";
		return tmpStr;
	}
	
	public Vector4 add(Vector4 v) {
		Vector4 newVector = new Vector4();
		
		for (int i = 0; i < 4; ++i)
			newVector.data[i] = this.data[i] + v.data[i];
		
		return newVector;
	}
	
	public Vector4 add(float f) {
		Vector4 newVector = new Vector4();
		
		for (int i = 0; i < 4; ++i)
			newVector.data[i] = this.data[i] + f;
		
		return newVector;
	}
	
	public Vector4 sub(Vector4 v) {
		Vector4 newVector = new Vector4();
		
		for (int i = 0; i < 4; ++i)
			newVector.data[i] = this.data[i] - v.data[i];
		
		return newVector;
	}
	
	public Vector4 sub(float f) {
		Vector4 newVector = new Vector4();
		
		for (int i = 0; i < 4; ++i)
			newVector.data[i] = this.data[i] - f;
		
		return newVector;
	}

	public Vector4 multiply(Matrix4 m) {
		Vector4 newVector = new Vector4();
		
		for (int col = 0; col < 4; ++col) {
			for (int row = 0; row < 4; ++row) {
				newVector.data[col] += this.data[row] * m.data[row][col];
			}
		}

		return newVector;
	}
	
	public Vector4 mul(Matrix4 m) {
		return this.multiply(m);
	}
	
	public Vector4 multiply(float f) {
		Vector4 newVector = new Vector4();
		
		for (int col = 0; col < 4; ++col) {
			newVector.data[col] = this.data[col] * f;
		}

		return newVector;
	}
	
	public Vector4 mul(float f) {
		return this.multiply(f);
	}
	
	public Vector4 divide(float f) {
		Vector4 newVector = new Vector4();
		
		for (int i = 0; i < 4; ++i)
			newVector.data[i] = this.data[i] / f;
		
		return newVector;
	}
	
	public Vector4 div(float f) {
		return this.divide(f);
	}
	
	public Vector4 cross(Vector4 v) {
		return new Vector4(
			this.data[1] * v.data[2] - this.data[2] * v.data[1],
			this.data[2] * v.data[0] - this.data[0] * v.data[2],
			this.data[0] * v.data[1] - this.data[1] * v.data[0],
			0.0f);
	}
	
	public float length() {
		return (float)Math.sqrt(
			this.data[0] * this.data[0] +
			this.data[1] * this.data[1] +
			this.data[2] * this.data[2]);
	}
	
	public Vector4 normalize() {
		float tmplen = this.length();
		if (tmplen > 0) {
			this.data[0] /= tmplen;
			this.data[1] /= tmplen;
			this.data[2] /= tmplen;
		}
		
		return this;
	}

	private void init() {
		this.data = new float[4];
	}

	public static void main(String[] args) {
		Vector4 vecA = new Vector4(1.0f, 0.0f, 0.0f);
		Matrix4 rotZ45 = Matrix4.getRotationZ(45.0f);
		System.out.println(rotZ45);
		System.out.println(vecA);
		Vector4 vecARotZ45 = vecA.multiply(rotZ45);
		System.out.println(vecARotZ45);
	}
}
