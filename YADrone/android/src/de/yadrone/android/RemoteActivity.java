package de.yadrone.android;

import android.R.bool;
import android.os.Bundle;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.shigeodayo.ardrone.ARDrone;
import com.shigeodayo.ardrone.command.CommandManager;
import com.shigeodayo.ardrone.command.PCMDMagCommand;
import com.shigeodayo.ardrone.navdata.AttitudeListener;
import com.shigeodayo.ardrone.navdata.NavDataManager;
import com.shigeodayo.ardrone.command.PCMDMagCommand;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.widget.TextView;

public class RemoteActivity extends BaseActivity {
	
	public RemoteActivity() {
		super(R.id.menuitem_remote);
	}

	private GLSurfaceView mGLSurfaceView;
    private SensorManager mSensorManager;
    private CommandManager mCommandManager;
    private NavDataManager mNavDataManager;
    private MyRenderer mRenderer;
    
    private short[] mDroneData; 
    private float[] mMovement;
    private long mLastCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get an instance of the SensorManager
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        
        // Use existing view and set to OpenGL ES 2.0
        setContentView(R.layout.activity_remote);
        mGLSurfaceView = (GLSurfaceView) findViewById(R.id.myGLView);
        //mGLSurfaceView.setEGLContextClientVersion(2);
        //mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        
        mRenderer = new MyRenderer();
        mGLSurfaceView.setRenderer(mRenderer);
        
