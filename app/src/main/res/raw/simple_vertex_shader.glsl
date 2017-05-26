/* 对于顶点的位置而言 精确度是最重要的 所以顶点着色器可以不定义精度 默认精度为highp */

//vec4: 包含4个分量的向量 在位置的上下文中可以认为这4个向量是xyzw坐标
//attribute:    顶点会有几个属性如颜色、位置，attribute是把这些属性放入着色器的手段
attribute vec4 a_Position;

/*main() 着色器的主要入口点 把前面定义过的位置复制到制定的输出变量gl_Position;
这个着色器一定要给gl_Position赋值，OpenGL会把gl_Position中存储的值作为当前顶点的最终位置并把这些顶点组装成点、直线、三角形。*/
void main(){
    gl_Position = a_Position;
    gl_PointSize = 10.0;
}