public class Camera {
	private Matrix4 _matrix;
	private Vector4 _lookat = null;
	
	public Camera() {
		_matrix = Matrix4.getIdentity();
	}
	
	public void setLookAt(Vector4 lookat) {
		_lookat = lookat;
	}
	
	public Vector4 getRight() {
		Vector4 newVector = new Vector4();
		newVector.data[0] = _matrix.data[0][0];
		newVector.data[1] = _matrix.data[0][1];
		newVector.data[2] = _matrix.data[0][2];
		newVector.data[3] = 0.0f;
		
		return newVector;
	}
	
	public Vector4 getLeft() {
		return this.getRight().multiply(-1.0f);
	}
	
	public Vector4 getForward() {
		return this.getBackward().multiply(-1.0f);
	}
	
	public Vector4 getUp() {
		Vector4 newVector = new Vector4();
		newVector.data[0] = _matrix.data[1][0];
		newVector.data[1] = _matrix.data[1][1];
		newVector.data[2] = _matrix.data[1][2];
		newVector.data[3] = 0.0f;
		
		return newVector;
	}
	
	public Vector4 getBackward() {
		Vector4 newVector = new Vector4();
		newVector.data[0] = _matrix.data[2][0];
		newVector.data[1] = _matrix.data[2][1];
		newVector.data[2] = _matrix.data[2][2];
		newVector.data[3] = 0.0f;
		
		return newVector;
	}
	
	public Matrix4 getBasis() {
		return _matrix.getBasis();
	}
	
	public Vector4 getPosition() {
		return _matrix.getPosition();
	}
	
	public void setBasis(Matrix4 basis) {
		_matrix.setBasis(basis);
	}
	
	public void setPosition(Vector4 pos) {
		_matrix.setPosition(pos);
	}
	
	public Matrix4 getViewMatrix() {
		Vector4 position = _matrix.getPosition();
		Matrix4 viewMatrix = Matrix4.getIdentity();
		if (_lookat == null) {
			Matrix4 rotMatrix = _matrix.getBasis().getTranspose();
			position = position.multiply(rotMatrix).multiply(-1.0f);
			viewMatrix.setPosition(position);
			viewMatrix.setBasis(rotMatrix);
		} else {
			Vector4 lookAtVec = position.sub(_lookat);
			lookAtVec.normalize();
			
			Vector4 up = new Vector4(0, 1, 0, 0);
			Vector4 right = up.cross(lookAtVec);
			right.normalize();
			
			Vector4 camUp = lookAtVec.cross(right);
			camUp.normalize();
			
			Matrix4 rotMatrix = Matrix4.getIdentity();
			
			rotMatrix.setRow(0, right);
			rotMatrix.setRow(1, camUp);
			rotMatrix.setRow(2, lookAtVec);
			rotMatrix = rotMatrix.getTranspose();
			position = position.multiply(rotMatrix).multiply(-1.0f);
			viewMatrix.setPosition(position);
			viewMatrix.setBasis(rotMatrix);
		}
		
		return viewMatrix;
	}
}
