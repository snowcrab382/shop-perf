package perf.shop.global.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<EnumValue, String> {
    private EnumValue enumValue;

    @Override
    public void initialize(final EnumValue constraintAnnotation) {
        this.enumValue = constraintAnnotation;
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        final Enum<?>[] enumConstants = this.enumValue.enumClass().getEnumConstants();
        if (enumConstants == null) {
            return false;
        }

        if (value == null) {
            return true;
        }

        return Arrays.stream(enumConstants)
                .anyMatch(
                        enumConstant -> convertible(value, enumConstant) || convertibleIgnoreCase(value, enumConstant));
    }

    private boolean convertibleIgnoreCase(final String value, final Enum<?> enumConstant) {
        return this.enumValue.ignoreCase() && value.trim().equalsIgnoreCase(enumConstant.name());
    }

    private boolean convertible(final String value, final Enum<?> enumConstant) {
        return value.trim().equals(enumConstant.name());
    }

}
