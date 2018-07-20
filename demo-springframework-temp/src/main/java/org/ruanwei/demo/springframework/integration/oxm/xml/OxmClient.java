package org.ruanwei.demo.springframework.integration.oxm.xml;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ruanwei.demo.springframework.integration.User;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;

public class OxmClient {
	private static Log log = LogFactory.getLog(OxmClient.class);
	
	// 具体用法参考Spring中关于OXM的部分JAXB/Castor/XMLBeans/JiBX/XStream
	private Marshaller marshaller;

	private Unmarshaller unmarshaller;

	public void saveSettings(User user) {
		log.info("saveSettings(User user)"+user);
		
		try (FileOutputStream os = new FileOutputStream("user.xml")) {
			this.marshaller.marshal(user, new StreamResult(os));
		} catch (XmlMappingException | IOException e) {
			e.printStackTrace();
		}
	}

	public User loadSettings() {
		log.info("loadSettings()");
		
		User user = null;
		try (FileInputStream is = new FileInputStream("user.xml")) {
			user = (User) this.unmarshaller.unmarshal(new StreamSource(is));

		} catch (XmlMappingException | IOException e) {
			e.printStackTrace();
		}
		return user;
	}

	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}

	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}
}
