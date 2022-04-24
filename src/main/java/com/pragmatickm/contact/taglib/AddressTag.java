/*
 * pragmatickm-contact-taglib - Contacts nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2013, 2014, 2015, 2016, 2017, 2021, 2022  AO Industries, Inc.
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
 * along with pragmatickm-contact-taglib.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.pragmatickm.contact.taglib;

import static com.aoapps.taglib.AttributeUtils.resolveValue;
import com.pragmatickm.contact.model.Address;
import com.pragmatickm.contact.model.AddressType;
import com.pragmatickm.contact.model.Contact;
import com.semanticcms.core.model.Node;
import com.semanticcms.core.pages.CaptureLevel;
import com.semanticcms.core.pages.local.CurrentCaptureLevel;
import com.semanticcms.core.pages.local.CurrentNode;
import java.io.IOException;
import java.util.Locale;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class AddressTag extends SimpleTagSupport {

  private ValueExpression type;

  public void setType(ValueExpression type) {
    this.type = type;
  }

  private ValueExpression address1;

  public void setAddress1(ValueExpression address1) {
    this.address1 = address1;
  }

  private ValueExpression address2;

  public void setAddress2(ValueExpression address2) {
    this.address2 = address2;
  }

  private ValueExpression city;

  public void setCity(ValueExpression city) {
    this.city = city;
  }

  private ValueExpression stateProv;

  public void setStateProv(ValueExpression stateProv) {
    this.stateProv = stateProv;
  }

  private ValueExpression zipPostal;

  public void setZipPostal(ValueExpression zipPostal) {
    this.zipPostal = zipPostal;
  }

  private ValueExpression country;

  public void setCountry(ValueExpression country) {
    this.country = country;
  }

  private ValueExpression comment;

  public void setComment(ValueExpression comment) {
    this.comment = comment;
  }

  @Override
  public void doTag() throws JspException, IOException {
    final PageContext pageContext = (PageContext) getJspContext();
    final ServletRequest request = pageContext.getRequest();

    // Get the current capture state
    final CaptureLevel captureLevel = CurrentCaptureLevel.getCaptureLevel(request);
    if (captureLevel.compareTo(CaptureLevel.META) >= 0) {
      final Node currentNode = CurrentNode.getCurrentNode(request);
      if (!(currentNode instanceof Contact)) {
        throw new JspTagException("<address> must be nested in <contact>");
      }
      final Contact currentContact = (Contact) currentNode;

      // Evaluate expressions
      ELContext elContext = pageContext.getELContext();
      AddressType typeObj = AddressType.valueOf(
          resolveValue(type, String.class, elContext)
              .toUpperCase(Locale.ROOT)
      );

      currentContact.addAddress(
          new Address(
              typeObj,
              resolveValue(address1, String.class, elContext),
              resolveValue(address2, String.class, elContext),
              resolveValue(city, String.class, elContext),
              resolveValue(stateProv, String.class, elContext),
              resolveValue(zipPostal, String.class, elContext),
              resolveValue(country, String.class, elContext),
              resolveValue(comment, String.class, elContext)
          )
      );
    }
  }
}
