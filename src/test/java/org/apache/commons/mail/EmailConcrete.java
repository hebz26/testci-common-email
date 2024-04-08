package org.apache.commons.mail;

import java.util.Map;

import javax.mail.internet.MimeMessage;

public class EmailConcrete extends Email {
	
	@Override
	public Email setMsg(String msg) throws EmailException {
		return null;
	}
	
	public Map<String, String> getHeaders(){
		return this.headers;
	}
	public String getContentType(){
		return this.contentType;
	}

}
