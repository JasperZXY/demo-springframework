package org.ruanwei.demo.springframework.core.ioc.extension;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.core.ioc.AbsHouse2;
import org.ruanwei.demo.springframework.core.ioc.Family2;
import org.ruanwei.demo.springframework.core.ioc.People2;
import org.ruanwei.demo.springframework.core.ioc.databinding.validation.PeopleSpringValidator2;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

@Order(Ordered.LOWEST_PRECEDENCE)
@Component
public class MyBeanPostProcessor2 implements BeanPostProcessor {
	private static Log log = LogFactory.getLog(MyBeanPostProcessor2.class);

	@Autowired
	private javax.validation.Validator validator;

	@Autowired
	private javax.validation.ValidatorFactory validatorFactory;

	@Autowired
	private org.springframework.validation.Validator springValidator;
	
	@Autowired
	private MessageSource messageSource;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		log.debug("postProcessBeforeInitialization(Object bean, String beanName) " + beanName + "=" + bean);
		if (bean instanceof People2) {
			People2 people = (People2) bean;
			log.info("postProcessBeforeInitialization====================" + people);

			validate(people);
			validateBySpring(people, new PeopleSpringValidator2());
		} else if (bean instanceof Family2) {
			Family2 family = (Family2) bean;
			log.info("postProcessBeforeInitialization====================" + family);

			// validate(family);
			// validateBySpring(family);
		} else if (bean instanceof AbsHouse2) {
			AbsHouse2 house = (AbsHouse2) bean;
			log.info("postProcessBeforeInitialization====================" + house);

			// validate(house);
			// validateBySpring(house);
		}
		return bean;
	}

	private <T> void validate(T t) {
//		Validator validator = Validation.byProvider(HibernateValidator.class).configure()
//		.messageInterpolator(
//				new ResourceBundleMessageInterpolator(new PlatformResourceBundleLocator("message/validate")))
//		.failFast(false).buildValidatorFactory().getValidator();

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
		if (bean instanceof People2) {
			People2 people = (People2) bean;
			log.info("postProcessAfterInitialization====================" + people);

			// ===========以下挪到AOP===============
			/*
			 * Method method = null; try { method =
			 * People2.class.getMethod("sayHello", String.class); } catch
			 * (NoSuchMethodException | SecurityException e) {
			 * e.printStackTrace(); }
			 * 
			 * ExecutableValidator executableValidator =
			 * validator.forExecutables(); Set<ConstraintViolation<People2>>
			 * constraintViolations =
			 * executableValidator.validateParameters(people, method, new
			 * Object[] { "" }); for (ConstraintViolation<People2> violation :
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
			 * Object[] { null }); for (ConstraintViolation<People2> violation :
			 * constraintViolations) { log.info("validation3==========" +
			 * violation.getMessage()); }
			 */
			// ===========以上挪到AOP===============
		} else if (bean instanceof Family2) {
			Family2 family = (Family2) bean;
			log.info("postProcessAfterInitialization====================" + family);
		} else if (bean instanceof AbsHouse2) {
			AbsHouse2 house = (AbsHouse2) bean;
			log.info("postProcessAfterInitialization====================" + house);
		}
		return bean;
	}

}
