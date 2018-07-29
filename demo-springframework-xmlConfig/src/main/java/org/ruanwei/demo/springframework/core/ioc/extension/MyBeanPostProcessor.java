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
import org.ruanwei.demo.springframework.core.ioc.databinding.validation.ValidationUtils;

public class MyBeanPostProcessor implements BeanPostProcessor,
		MessageSourceAware, PriorityOrdered {
	private static Log log = LogFactory.getLog(MyBeanPostProcessor.class);

	private int order = Ordered.LOWEST_PRECEDENCE;

	private javax.validation.Validator validator;
	private javax.validation.ValidatorFactory validatorFactory;
	private org.springframework.validation.Validator springValidator;

	private MessageSource messageSource;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		log.debug("postProcessBeforeInitialization(Object bean, String beanName) "
				+ beanName + "=" + bean);
		if (bean instanceof People) {
			People people = (People) bean;
			log.info("postProcessBeforeInitialization===================="
					+ people);

			ValidationUtils.validate1(people, validator);
			ValidationUtils.validate2(people, validatorFactory);
			ValidationUtils.validateBySpring(people, messageSource,
					springValidator, new PeopleSpringValidator());
		} else if (bean instanceof Family) {
			Family family = (Family) bean;
			log.info("postProcessBeforeInitialization===================="
					+ family);
		} else if (bean instanceof House) {
			House house = (House) bean;
			log.info("postProcessBeforeInitialization===================="
					+ house);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		log.debug("postProcessAfterInitialization(Object bean, String beanName)"
				+ beanName + "=" + bean);
		if (bean instanceof People) {
			People people = (People) bean;
			log.info("postProcessAfterInitialization===================="
					+ people);

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
			log.info("postProcessAfterInitialization===================="
					+ family);
		} else if (bean instanceof House) {
			House house = (House) bean;
			log.info("postProcessAfterInitialization===================="
					+ house);
		}
		return bean;
	}

	public void setValidator(Validator validator) {
		log.info("setValidator(Validator validator)" + validator);
		this.validator = validator;
	}

	public void setValidatorFactory(ValidatorFactory validatorFactory) {
		log.info("setValidatorFactory(ValidatorFactory validatorFactory)"
				+ validatorFactory);
		this.validatorFactory = validatorFactory;
	}

	public void setSpringValidator(
			org.springframework.validation.Validator validator) {
		log.info("setSpringValidator(org.springframework.validation.Validator)"
				+ validator);
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
