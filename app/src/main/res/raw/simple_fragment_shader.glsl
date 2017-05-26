/* 定义所有浮点类型的默认精度 mediump 速度与质量的权衡*/
precision mediump float;

/* uniform: uniform不像attribute每个顶点都要设置一个 一个uniform会让每个顶点都使用同样的值 除非我们再次改变它 */
/* vec4:    四分量的向量 在颜色的上下文中 分别对应 红、绿、蓝、阿尔法 */
uniform vec4 u_Color;

/* 着色器主入口 把在uniform中定义的颜色复制到那个特殊的输出变量gl_FragColor */
/* 着色器一定要给gl_FragColor赋值 OpenGL会使用这个颜色作为当前片段的最终颜色 */
void main(){
    gl_FragColor = u_Color;
}