package org.ruanwei.demo.springframework.core.ioc.databinding.validation;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class ValidationUtils {
	private static Log log = LogFactory.getLog(ValidationUtils.class);

	public static <T> void validate1(T t, Validator validator) {
		// Validator validator =
		// Validation.byProvider(HibernateValidator.class).configure()
		// .messageInterpolator(
		// new ResourceBundleMessageInterpolator(new
		// PlatformResourceBundleLocator("message/validate")))
		// .failFast(false).buildValidatorFactory().getValidator();
		if (validator == null) {
			validator = Validation.buildDefaultValidatorFactory()
					.getValidator();
		}
		Set<ConstraintViolation<T>> constraintViolations = validator
				.validate(t);
		for (ConstraintViolation<T> violation : constraintViolations) {
			log.info("validation1==========" + violation.getMessage());
		}
	}

	public static <T> void validate2(T t, ValidatorFactory validatorFactory) {
		// Validator validator =
		// Validation.byProvider(HibernateValidator.class).configure()
		// .messageInterpolator(
		// new ResourceBundleMessageInterpolator(new
		// PlatformResourceBundleLocator("message/validate")))
		// .failFast(false).buildValidatorFactory().getValidator();

		if (validatorFactory == null) {
			validatorFactory = Validation.buildDefaultValidatorFactory();
		}
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<T>> constraintViolations = validator
				.validate(t);
		for (ConstraintViolation<T> violation : constraintViolations) {
			log.info("validation2==========" + violation.getMessage());
		}
	}

	public static void validateBySpring(Object target,
			MessageSource messageSource,
			org.springframework.validation.Validator... validators) {
		DataBinder dataBinder = new DataBinder(target);
		dataBinder.addValidators(validators);
		dataBinder.validate();

		BindingResult bindingResult = dataBinder.getBindingResult();
		if (bindingResult.hasGlobalErrors()) {
			for (ObjectError error : bindingResult.getGlobalErrors()) {
				log.info("globalError==========" + error);
			}
		}

		if (bindingResult.hasFieldErrors()) {
			for (FieldError error : bindingResult.getFieldErrors()) {
				log.info("fieldError==========" + error);
				log.info("object==========" + error.getObjectName());
				log.info("field==========" + error.getField());
				log.info("rejectedValue==========" + error.getRejectedValue());

				log.info("code==========" + error.getCode());
				for (Object arg : error.getArguments()) {
					log.info("argument==========" + arg);
				}
				log.info("defaultMessage==========" + error.getDefaultMessage());
				String message = messageSource.getMessage(error.getCode(),
						error.getArguments(), error.getDefaultMessage(),
						Locale.US);
				log.info("message==========" + message);
			}
		}
	}

	public static void validateBySpring2(Object target,
			MessageSource messageSource,
			org.springframework.validation.Validator validator) {

		Errors errors =  new BeanPropertyBindingResult(target,
				"People");;
		org.springframework.validation.ValidationUtils.invokeValidator(
				validator, target, errors);
	}
}
