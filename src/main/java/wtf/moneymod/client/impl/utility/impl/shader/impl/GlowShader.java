package wtf.moneymod.client.impl.utility.impl.shader.impl;

import wtf.moneymod.client.impl.utility.impl.shader.*;
import org.lwjgl.opengl.*;

public class GlowShader extends FramebufferShader
{
    public static final FramebufferShader INSTANCE;
    
    public GlowShader(final String fragmentShader) {
        super(fragmentShader);
    }
    
    public void setupUniforms() {
        this.setupUniform("texture");
        this.setupUniform("texelSize");
        this.setupUniform("color");
        this.setupUniform("radius");
        this.setupUniform("devider");
        this.setupUniform("maxSample");
        this.setupUniform("rainbowStrength");
        this.setupUniform("rainbowSpeed");
        this.setupUniform("saturation");
    }
    
    public void updateUniforms() {
        GL20.glUniform1i(this.getUniform("texture"), 0);
        GL20.glUniform2f(this.getUniform("texelSize"), 1.0f / GlowShader.mc.displayWidth * (this.radius * this.quality), 1.0f / GlowShader.mc.displayHeight * (this.radius * this.quality));
        GL20.glUniform3f(this.getUniform("color"), this.red, this.green, this.blue);
        GL20.glUniform1f(this.getUniform("radius"), this.radius);
        GL20.glUniform2f(this.getUniform("rainbowStrength"), this.x, this.y);
        GL20.glUniform1f(this.getUniform("rainbowSpeed"), this.speed);
        GL20.glUniform1f(this.getUniform("saturation"), this.saturation);
    }
    
    static {
        INSTANCE = new GlowShader("glow.frag");
    }
}
