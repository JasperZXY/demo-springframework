package org.ruanwei.demo.springframework.core.ioc.extension;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.Family;
import org.ruanwei.demo.springframework.core.ioc.House;
import org.ruanwei.demo.springframework.core.ioc.People;
import org.ruanwei.demo.springframework.core.ioc.databinding.validation.PeopleSpringValidator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public class MyBeanPostProcessor implements BeanPostProcessor, MessageSourceAware, PriorityOrdered {
	private static Log log = LogFactory.getLog(MyBeanPostProcessor.class);

	private int order = Ordered.LOWEST_PRECEDENCE;

	private javax.validation.Validator validator;
	private javax.validation.ValidatorFactory validatorFactory;
	private org.springframework.validation.Validator springValidator;

	private MessageSource messageSource;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		log.debug("postProcessBeforeInitialization(Object bean, String beanName) " + beanName + "=" + bean);
		if (bean instanceof People) {
			People people = (People) bean;
			log.info("postProcessBeforeInitialization====================" + people);

			validate(people);
			validateBySpring(people, new PeopleSpringValidator());
		} else if (bean instanceof Family) {
			Family family = (Family) bean;
			log.info("postProcessBeforeInitialization====================" + family);

			// validate(family);
			// validateBySpring(family);
		} else if (bean instanceof House) {
			House house = (House) bean;
			log.info("postProcessBeforeInitialization====================" + house);

			// validate(house);
			// validateBySpring(house);
		}
		return bean;
	}

	private <T> void validate(T t) {
		// Validator validator =
		// Validation.byProvider(HibernateValidator.class).configure()
		// .messageInterpolator(
		// new ResourceBundleMessageInterpolator(new
		// PlatformResourceBundleLocator("message/validate")))
		// .failFast(false).buildValidatorFactory().getValidator();
		if (this.validator == null) {
			this.validator = Validation.buildDefaultValidatorFactory().getValidator();
		}
		
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(t);
		for (ConstraintViolation<T> violation : constraintViolations) {
			log.info("validation1==========" + violation.getMessage());
		}

		Validator validator2 = validatorFactory.getValidator();
		constraintViolations = validator2.validate(t);
		for (ConstraintViolation<T> violation : constraintViolations) {
			log.info("validation2==========" + violation.getMessage());
		}
	}

	private void validateBySpring(Object target, org.springframework.validation.Validator... validators) {
		DataBinder dataBinder = new DataBinder(target);
		dataBinder.setValidator(springValidator);
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
				String message = messageSource.getMessage(error.getCode(), error.getArguments(),
						error.getDefaultMessage(), Locale.US);
				log.info("message==========" + message);
			}
		}
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		log.debug("postProcessAfterInitialization(Object bean, String beanName)" + beanName + "=" + bean);
		if (bean instanceof People) {
			People people = (People) bean;
			log.info("postProcessAfterInitialization====================" + people);

			// ===========以下挪到AOP===============
			/*
			 * Method method = null; try { method =
			 * People.class.getMethod("sayHello", String.class); } catch
			 * (NoSuchMethodException | SecurityException e) {
			 * e.printStackTrace(); }
			 * 
			 * ExecutableValidator executableValidator =
			 * validator.forExecutables(); Set<ConstraintViolation<People>>
			 * constraintViolations =
			 * executableValidator.validateParameters(people, method, new
			 * Object[] { "" }); for (ConstraintViolation<People> violation :
			 * constraintViolations) { log.info("validation2==========" +
			 * violation.getMessage()); Path path = violation.getPropertyPath();
			 * Iterator<Node> nodes = path.iterator(); MethodNode methodNode =
			 * nodes.next().as(MethodNode.class);
			 * log.info("methodName==========" + methodNode.getName()); while
			 * (nodes.hasNext()) { ParameterNode parameterNode =
			 * nodes.next().as(ParameterNode.class);
			 * log.info("parameterName==========" + parameterNode.getName()); }
			 * }
			 * 
			 * constraintViolations =
			 * executableValidator.validateReturnValue(people, method, new
			 * Object[] { null }); for (ConstraintViolation<People> violation :
			 * constraintViolations) { log.info("validation3==========" +
			 * violation.getMessage()); }
			 */
			// ===========以上挪到AOP===============
		} else if (bean instanceof Family) {
			Family family = (Family) bean;
			log.info("postProcessAfterInitialization====================" + family);
		} else if (bean instanceof House) {
			House house = (House) bean;
			log.info("postProcessAfterInitialization====================" + house);
		}
		return bean;
	}

	public void setValidator(Validator validator) {
		log.info("setValidator(Validator validator)" + validator);
		this.validator = validator;
	}

	public void setValidatorFactory(ValidatorFactory validatorFactory) {
		log.info("setValidatorFactory(ValidatorFactory validatorFactory)" + validatorFactory);
		this.validatorFactory = validatorFactory;
	}

	public void setSpringValidator(org.springframework.validation.Validator validator) {
		log.info("setSpringValidator(org.springframework.validation.Validator)" + validator);
		this.springValidator = validator;
	}

	@Override
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

}
