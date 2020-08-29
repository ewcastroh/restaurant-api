package com.teaminternational.assessment.ewch.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Validations {

    public static boolean checkHasErrors(BindingResult result, Map<String, Object> response) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "Field '" + err.getField() + "' " + err.getDefaultMessage())
                    .peek(e -> System.err.println(e))
                    .collect(Collectors.toList());
            response.put(Constants.ERROR, errors);
            return true;
        }
        return false;
    }
}
