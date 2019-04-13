public class Vector4 {
	public float[] data;
	
	public Vector4() {
		this.init()
		for (int i = 0; i < 4; ++i) data [i] = 0.0f;
	}
	
	public Vector4(float x, float y, float z, float w) {
		this.init()
		
		this.data[0] = x;
		this.data[1] = y;
		this.data[2] = z;
		this.data[3] = w
	}
	
	private void init() {
		data = new float[4];
	}
}