/*
 * pragmatickm-contact-taglib - Contacts nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2013, 2014, 2015, 2016  AO Industries, Inc.
 *     support@aoindustries.com
 *     7262 Bull Pen Cir
 *     Mobile, AL 36695
 *
 * This file is part of pragmatickm-contact-taglib.
 *
 * pragmatickm-contact-taglib is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * pragmatickm-contact-taglib is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with pragmatickm-contact-taglib.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.pragmatickm.contact.taglib;

import com.pragmatickm.contact.model.Address;
import com.pragmatickm.contact.model.AddressType;
import com.pragmatickm.contact.model.Contact;
import com.semanticcms.core.model.Node;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.servlet.CurrentNode;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class AddressTag extends SimpleTagSupport {

	private AddressType type;
    public void setType(String type) {
		this.type = AddressType.valueOf(type.toUpperCase(Locale.ROOT));
    }

	private String address1;
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	private String address2;
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	private String city;
	public void setCity(String city) {
		this.city = city;
	}

	private String stateProv;
	public void setStateProv(String stateProv) {
		this.stateProv = stateProv;
	}

	private String zipPostal;
	public void setZipPostal(String zipPostal) {
		this.zipPostal = zipPostal;
	}

	private String country;
	public void setCountry(String country) {
		this.country = country;
	}

	private String comment;
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
    public void doTag() throws JspException, IOException {
		final PageContext pageContext = (PageContext)getJspContext();
		final ServletRequest request = pageContext.getRequest();

		// Get the current capture state
		final CaptureLevel captureLevel = CaptureLevel.getCaptureLevel(request);
		if(captureLevel.compareTo(CaptureLevel.META) >= 0) {
			final Node currentNode = CurrentNode.getCurrentNode(request);
			if(!(currentNode instanceof Contact)) throw new JspTagException("<address> must be nested in <contact>");
			final Contact currentContact = (Contact)currentNode;

			currentContact.addAddress(
				new Address(
					type,
					address1,
					address2,
					city,
					stateProv,
					zipPostal,
					country,
					comment
				)
			);
		}
	}
}
