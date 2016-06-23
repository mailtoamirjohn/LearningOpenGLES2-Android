package kr.pe.burt.android.animatecube;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.renderscript.Float3;
import android.renderscript.Matrix4f;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import kr.pe.burt.android.animatecube.glkit.ShaderProgram;
import kr.pe.burt.android.animatecube.glkit.ShaderUtils;


/**
 * Created by burt on 2016. 6. 15..
 */
public class OGLRenderer implements GLSurfaceView.Renderer {

    private static final float ONE_SEC = 1000.0f; // 1 second

    private Context context;
    private Cube   cube;
    private long lastTimeMillis = 0L;

    public OGLRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

        ShaderProgram shader = new ShaderProgram(
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.simple_vertex_shader),
                ShaderUtils.readShaderFileFromRawResource(context, R.raw.simple_fragment_shader)
        );

        cube = new Cube(shader);
        cube.setPosition(new Float3(0.0f, 0.0f, 0.0f));

        lastTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        GLES20.glViewport(0, 0, w, h);

        Matrix4f perspective = new Matrix4f();
        perspective.loadPerspective(85.0f, (float)w / (float)h, 1.0f, -150.0f);

        if(cube != null) {
            cube.setProjection(perspective);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        long currentTimeMillis = System.currentTimeMillis();
        updateWithDelta(currentTimeMillis - lastTimeMillis);
        lastTimeMillis = currentTimeMillis;
    }

    public void updateWithDelta(long dt) {

        Matrix4f camera2 = new Matrix4f();
        camera2.translate(0.0f, 0.0f, -5.0f);
        cube.setCamera(camera2);
        cube.setRotationY((float)( cube.rotationY + Math.PI * dt / (ONE_SEC * 0.1f) ));
        cube.setRotationZ((float)( cube.rotationZ + Math.PI * dt / (ONE_SEC * 0.1f) ));
        cube.draw(dt);
    }

}
