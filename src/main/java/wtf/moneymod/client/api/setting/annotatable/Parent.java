package wtf.moneymod.client.api.setting.annotatable;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Parent {
    String value();
}
