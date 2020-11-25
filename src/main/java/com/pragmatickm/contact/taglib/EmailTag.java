/*
 * pragmatickm-contact-taglib - Contacts nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2013, 2014, 2015, 2016, 2017, 2020  AO Industries, Inc.
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
import static com.aoindustries.encoding.TextInXhtmlAttributeEncoder.encodeTextInXhtmlAttribute;
import com.aoindustries.encoding.servlet.DoctypeEE;
import com.aoindustries.encoding.servlet.SerializationEE;
import com.aoindustries.html.Html;
import com.aoindustries.net.Email;
import static com.aoindustries.taglib.AttributeUtils.resolveValue;
import com.aoindustries.validation.ValidationException;
import com.pragmatickm.contact.model.Contact;
import com.semanticcms.core.model.ElementContext;
import com.semanticcms.core.model.ElementWriter;
import com.semanticcms.core.model.Node;
import com.semanticcms.core.model.NodeBodyWriter;
import com.semanticcms.core.model.Page;
import com.semanticcms.core.servlet.CaptureLevel;
import com.semanticcms.core.servlet.CurrentNode;
import com.semanticcms.core.servlet.CurrentPage;
import com.semanticcms.core.taglib.PageElementContext;
import java.io.IOException;
import java.io.Writer;
import javax.el.ValueExpression;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class EmailTag extends SimpleTagSupport implements ElementWriter {

	private ValueExpression address;
    public void setAddress(ValueExpression address) {
		this.address = address;
    }

	private Email email;
	private Serialization serialization;
	private Doctype doctype;

	@Override
    public void doTag() throws JspException, IOException {
		final PageContext pageContext = (PageContext)getJspContext();
		final HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

		// Get the current capture state
		final CaptureLevel captureLevel = CaptureLevel.getCaptureLevel(request);
		if(captureLevel.compareTo(CaptureLevel.META) >= 0) {
			// Evaluate expressions
			try {
				email = Email.valueOf(resolveValue(address, String.class, pageContext.getELContext()));
			} catch(ValidationException e) {
				throw new JspTagException(e);
			}

			Node node = CurrentNode.getCurrentNode(request);
			if(node instanceof Contact) {
				Contact currentContact = (Contact)node;
				currentContact.addEmail(email);
			} else {
				ServletContext servletContext = pageContext.getServletContext();
				serialization = SerializationEE.get(servletContext, request);
				doctype = DoctypeEE.get(servletContext, request);

				JspWriter out = pageContext.getOut();
				if(node == null) {
					// Write now
					if(captureLevel == CaptureLevel.BODY) writeTo(out, new PageElementContext(pageContext));
				} else {
					// Write an element marker instead
					Contact contact = new Contact();
					contact.addEmail(email);
					// Find the optional parent page
					Page currentPage = CurrentPage.getCurrentPage(request);
					if(currentPage != null) {
						currentPage.addElement(contact);
					} else {
						// Note: Page freezes all of its elements
						contact.freeze();
					}
					// Add as a child element
					NodeBodyWriter.writeElementMarker(
						node.addChildElement(
							contact,
							this
						),
						out
					);
				}
			}
		}
	}

	@Override
	public void writeTo(Writer out, ElementContext context) throws IOException {
		Html html = new Html(serialization, doctype, out);
		html.out.write("<span class=\"pragmatickm-contact-email\"><a href=\"mailto:");
		String emailString = email.toString();
		encodeTextInXhtmlAttribute(emailString, html.out);
		html.out.write("\">");
		html.text(emailString);
		html.out.write("</a></span>");
	}
}
