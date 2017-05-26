package com.example.hemingchiu.AirHockey1.util;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

/**
 * Created by hemingchiu on 2017/5/25.
 */

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";
    public static int compileVertexShader(String shaderCode) {
        return compileShader(GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCoder) {
        return compileShader(GL_FRAGMENT_SHADER, shaderCoder);
    }


    /*
    * 1、glCreateShader 创建着色器
    * 2、glShaderSource 为所创建的着色器上传源码
    * 3、glCompileShader 编译所创建的着色器
    * 4、glGetShaderiv 获取编译结果
    * 5、返回着色器ID
    * */
    public static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = glCreateShader(type);    //创建一个传入类型的shader

        if (shaderObjectId == 0) {  //创建失败返回0 创建成功返回shaderID
            Log.e(TAG, "compileShader: Could not creat new shader.");
            return 0;
        }
        glShaderSource(shaderObjectId, shaderCode);     //上传shader源代码 告诉OpenGL读入shaderCode源代码 并把它与shaderObjectId所引用的着色器对象关联起来。
        glCompileShader(shaderObjectId);    //告诉OpenGL编译先前上传到shaderObjectId的源代码

        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, compileStatus, 0); //读取与shaderObjectId关联的编译状态 并写入compileStatus的第0个元素中

        //打印编译结果 获取作色器信息日志
        Log.e(TAG, "compileShader: result of compiling source:\n" + shaderCode + "\n" + glGetShaderInfoLog(shaderObjectId));

        if (compileStatus[0] == 0) {            //返回值为0 表明编译失败
            glDeleteShader(shaderObjectId);     //编译失败 则删除着色器
            Log.e(TAG, "compileShader: " + "Compilation of shader failed." );
            return 0;
        }
        return shaderObjectId;  //编译成功 返回新的着色器对象ID
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId){
        final int programObjectId = glCreateProgram();      //创建program
        if (programObjectId == 0) {
            Log.e(TAG, "linkProgram: Could not creat new program");
            return 0;
        }
        glAttachShader(programObjectId, vertexShaderId);    //绑定顶点着色器
        glAttachShader(programObjectId, fragmentShaderId);  //绑定片段着色器
        glLinkProgram(programObjectId); //链接program
        final int[] linkStatus = new int[1];
        glGetProgramiv(programObjectId, GL_LINK_STATUS, linkStatus, 0); //获取链接状态
        Log.e(TAG, "linkProgram: Results of linking program:\n" + glGetProgramInfoLog(programObjectId));

        if (linkStatus[0] == 0){
            glDeleteProgram(programObjectId);
            Log.e(TAG, "linkProgram: Linking of program failed.");
            return 0;
        }

        return programObjectId;     //链接成功 返回program的ID
    }

    public static boolean validateProgram(int programObjectId) {
        glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS, validateStatus, 0);
        Log.e(TAG, "validateProgram: "+ validateStatus[0] + "\n" +glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }
}