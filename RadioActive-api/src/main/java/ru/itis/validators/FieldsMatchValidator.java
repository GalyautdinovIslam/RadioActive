package ru.itis.validators;

import org.springframework.data.util.ReflectionUtils;
import ru.itis.annotations.FieldsMatch;
import ru.itis.dto.request.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FieldsMatchValidator implements ConstraintValidator<FieldsMatch, SignUpRequest> {

    private String[] fields;

    @Override
    public void initialize(FieldsMatch constraintAnnotation) {
        fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(SignUpRequest signUpRequest, ConstraintValidatorContext constraintValidatorContext) {
        Set<String> set = new HashSet<>();
        List<Field> fieldList = new ArrayList<>();
        for (String f : fields) {
            try {
                Field field = ReflectionUtils.findRequiredField(signUpRequest.getClass(), f);
                fieldList.add(field);
                field.setAccessible(true);
                Object object = field.get(signUpRequest);
                if (object == null) return false;
                set.add((String) object);
            } catch (IllegalAccessException | ClassCastException e) {
                return false;
            } finally {
                for (Field field : fieldList) {
                    field.setAccessible(false);
                }
            }
        }
        return set.size() == 1;
    }
}
