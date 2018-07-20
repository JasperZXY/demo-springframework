package org.ruanwei.demo.springframework.core.ioc.databinding.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FamilyNameConstraintValidator3 implements ConstraintValidator<FamilyName3, String> {
	private static Log log = LogFactory.getLog(FamilyNameConstraintValidator3.class);
	private String familyName;

	public void initialize(FamilyName3 constraintAnnotation) {
		log.info("initialize(FamilyName2 constraintAnnotation)" + constraintAnnotation);
		this.familyName = constraintAnnotation.value();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		log.info("isValid(String value, ConstraintValidatorContext context)" + value + context);
		// context.buildConstraintViolationWithTemplate("字符串不能为空").addConstraintViolation();
		return value != null && (value.startsWith(this.familyName) || value.startsWith("li"));
	}
}
