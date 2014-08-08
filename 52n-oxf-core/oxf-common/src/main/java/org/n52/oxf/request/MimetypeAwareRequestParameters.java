/**
 * Copyright (C) 2014
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
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
