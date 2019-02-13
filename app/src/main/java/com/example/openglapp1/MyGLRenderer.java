package com.example.openglapp1;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Triangle mTriangle;
    private Triangle mTriangle2;
    private Triangle mTriangle3;

    private float[] mRotationMatrix = new float[16];


    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    public volatile float mAngle;

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    public Triangle getmTriangle() {
        return mTriangle;
    }

    @Override
        public void onSurfaceCreated(GL10 gl, javax.microedition.khronos.egl.EGLConfig config) {
            // Set the background frame color
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            // initialize a triangle

            float triangleCoords[] = {   // in counterclockwise order:
                         0.0f,  0.622008459f, 0.0f, // top
                        -0.55f,  0.311004243f, 0.0f, // bottom left
                        -0.35f,  0.311004243f, 0.0f  // bottom right
            };
            float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

            mTriangle=new Triangle(triangleCoords,color);


            float triangleCoords2[] = {   // in counterclockwise order:
                    0.0f,  0.622008459f, 0.0f, // top
                    -0.5f, -0.311004243f, 0.0f, // bottom left
                    0.5f, -0.311004243f, 0.0f  // bottom right
            };
            float color2[] = { 0.23671875f, 0.746953125f, 0.82265625f, 1.0f };

            mTriangle2=new Triangle(triangleCoords2,color2);


            float triangleCoords3[] = {   // in counterclockwise order:
                    0.0f,  0.622008459f, 0.0f, // top
                    0.55f,  0.311004243f, 0.0f, // bottom left
                    0.35f,  0.311004243f, 0.0f  // bottom right
            };
            float color3[] = { 0.83671875f, 0.56953125f, 0.32265625f, 1.0f };

            mTriangle3=new Triangle(triangleCoords3,color3);


        }

        @Override
        public void onDrawFrame(GL10 unused) {
            float[] scratch = new float[16];


            // Redraw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            // Set the camera position (View matrix)
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

            // Calculate the projection and view transformation
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

            // Create a rotation transformation for the triangle
            long time = SystemClock.uptimeMillis() % 4000L;
            float angle = 0.090f * ((int) time);
            Matrix.setRotateM(mRotationMatrix, 0, mAngle+angle, 0, 0, 1.0f);



            // Combine the rotation matrix with the projection and camera view
            // Note that the mMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);


            mTriangle.draw(scratch);
            mTriangle2.draw(scratch);
            mTriangle3.draw(scratch);




        }

        @Override
        public void onSurfaceChanged(GL10 unused, int width, int height) {
            GLES20.glViewport(0, 0, width, height);

            float ratio = (float) width / height;

            // this projection matrix is applied to object coordinates
            // in the onDrawFrame() method
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);




        }


        public static int loadShader(int type, String shaderCode){

            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            int shader = GLES20.glCreateShader(type);

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);

            return shader;
        }

    }