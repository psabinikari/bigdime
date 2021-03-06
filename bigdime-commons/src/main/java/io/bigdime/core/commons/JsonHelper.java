/**
 * Copyright (C) 2015 Stubhub.
 */
package io.bigdime.core.commons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonNode;
import org.springframework.stereotype.Component;

/**
 * Helper class to parse the {@code JsonNode} to get needed properties.
 *
 * @author Neeraj Jain
 *
 */
@Component
public final class JsonHelper {
	/**
	 * If the node is present has the value as true,
	 * 
	 * @param node
	 * @param key
	 * @return true if the node is present has the value as true, false
	 *         otherwise.
	 */
	public boolean getBooleanProperty(final JsonNode node, final String key) {
		try {
			String stringValue = getRequiredStringProperty(node, key);
			return Boolean.valueOf(stringValue);
		} catch (IllegalArgumentException ex) {
			return false;
		}
	}

	public String getRequiredStringProperty(final JsonNode node, final String key) {
		final JsonNode childNode = getRequiredNode(node, key);
		if (childNode.isTextual()) {
			return childNode.getTextValue();
		}
		throw new IllegalArgumentException("no text node found with key=" + key);
	}

	public JsonNode getRequiredNode(final JsonNode node, final String key) {
		final JsonNode childNode = getOptionalNodeOrNull(node, key);
		if (childNode == null) {
			throw new IllegalArgumentException("no node found with key=" + key);
		}
		return childNode;
	}

	public JsonNode getOptionalNodeOrNull(final JsonNode node, final String key) {
		return node.get(key);
	}

	public JsonNode getRequiredArrayNode(final JsonNode node, final String key) {
		final JsonNode childNode = getRequiredNode(node, key);
		if (childNode.isArray()) {
			return childNode;
		}
		throw new IllegalArgumentException("no array node found with key=" + key);
	}

	/*
	 * 	@formatter:off
	 * {
	 *   "name" : "valueText",
	 *   "arrayKey" : [
	 *     "name" : "valueArray",
	 *     "path" : "/tmp"
	 *   ],
	 *   "nodeKey" : {
	 *     "name" : "valueNode",
	 *     "id" : "p1"
	 *   },
	 *   "simpleKey" : "simpleValue"
	 * }
	 * 	@formatter:on
	 *
	 *
	 */
	public Map<String, Object> getNodeTree(final JsonNode node) {
		Map<String, Object> propertyMap = new HashMap<>();
		final Iterator<Entry<String, JsonNode>> iter = node.getFields();

		while ((iter != null) && iter.hasNext()) {
			final Entry<String, JsonNode> entry = iter.next();

			if (entry.getValue().isTextual()) {
				propertyMap.put(entry.getKey(), entry.getValue().getTextValue());
			} else if (entry.getValue().isArray()) {
				Map<String, Object> innerMap = null;
				for (JsonNode arrayValue : entry.getValue()) {
					if (arrayValue.isTextual()) {
						@SuppressWarnings("unchecked")
						List<String> values = (List<String>) propertyMap.get(entry.getKey());
						if (values == null) {
							values = new ArrayList<>();
							propertyMap.put(entry.getKey(), values);
						}
						values.add(arrayValue.getTextValue());
					} else {
						innerMap = getNodeTree(arrayValue);
						propertyMap.put(entry.getKey(), innerMap);
					}
				}

			} else {
				Map<String, Object> innerMap = getNodeTree(entry.getValue());
				propertyMap.put(entry.getKey(), innerMap);
			}
			// TODO: Setting the JsonNode as it is in the map, need to
			// come back to this and populate text values by drilling
			// down to all levels.
		}
		return propertyMap;
		// propertyMap.put(node., value)
	}

}
