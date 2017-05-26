package com.example.hemingchiu.AirHockey1;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.example.hemingchiu.AirHockey1.util.ShaderHelper;
import com.example.hemingchiu.AirHockey1.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

/**
 * Created by hemingchiu on 2017/5/25.
 */

public class AirHockeyRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "FirstOpenGLProjectRende";
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    private final FloatBuffer vertexData;
    private final Context mContex;
    private int program;

    private static final String U_COLOR = "u_Color";
    private int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;

    public AirHockeyRenderer(Context contex) {
        mContex = contex;
        float[] tableVertices = {
                0f, 0f,
                0f, 14f,
                9f, 14f,
                9f, 0f
        };

        /* 逆时针排列顶点 --> 卷曲顺序 */
        float[] tableVerticesWithTriangles = {
//                0f, 0f,
//                9f, 14f,
//                0f, 14f,
//
//                0f, 0f,
//                9f, 0f,
//                9f, 14f,
//
//                // Line 1
//                0f, 7f,
//                9f, 7f,
//
//                // Mallets
//                4.5f, 2f,
//                4.5f, 12f

                // Triangle 1
                -0.5f, -0.5f,
                0.5f,  0.5f,
                -0.5f,  0.5f,

                // Triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f,  0.5f,

                // Line 1
                -0.5f, 0f,
                0.5f, 0f,

                // Mallets
                0f, -0.25f,
                0f,  0.25f,

                //桌子边框
                -0.55f, -0.55f,
                0.55f, 0.55f,
                -0.55f, 0.55f,

                -0.55f, -0.55f,
                0.55f, -0.55f,
                0.55f, 0.55f
        };

        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(tableVerticesWithTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContex, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContex, R.raw.simple_fragment_shader);

        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);    //编译顶点着色器
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);  //编译片段着色器
        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);   //创建program 绑定顶点着色器 片段着色器 并链接program 返回program的ID

        ShaderHelper.validateProgram(program);  //验证程序 获取program状态
        glUseProgram(program);  //告诉OpenGL在绘制任何东西到屏幕上的时候要使用这里定义的程序

        /*
            调用此函数 获取program绑定的着色器语言中uniform类型的u_Color的位置，并把这个位置存入uColorLocation
            后续可以调用glUniform*并传入此处获取的位置 用来修改着色器语言中定义的u_Color, 例：glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        */
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);   //获取属性attib位置 有了这个位置就能告诉OpenGL到哪里去找这个属性对应的数据了

        vertexData.position(0);     //移动顶点数据指针至起始位置

        //告诉OpenGL可以在缓冲区vertexData中找到a_Position对应的数据
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);
        glEnableVertexAttribArray(aPositionLocation);   //使能属性 OpenGL现在就知道去哪寻找它所需要的数据了

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);

        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);    //画桌子边框
        glDrawArrays(GL_TRIANGLES, 10, 6);  //第10个顶点开始 读入6个顶点（2个三角形的顶点数据）

        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);    //更新着色器代码中的u_Color的值    与属性attrib不同,uniform的分量没有默认值
        glDrawArrays(GL_TRIANGLES, 0, 6);   //画三角形 从0开始 读入6个顶点

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);

        Log.e(TAG, "onDrawFrame: ....");
    }
}
