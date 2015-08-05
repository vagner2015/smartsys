/**
 * This document is a part of the source code and related artifacts
 * for Unilims, a restricted laboratorial system.
 *
 * http://www.unicorp.com.br
 *
 * Copyright © 2014 Unicorp Informática Industrial - All Rights Reserved
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 */

package br.com.vagner.apirote.util;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Object for http responses encapsulation
 * @author DevTeam
 *
 */
@XmlRootElement(name="response")
@XmlAccessorType(XmlAccessType.FIELD)
public class HttpResponse implements Serializable{

	private static final long serialVersionUID = -5023900654918295556L;
	
	@XmlElement(name="message",required=false,defaultValue="nothing")
	private String message;
	
	public HttpResponse() {
		
	}

	public HttpResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
