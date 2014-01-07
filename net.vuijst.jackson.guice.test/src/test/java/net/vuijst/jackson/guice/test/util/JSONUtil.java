/**
 * ---Begin Copyright Notice--- Oct 5, 2012 11:53:41 AM
 *
 * NOTICE
 *
 * THIS SOFTWARE IS THE PROPERTY OF AND CONTAINS CONFIDENTIAL INFORMATION OF
 * INFOR AND/OR ITS AFFILIATES OR SUBSIDIARIES AND SHALL NOT BE DISCLOSED
 * WITHOUT PRIOR WRITTEN PERMISSION. LICENSED CUSTOMERS MAY COPY AND ADAPT
 * THIS SOFTWARE FOR THEIR OWN USE IN ACCORDANCE WITH THE TERMS OF THEIR
 * SOFTWARE LICENSE AGREEMENT. ALL OTHER RIGHTS RESERVED.
 *
 * (c) COPYRIGHT 2012 INFOR. ALL RIGHTS RESERVED. THE WORD AND DESIGN MARKS
 * SET FORTH HEREIN ARE TRADEMARKS AND/OR REGISTERED TRADEMARKS OF INFOR
 * AND/OR ITS AFFILIATES AND SUBSIDIARIES. ALL RIGHTS RESERVED. ALL OTHER
 * TRADEMARKS LISTED HEREIN ARE THE PROPERTY OF THEIR RESPECTIVE OWNERS.
 *
 * ---End Copyright Notice---
 */
package net.vuijst.jackson.guice.test.util;

import net.vuijst.jackson.guice.test.model.IAddress;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class JSONUtil.
 */
public final class JSONUtil {

	/**
	 * Instantiates a new jSON util.
	 */
	private JSONUtil() {

	}

	/**
	 * Marshal object to json string.
	 * 
	 * @param object
	 *            the object
	 * @return the string
	 */
	public static synchronized String marshalToJSON(final Object object) {
		final ObjectMapper mapper = new ObjectMapper();
		String jsonString = null;
		try {
			jsonString = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(object);
		} catch (Exception e) {
			System.err.println("Cannot marshall object to JSON string, reason: " + e.toString());
		}
		return jsonString;
	}

	/**
	 * Unmarshal from json.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param jsonString
	 *            the json string
	 * @param type
	 *            the type
	 * @return the t
	 */
	public static synchronized <T> T unmarshalFromJSON(final String jsonString, final Class<T> type) {

		if (jsonString == null) {
			System.err.println("Cannot unmarshall from empty JSON string");
			return null;
		}

		final ObjectMapper mapper = new ObjectMapper();

		// See: http://markmail.org/message/buv6asokwbkw6rjf
		// For omitting a JsonMappingException during unmarshalling, @JacksonInject annotations
		// require to be filled by default.
		mapper.setInjectableValues(new InjectableValues.Std().addValue(IAddress.class, null));

		T object = null;
		try {
			object = mapper.readValue(jsonString, type);
		} catch (Exception e) {
			System.err.println("Cannot unmarshall object from JSON string, reason: " + e.toString());
		}

		return object;
	}

}
