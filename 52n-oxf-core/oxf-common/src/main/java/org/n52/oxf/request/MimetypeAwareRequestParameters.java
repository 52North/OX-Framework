/**
 * ﻿Copyright (C) 2012-2015 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License version 2 as publishedby the Free
 * Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of the
 * following licenses, the combination of the program with the linked library is
 * not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed under
 * the aforementioned licenses, is permitted by the copyright holders if the
 * distribution is compliant with both the GNU General Public License version 2
 * and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 */
package org.n52.oxf.request;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.http.entity.ContentType;

/**
 * Assembles parameters for an encoded request: the mimetype build of
 * type and encoding.
 *
 * If not set, the default used is {@link ContentType#TEXT_XML}
 * (:= <code>text/xml; charset=ISO-8859-1</code>).
 */
public abstract class MimetypeAwareRequestParameters extends MultiValueRequestParameters {

	public static final String MIME_TYPE = "mimetypeType";
	public static final String CHARSET = "mimetypeCharset";

	public void setType(final String mimetype) {
		if (mimetype == null) {
			throw new IllegalArgumentException("Parameter 'mimetype' should not be null!");
		}
		addNonEmpty(MIME_TYPE, mimetype);
	}

	public void setCharset(final Charset charset) {
		if (charset == null) {
			throw new IllegalArgumentException("Parameter 'charset' should not be null!");
		}
		addNonEmpty(CHARSET, charset.toString());
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() || isEmpty(MIME_TYPE) || isEmpty(CHARSET);
	}

	public boolean isValid() {
		if (this.isEmpty()) {
			return false;
		}
		try {
			ContentType.create(getSingleValue(MIME_TYPE), getSingleValue(CHARSET));
		} catch(final UnsupportedCharsetException uce) {
			return false;
		}
		return true;
	}

	public boolean isSetMimetype() {
		return isValid();
	}

}
