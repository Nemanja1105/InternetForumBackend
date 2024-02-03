package org.unibl.etf.models.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SqlValidator implements ConstraintValidator<SqlValidators, String> {



    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println(value);
        for(var pattern: getSqlPatterns()) {
            Matcher matcher = pattern.matcher(value);
            if (matcher.find())
                return false;
        }
        return true;
    }


    private List<Pattern> getSqlPatterns() {
       return Arrays.asList(Pattern.compile("(?i)(.*)(\\b)+(OR|AND)(\\s)+(true|false)(\\s)*(.*)", Pattern.CASE_INSENSITIVE),
               Pattern.compile("(?i)(.*)(\\b)+(OR|AND)(\\s)+(\\w)(\\s)*(\\=)(\\s)*(\\w)(\\s)*(.*)", Pattern.CASE_INSENSITIVE),
               Pattern.compile("(?i)(.*)(\\b)+SELECT(\\b)+\\s.*(\\b)(.*)", Pattern.CASE_INSENSITIVE),
               Pattern.compile("(?i)(.*)(\\b)+DELETE(\\b)+\\s.*(\\b)+FROM(\\b)+\\s.*(.*)", Pattern.CASE_INSENSITIVE),
               Pattern.compile("(?i)(.*)(\\b)+(INSERT|UPDATE|UPSERT|SAVEPOINT|CALL)(\\b)+\\s.*(.*)", Pattern.CASE_INSENSITIVE),
               Pattern.compile("(?i)(.*)(\\b)+DROP(\\b)+\\s.*(.*)", Pattern.CASE_INSENSITIVE),
               Pattern.compile("(?i)(.*)(\\b)+UNION(\\b)+\\s+SELECT(\\b)+\\s.*(\\b)(.*)", Pattern.CASE_INSENSITIVE));
    }
}