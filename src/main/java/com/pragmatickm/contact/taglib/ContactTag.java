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
import com.aoindustries.encoding.Coercion;
import static com.aoindustries.taglib.AttributeUtils.resolveValue;
import com.aoindustries.taglib.StyleAttribute;
import com.aoindustries.util.StringUtility;
import com.pragmatickm.contact.model.Contact;
import com.pragmatickm.contact.servlet.impl.ContactImpl;
import com.semanticcms.core.model.ElementContext;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.servlet.PageIndex;
import com.semanticcms.core.taglib.ElementTag;
import java.io.IOException;
import java.io.Writer;
import javax.el.ELContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

public class ContactTag extends ElementTag<Contact> implements StyleAttribute {

	private Object style;
	@Override
	public void setStyle(Object style) {
		this.style = style;
	}

	private Object title;
	public void setTitle(Object title) {
		this.title = title;
	}

	private Object first;
	public void setFirst(Object first) {
		this.first = first;
	}

	private Object middle;
	public void setMiddle(Object middle) {
		this.middle = middle;
	}

	private Object nick;
	public void setNick(Object nick) {
		this.nick = nick;
	}

	private Object last;
	public void setLast(Object last) {
		this.last = last;
	}

	private Object maiden;
	public void setMaiden(Object maiden) {
		this.maiden = maiden;
	}

	private Object suffix;
	public void setSuffix(Object suffix) {
		this.suffix = suffix;
	}

	private Object company;
	public void setCompany(Object company) {
		this.company = company;
	}

	private Object department;
	public void setDepartment(Object department) {
		this.department = department;
	}

	private Object jobTitle;
	public void setJobTitle(Object jobTitle) {
		this.jobTitle = jobTitle;
	}

	private Object email;
    public void setEmail(Object email) {
		this.email = email;
    }

	private Object webPage;
    public void setWebPage(Object webPage) {
		this.webPage = webPage;
    }

	@Override
	protected Contact createElement() {
		return new Contact();
	}

	@Override
	protected void evaluateAttributes(Contact element, ELContext elContext) throws JspTagException, IOException {
		try {
			super.evaluateAttributes(element, elContext);
			style = Coercion.nullIfEmpty(resolveValue(style, Object.class, elContext));
			element.setTitle(resolveValue(title, String.class, elContext));
			element.setFirst(resolveValue(first, String.class, elContext));
			element.setMiddle(resolveValue(middle, String.class, elContext));
			element.setNick(resolveValue(nick, String.class, elContext));
			element.setLast(resolveValue(last, String.class, elContext));
			element.setMaiden(resolveValue(maiden, String.class, elContext));
			element.setSuffix(resolveValue(suffix, String.class, elContext));
			element.setCompany(resolveValue(company, String.class, elContext));
			element.setDepartment(resolveValue(department, String.class, elContext));
			element.setJobTitle(resolveValue(jobTitle, String.class, elContext));
			String emailStr = StringUtility.nullIfEmpty(resolveValue(email, String.class, elContext));
			if(emailStr != null) element.addEmail(Email.valueOf(emailStr));
			String webPageStr = StringUtility.nullIfEmpty(resolveValue(webPage, String.class, elContext));
			if(webPageStr != null) element.addWebPage(webPageStr);
		} catch(ValidationException e) {
			throw new JspTagException(e);
		}
	}

	private PageIndex pageIndex;
	@Override
	protected void doBody(Contact contact, CaptureLevel captureLevel) throws JspException, IOException {
		final PageContext pageContext = (PageContext)getJspContext();
		pageIndex = PageIndex.getCurrentPageIndex(pageContext.getRequest());
		super.doBody(contact, captureLevel);
	}

	@Override
	public void writeTo(Writer out, ElementContext context) throws IOException {
		ContactImpl.writeContactTable(pageIndex, out, context, style, getElement());
	}
}
