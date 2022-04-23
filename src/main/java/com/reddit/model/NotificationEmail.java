package com.reddit.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEmail {
    private String subject;
    private String recipient;
    private String body;
    
    
//	public NotificationEmail() {
//		
//	}
//	
//	public NotificationEmail(String subject, String recipient, String body) {
//		super();
//		this.subject = subject;
//		this.recipient = recipient;
//		this.body = body;
//	}
//	/**
//	 * @return the subject
//	 */
//	public String getSubject() {
//		return subject;
//	}
//	/**
//	 * @param subject the subject to set
//	 */
//	public void setSubject(String subject) {
//		this.subject = subject;
//	}
//	/**
//	 * @return the recipient
//	 */
//	public String getRecipient() {
//		return recipient;
//	}
//	/**
//	 * @param recipient the recipient to set
//	 */
//	public void setRecipient(String recipient) {
//		this.recipient = recipient;
//	}
//	/**
//	 * @return the body
//	 */
//	public String getBody() {
//		return body;
//	}
//	/**
//	 * @param body the body to set
//	 */
//	public void setBody(String body) {
//		this.body = body;
//	}
    
    
    
}