/*
 * pragmatickm-contact-taglib - Contacts nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2013, 2014, 2015, 2016, 2017, 2020, 2021  AO Industries, Inc.
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

import com.aoindustries.encoding.Doctype;
import com.aoindustries.encoding.Serialization;
import com.aoindustries.encoding.servlet.DoctypeEE;
import com.aoindustries.encoding.servlet.SerializationEE;
import com.aoindustries.html.Document;
import com.aoindustries.lang.Coercion;
import com.aoindustries.lang.Strings;
import com.aoindustries.net.Email;
import static com.aoindustries.taglib.AttributeUtils.resolveValue;
import com.aoindustries.validation.ValidationException;
import com.pragmatickm.contact.model.Contact;
import com.pragmatickm.contact.renderer.html.ContactHtmlRenderer;
import com.semanticcms.core.model.ElementContext;
import com.semanticcms.core.pages.CaptureLevel;
import com.semanticcms.core.renderer.html.PageIndex;
import com.semanticcms.core.taglib.ElementTag;
import java.io.IOException;
import java.io.Writer;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;

public class ContactTag extends ElementTag<Contact> /*implements StyleAttribute*/ {

	private ValueExpression style;
	public void setStyle(ValueExpression style) {
		this.style = style;
	}

	private ValueExpression title;
	public void setTitle(ValueExpression title) {
		this.title = title;
	}

	private ValueExpression first;
	public void setFirst(ValueExpression first) {
		this.first = first;
	}

	private ValueExpression middle;
	public void setMiddle(ValueExpression middle) {
		this.middle = middle;
	}

	private ValueExpression nick;
	public void setNick(ValueExpression nick) {
		this.nick = nick;
	}

	private ValueExpression last;
	public void setLast(ValueExpression last) {
		this.last = last;
	}

	private ValueExpression maiden;
	public void setMaiden(ValueExpression maiden) {
		this.maiden = maiden;
	}

	private ValueExpression suffix;
	public void setSuffix(ValueExpression suffix) {
		this.suffix = suffix;
	}

	private ValueExpression company;
	public void setCompany(ValueExpression company) {
		this.company = company;
	}

	private ValueExpression department;
	public void setDepartment(ValueExpression department) {
		this.department = department;
	}

	private ValueExpression jobTitle;
	public void setJobTitle(ValueExpression jobTitle) {
		this.jobTitle = jobTitle;
	}

	private ValueExpression email;
    public void setEmail(ValueExpression email) {
		this.email = email;
    }

	private ValueExpression webPage;
    public void setWebPage(ValueExpression webPage) {
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
			String emailStr = Strings.nullIfEmpty(resolveValue(email, String.class, elContext));
			if(emailStr != null) element.addEmail(Email.valueOf(emailStr));
			String webPageStr = Strings.nullIfEmpty(resolveValue(webPage, String.class, elContext));
			if(webPageStr != null) element.addWebPage(webPageStr);
		} catch(ValidationException e) {
			throw new JspTagException(e);
		}
	}

	private PageIndex pageIndex;
	private Object styleObj;
	private Serialization serialization;
	private Doctype doctype;

	@Override
	protected void doBody(Contact contact, CaptureLevel captureLevel) throws JspException, IOException {
		final PageContext pageContext = (PageContext)getJspContext();
		if(captureLevel == CaptureLevel.BODY) {
			ServletContext servletContext = pageContext.getServletContext();
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			pageIndex = PageIndex.getCurrentPageIndex(pageContext.getRequest());
			styleObj = Coercion.nullIfEmpty(resolveValue(style, Object.class, pageContext.getELContext()));
			serialization = SerializationEE.get(servletContext, request);
			doctype = DoctypeEE.get(servletContext, request);
		}
		super.doBody(contact, captureLevel);
	}

	@Override
	public void writeTo(Writer out, ElementContext context) throws IOException {
		Document document = new Document(serialization, doctype, out);
		document.setIndent(false); // Do not add extra indentation to JSP
		ContactHtmlRenderer.writeContactTable(pageIndex, document, context, styleObj, getElement());
	}
}
