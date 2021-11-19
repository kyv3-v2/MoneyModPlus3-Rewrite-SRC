package wtf.moneymod.client.impl.utility.impl.shader.impl;

import wtf.moneymod.client.impl.utility.impl.shader.*;
import org.lwjgl.opengl.*;

public class SpaceShader extends FramebufferShader
{
    float time;
    public static final FramebufferShader INSTANCE;
    
    public SpaceShader(final String fragmentShader) {
        super(fragmentShader);
        this.time = 1.0f;
    }
    
    public void setupUniforms() {
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("color");
        this.setupUniform("time");
        this.setupUniform("resolution");
    }
    
    public void updateUniforms() {
        GL20.glUniform1i(this.getUniform("texture"), 0);
        GL20.glUniform2f(this.getUniform("texelSize"), 1.0f / SpaceShader.mc.displayWidth * (this.radius * this.quality), 1.0f / SpaceShader.mc.displayHeight * (this.radius * this.quality));
        GL20.glUniform4f(this.getUniform("color"), this.red, this.green, this.blue, this.alpha);
        GL20.glUniform1f(this.getUniform("time"), this.time);
        GL20.glUniform2f(this.getUniform("resolution"), (float)SpaceShader.mc.displayWidth, (float)SpaceShader.mc.displayHeight);
        this.time += (float)0.003;
    }
    
    static {
        INSTANCE = new SpaceShader("space.frag");
    }
}
