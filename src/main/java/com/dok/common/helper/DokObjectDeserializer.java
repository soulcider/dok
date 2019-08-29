package com.dok.common.helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

@SuppressWarnings("deprecation")
public class DokObjectDeserializer extends UntypedObjectDeserializer {

		/** serialVersionUID */
    private static final long serialVersionUID = 1L;

		@Override
		protected Object mapObject(JsonParser parser, DeserializationContext context) throws IOException {
			// Read the first key.
			String firstKey;
			JsonToken token = parser.getCurrentToken();
			if (token == JsonToken.START_OBJECT) {
				firstKey = parser.nextFieldName();
			} else if (token == JsonToken.FIELD_NAME) {
				firstKey = parser.getCurrentName();
			} else {
				if (token != JsonToken.END_OBJECT) {
					// throw context.mappingException(handledType(), parser.getCurrentToken());
					String msg = String.format("Cannot deserialize instance of %s out of %s token",
							ClassUtil.nameOf(handledType()), token);
					return JsonMappingException.from(parser, msg);
				}
				return Collections.emptyMap();
			}

			// Populate entries.
			String nextKey = firstKey;
			Map<String, Object> valueByKey = new LinkedHashMap<>();
			do {
				parser.nextToken();
				Object nextValue = deserialize(parser, context);
				if (valueByKey.containsKey(nextKey)) {
					// Key conflict? Combine existing and current entries into a list.
					Object existingValue = valueByKey.get(nextKey);
					if (existingValue instanceof List) {
						@SuppressWarnings("unchecked")
						List<Object> values = (List<Object>) existingValue;
						values.add(nextValue);
					} else {
						List<Object> values = new ArrayList<>();
						values.add(existingValue);
						values.add(nextValue);
						valueByKey.put(nextKey, values);
					}
				} else {
					// New key? Put into the map.
					valueByKey.put(nextKey, nextValue);
				}
			} while ((nextKey = parser.nextFieldName()) != null);

			// Ship back the collected entries.
			return valueByKey;
		} // end of mapObject

	}