package wtf.moneymod.eventhandler.listener;

import java.lang.annotation.*;
import wtf.moneymod.eventhandler.event.enums.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Handler {
    Priority priority() default Priority.MEDIUM;
    
    Era era() default Era.NONE;
}
