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

import com.aoindustries.aoserv.client.validator.Email;
import com.aoindustries.aoserv.client.validator.ValidationException;
import com.pragmatickm.contact.model.Contact;
import com.pragmatickm.contact.servlet.impl.ContactImpl;
import com.semanticcms.core.model.ElementContext;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.servlet.PageIndex;
import com.semanticcms.core.taglib.ElementTag;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class ContactTag extends ElementTag<Contact> {

	public ContactTag() {
		super(new Contact());
	}

	private String style;
	public void setStyle(String style) {
		this.style = style;
	}

	public void setTitle(String title) {
		element.setTitle(title);
	}

	public void setFirst(String first) {
		element.setFirst(first);
	}

	public void setMiddle(String middle) {
		element.setMiddle(middle);
	}

	public void setNick(String nick) {
		element.setNick(nick);
	}

	public void setLast(String last) {
		element.setLast(last);
	}

	public void setMaiden(String maiden) {
		element.setMaiden(maiden);
	}

	public void setSuffix(String suffix) {
		element.setSuffix(suffix);
	}

	public void setCompany(String company) {
		element.setCompany(company);
	}

	public void setDepartment(String department) {
		element.setDepartment(department);
	}

	public void setJobTitle(String jobTitle) {
		element.setJobTitle(jobTitle);
	}

    public void setEmail(String address) throws ValidationException {
		element.addEmail(Email.valueOf(address));
    }

    public void setWebPage(String webPage) throws ValidationException {
		element.addWebPage(webPage);
    }

	private PageIndex pageIndex;
	@Override
	protected void doBody(CaptureLevel captureLevel) throws JspException, IOException {
		final PageContext pageContext = (PageContext)getJspContext();
		pageIndex = PageIndex.getCurrentPageIndex(pageContext.getRequest());
		super.doBody(captureLevel);
	}

	@Override
	public void writeTo(Writer out, ElementContext context) throws IOException {
		ContactImpl.writeContactTable(pageIndex, out, context, style, element);
	}
}
