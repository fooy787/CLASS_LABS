// Filename: GLWindow.java
// By: Andrew Holbrook
// Date: 9/19/2016

import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.system.MemoryUtil.*;

public class GLWindow {
	private int width;
	private int height;
	private String title = "GLWindow";
	private long _glfwWindowHandle;
	
	private static GLWindow _instance = null;
	
	// ---- CLASS METHODS FOR CREATING/GETTING WINDOW
	
	public static GLWindow getInstance() {
		if (GLWindow._instance == null)
			GLWindow._instance = new GLWindow(400, 400);
		return GLWindow._instance;
	}
	
	// width and height arguments have no effect if the window has already been
	// created
	public static GLWindow getInstance(int width, int height) {
		if (GLWindow._instance == null)
			GLWindow._instance = new GLWindow(width, height);
		return GLWindow._instance;
	}
	
	// ---- INPUT HANDLING METHODS ----
	
	public boolean isPressed(int key) {
		return glfwGetKey(_glfwWindowHandle, key) == GLFW_PRESS ? true : false;
	}
	
	public boolean isLeftMouseButtonPressed() {
		int state = glfwGetMouseButton(_glfwWindowHandle, GLFW_MOUSE_BUTTON_1);
		return state == GLFW_PRESS ? true : false;
	}
	
	public boolean isRightMouseButtonPressed() {
		int state = glfwGetMouseButton(_glfwWindowHandle, GLFW_MOUSE_BUTTON_2);
		return state == GLFW_PRESS ? true : false;
	}
	
	public boolean isMiddleMouseButtonPressed() {
		int state = glfwGetMouseButton(_glfwWindowHandle, GLFW_MOUSE_BUTTON_3);
		return state == GLFW_PRESS ? true : false;
	}
	
	public int[] getMousePosition() {
		double[] xpos_d = new double[1];
		double[] ypos_d = new double[1];
		glfwGetCursorPos(_glfwWindowHandle, xpos_d, ypos_d);
		
		// convert cursor position to int and bound to window
		int xpos_i = (int)xpos_d[0];
		xpos_i = Math.max(0, Math.min(this.width, xpos_i));
		
		int ypos_i = (int)ypos_d[0];
		ypos_i = Math.max(0, Math.min(this.height, ypos_i));
		
		return new int[]{xpos_i, ypos_i};
	}
	
	// ---- OTHER PUBLIC METHODS
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public float getAspect() {
		return (float)width / height;
	}
	
	// tell the window it should close
	public void close() {
		glfwSetWindowShouldClose(_glfwWindowHandle, true);
	}
	
	// check if window has been "told" to close
	public boolean shouldClose() {
		return glfwWindowShouldClose(_glfwWindowHandle);
	}
	
	public void setTitle(String title) {
		this.title = title;
		glfwSetWindowTitle(_glfwWindowHandle, title);
	}
	
	// get GLFW handle for window
	public long getWindowHandle() {
		return _glfwWindowHandle;
	}
	
	public void swapBuffers() {
		glfwSwapBuffers(_glfwWindowHandle);
	}
	
	public void pollEvents() {
		glfwPollEvents();
	}
	
	public void cleanup() {
		ShaderProgram.cleanupDefaultProgram();
		glfwFreeCallbacks(_glfwWindowHandle);
		glfwDestroyWindow(_glfwWindowHandle);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	// ---- PRIVATE METHODS ----
	
	private GLWindow(int width, int height) {
		this.width = width;
		this.height = height;
		
		// initialize GLFW and check for errors
		if (!this.initGLFW()) {
			System.err.println("Error initializing GLFW");
			this.cleanup();
			System.exit(0);
		}
		
		// set window hints (visible, resizable, opengl version, etc) and
		// create window
		this.setWindowHints();
		_glfwWindowHandle = glfwCreateWindow(
			width, // window width
			height, // window height
			this.title, // window title
			NULL, // specify monitor for fullscreen (NULL means windowed)
			NULL // do not share opengl context
		);
		
		// check that the window was created without error
		if (_glfwWindowHandle == NULL) {
			System.err.println("Error creating window");
			this.cleanup();
			System.exit(0);
		}
		
		// make the window's opengl context the current one and use lwjgl's
		// createCapabilities method to setup the opengl api
		glfwMakeContextCurrent(_glfwWindowHandle);
		GL.createCapabilities();
		
		// initialize default shader program
		if (!ShaderProgram.initDefaultProgram()) {
			System.err.println("Error building shader program");
			this.cleanup();
			System.exit(0);
		}
		
		// the default program will be used when rendering unless another
		// program is specified
		ShaderProgram.useDefaultProgram();
		
		// make window visible
		glfwShowWindow(_glfwWindowHandle);
	}
	
	private boolean initGLFW() {
		// tell GLFW to print errors to the console
		GLFWErrorCallback.createPrint(System.err).set();
		return glfwInit();
	}
	
	private void setWindowHints() {
		
		// make window invisible (we will make it visible after initial setup)
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		
		// make window not resizable
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		// ask for an opengl 4.1 context
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
		
		// use core profile--don't allow deprecated functions
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		
		// uncomment if using a mac running osx
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
	}
}
