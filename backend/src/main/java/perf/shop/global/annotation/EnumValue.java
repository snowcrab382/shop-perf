package perf.shop.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface EnumValue {
    Class<? extends Enum<?>> enumClass();

    String message() default "ENUM 값이 올바르지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean ignoreCase() default false;
}
