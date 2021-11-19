package wtf.moneymod.client.api.setting.annotatable;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Bounds {
    float min() default 0.0f;
    
    float max() default 0.0f;
}
