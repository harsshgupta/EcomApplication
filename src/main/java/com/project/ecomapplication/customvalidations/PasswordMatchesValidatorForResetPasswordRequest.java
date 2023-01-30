package com.project.ecomapplication.customvalidations;

import com.project.ecomapplication.dto.request.ResetPasswordDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidatorForResetPasswordRequest implements ConstraintValidator<PasswordMatchesForResetPasswordRequest, Object> {

    @Override
    public void initialize(final PasswordMatchesForResetPasswordRequest constraintAnnotation) {
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final ResetPasswordDto user = (ResetPasswordDto) obj;
        return user.getPassword().equals(user.getConfirmPassword());
    }

}