		YADroneApplication app = (YADroneApplication) getApplication();
		final ARDrone drone = app.getARDrone();
		mNavDataManager = drone.getNavDataManager();
		mCommandManager = drone.getCommandManager();
		
//		mNavDataManager.setAttitudeListener(new AttitudeListener() {
//
//			@Override
//			public void attitudeUpdated(float pitch, float roll, float yaw) {
//				mDroneData[0] = (short) pitch;
//				mDroneData[1] = (short) roll;
//				mDroneData[2] = (short) yaw;
//				
//				TextView t = (TextView) findViewById(R.id.remoteText1);
//				String s = "Drone: " + 
//					String.format("%.1f", pitch) + ", " + 
//					String.format("%.1f", roll) + ", " + 
//					String.format("%.1f", yaw);
//				t.setText(s);
//			}
//
//			@Override
//			public void attitudeUpdated(float pitch, float roll) {}
//
//			@Override
//			public void windCompensation(float pitch, float roll) {}
//		});
    }

    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mRenderer.start();
        mGLSurfaceView.onResume();
        
        mCommandManager.takeOff();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mRenderer.stop();
        mGLSurfaceView.onPause();
        
        mCommandManager.landing();
    }


    class MyRenderer implements GLSurfaceView.Renderer, SensorEventListener {
        private Cube mCube;
        private Sensor mRotationVectorSensor;
        private Sensor mAccelerationSensor;
        private final float[] mRotationMatrix = new float[16];
        private final float[] mSizeVector = new float[3];

        public MyRenderer() {
            // find the rotation-vector sensor
            mRotationVectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            mAccelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            
            mCube = new Cube();
            // initialize the rotation matrix to identity
            mRotationMatrix[ 0] = 1;
            mRotationMatrix[ 4] = 1;
            mRotationMatrix[ 8] = 1;
            mRotationMatrix[12] = 1;
        }

        public void start() {
            // enable our sensor when the activity is resumed, ask for
            // 10 ms updates.
            mSensorManager.registerListener(this, mRotationVectorSensor, SensorManager.SENSOR_DELAY_GAME);
            mSensorManager.registerListener(this, mAccelerationSensor, SensorManager.SENSOR_DELAY_UI);
        }

        public void stop() {
            // make sure to turn our sensor off when the activity is paused
            mSensorManager.unregisterListener(this);
        }

        public void onSensorChanged(SensorEvent event) {
            // we received a sensor event. it is a good practice to check
            // that we received the proper event
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                // convert the rotation-vector to a 4x4 matrix. the matrix
                // is interpreted by Open GL as the inverse of the
                // rotation-vector, which is what we want.
                SensorManager.getRotationMatrixFromVector(
                        mRotationMatrix , event.values);
                
				TextView t = (TextView) findViewById(R.id.remoteText2);
				String s = "A " + 
						String.format("%d", event.values.length) + ": " +
						String.format("%.1f", event.values[0]) + ", " + 
						String.format("%.1f", event.values[1]) + ", " + 
						String.format("%.1f", event.values[2]);
				t.setText(s);
				
				if (System.currentTimeMillis() - mLastCommand > 500)
				{
					boolean usePCM = false;
					
					if (usePCM)
					{
						if ((Math.abs(event.values[0]) > 0.1) | (Math.abs(event.values[1]) > 0.1))
						{
							new PCMDMagCommand(false, false, true, event.values[0], event.values[1], 25, 5, 0, 0);
							s = "PCM: F, T, F, " + event.values[0] + ", " + event.values[1] + ", 25, 5, 0, 0";
						}
					}
					else
					{
						s = "Move ";
						if (event.values[1] > 0.1)
						{
							mCommandManager.forward((int) (event.values[1] * event.values[1] * 400));
							s = s + "Fwd: " + (int) (event.values[1] * event.values[1] * 400);
						}
						else if (event.values[1] < -0.1)
						{
							mCommandManager.backward((int) (event.values[1] * event.values[1] * 400));
							s = s + "Bck: " + (int) (event.values[1] * event.values[1] * 400);
						}
	
						
						if (event.values[0] < -0.1)
						{
							mCommandManager.goLeft((int) (event.values[0] * event.values[0] * 400));
							s = s + " L: " + (int) (event.values[0] * event.values[0] * 400);
						}
						else if (event.values[0] > 0.1)
						{
							mCommandManager.goRight((int) (event.values[0] * event.values[0] * 400));
							s = s + " R: " + (int) (event.values[0] * event.values[0] * 400);
						}
					}
					
					if ((Math.abs(event.values[0]) <= 0.1) && (Math.abs(event.values[1]) <= 0.1))
					{
						mCommandManager.freeze();
						s = "Freeze";
					};
					
					TextView u = (TextView) findViewById(R.id.remoteText1);
					u.setText(s);
					
					mLastCommand = System.currentTimeMillis();
				}
            }
            
            if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            	mSizeVector[0] = Math.abs(event.values[0] + mSizeVector[0] /2) + 1;
            	mSizeVector[1] = Math.abs(event.values[1] + mSizeVector[1] /2) + 1;
            	mSizeVector[2] = Math.abs(event.values[2] + mSizeVector[2] /2) + 1;
                
				TextView t = (TextView) findViewById(R.id.remoteText3);
				String s = "S " + 
						String.format("%d", event.values.length) + ": " +
						String.format("%.1f", mSizeVector[0]) + ", " + 
						String.format("%.1f", mSizeVector[1]) + ", " + 
						String.format("%.1f", mSizeVector[2]);
				t.setText(s);
            }
        }

        public void onDrawFrame(GL10 gl) {
            // clear screen
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

            // set-up modelview matrix
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(0, 0, -3.0f);
            gl.glMultMatrixf(mRotationMatrix, 0);
            
            // draw our object
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

            mCube.draw(gl);
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            // set view-port
            gl.glViewport(0, 0, width, height);
            // set projection matrix
            float ratio = (float) width / height;
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10); //-1, 1, 3, 7 is default
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // dither is enabled by default, we don't need it
            gl.glDisable(GL10.GL_DITHER);
            // clear screen in white
            gl.glClearColor(1,1,1,1);
        }

        class Cube {
            // initialize our cube
            private FloatBuffer mVertexBuffer;
            private FloatBuffer mColorBuffer;
            private ByteBuffer  mIndexBuffer;

            public Cube() {
                final float vertices[] = {
                        -1, -1, -1,     1, -1, -1,
                         1,  1, -1,    -1,  1, -1,
                        -1, -1,  1,     1, -1,  1,
                         1,  1,  1,    -1,  1,  1,
                };

                final float colors[] = {
                        0,  0,  0,  1,  1,  0,  0,  1,
                        1,  1,  0,  1,  0,  1,  0,  1,
                        0,  0,  1,  1,  1,  0,  1,  1,
                        1,  1,  1,  1,  0,  1,  1,  1,
                };

                final byte indices[] = {
                        0, 4, 5,    0, 5, 1,
                        1, 5, 6,    1, 6, 2,
                        2, 6, 7,    2, 7, 3,
                        3, 7, 4,    3, 4, 0,
                        4, 7, 6,    4, 6, 5,
                        3, 0, 1,    3, 1, 2
                };

                ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
                vbb.order(ByteOrder.nativeOrder());
                mVertexBuffer = vbb.asFloatBuffer();
                mVertexBuffer.put(vertices);
                mVertexBuffer.position(0);

                ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
                cbb.order(ByteOrder.nativeOrder());
                mColorBuffer = cbb.asFloatBuffer();
                mColorBuffer.put(colors);
                mColorBuffer.position(0);

                mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
                mIndexBuffer.put(indices);
                mIndexBuffer.position(0);
            }

            public void draw(GL10 gl) {
                gl.glEnable(GL10.GL_CULL_FACE);
                gl.glFrontFace(GL10.GL_CW);
                gl.glShadeModel(GL10.GL_SMOOTH);
                gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
                gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
                gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);
            }            
        }

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
		}
    }
}
