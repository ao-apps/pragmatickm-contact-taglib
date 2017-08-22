/*
 * pragmatickm-contact-taglib - Contacts nested within SemanticCMS pages and elements in a JSP environment.
 * Copyright (C) 2017  AO Industries, Inc.
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
package com.pragmatickm.contact.taglib.book;

import com.aoindustries.net.DomainName;
import com.aoindustries.net.Path;
import com.aoindustries.validation.ValidationException;
import com.semanticcms.core.model.BookRef;
import com.semanticcms.core.model.ResourceRef;
import com.semanticcms.tagreference.TagReferenceInitializer;
import java.util.Collections;

/**
 * @author  AO Industries, Inc.
 */
public class PragmaticKmContactTldInitializer extends TagReferenceInitializer {

	public PragmaticKmContactTldInitializer() throws ValidationException {
		super(
			"Contact Taglib Reference",
			"Taglib Reference",
			new ResourceRef(
				new BookRef(
					DomainName.valueOf("pragmatickm.com"),
					Path.valueOf("/contact/taglib")
				),
				Path.valueOf("/pragmatickm-contact.tld")
			),
			Maven.properties.getProperty("javac.link.javaApi.jdk16"),
			Maven.properties.getProperty("javac.link.javaeeApi.6"),
			Collections.singletonMap("com.pragmatickm.contact.taglib.", Maven.properties.getProperty("documented.url") + "apidocs/")
		);
	}
}
