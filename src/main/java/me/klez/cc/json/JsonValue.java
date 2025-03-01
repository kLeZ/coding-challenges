/*
 * Copyright (c) 2025. Alessandro 'kLeZ' Accardo.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.klez.cc.json;

import java.util.*;

public sealed interface JsonValue {
	// Helper methods for type checking and casting
	default boolean isObject() {
		return this instanceof JsonObject;
	}

	default boolean isArray() {
		return this instanceof JsonArray;
	}

	default boolean isString() {
		return this instanceof JsonString;
	}

	default boolean isNumber() {
		return this instanceof JsonNumber;
	}

	default boolean isBoolean() {
		return this instanceof JsonBoolean;
	}

	default boolean isNull() {
		return this instanceof JsonNull;
	}

	default JsonObject asObject() {
		return (JsonObject) this;
	}

	default JsonArray asArray() {
		return (JsonArray) this;
	}

	default JsonString asString() {
		return (JsonString) this;
	}

	default JsonNumber asNumber() {
		return (JsonNumber) this;
	}

	default JsonBoolean asBoolean() {
		return (JsonBoolean) this;
	}

	// Pretty-printing helper method
	String toJson();

	// Concrete JSON value implementations
	record JsonObject(Map<String, JsonValue> properties) implements JsonValue {
		public JsonObject(Map<String, JsonValue> properties) {
			this.properties = Collections.unmodifiableMap(properties);
		}

		public JsonObject() {
			this(new LinkedHashMap<>());
		}

		public JsonValue get(String key) {
			return properties.get(key);
		}

		@Override
		public String toJson() {
			return toPrettyString(0);
		}

		private String toPrettyString(int indent) {
			if (properties.isEmpty()) {
				return "{}";
			}

			StringBuilder sb = new StringBuilder("{" + System.lineSeparator());
			String indentStr = "  ".repeat(indent + 1);

			boolean first = true;
			for (var entry : properties.entrySet()) {
				if (!first) {
					sb.append(",").append(System.lineSeparator());
				}
				first = false;

				sb.append(indentStr).append("\"").append(entry.getKey()).append("\": ");

				// Handle nested pretty printing
				if (entry.getValue() instanceof JsonObject obj) {
					sb.append(obj.toPrettyString(indent + 1));
				} else if (entry.getValue() instanceof JsonArray arr) {
					sb.append(arr.toPrettyString(indent + 1));
				} else {
					sb.append(entry.getValue().toJson());
				}
			}

			sb.append(System.lineSeparator()).append("  ".repeat(indent)).append("}");
			return sb.toString();
		}
	}

	record JsonArray(List<JsonValue> elements) implements JsonValue {
		public JsonArray(List<JsonValue> elements) {
			this.elements = Collections.unmodifiableList(elements);
		}

		public JsonArray() {
			this(new ArrayList<>());
		}

		public JsonValue get(int index) {
			return elements.get(index);
		}

		public int size() {
			return elements.size();
		}

		@Override
		public String toJson() {
			return toPrettyString(0);
		}

		private String toPrettyString(int indent) {
			if (elements.isEmpty()) {
				return "[]";
			}

			StringBuilder sb = new StringBuilder("[" + System.lineSeparator());
			String indentStr = "  ".repeat(indent + 1);

			boolean first = true;
			for (var element : elements) {
				if (!first) {
					sb.append(",").append(System.lineSeparator());
				}
				first = false;

				sb.append(indentStr);

				// Handle nested pretty printing
				if (element instanceof JsonObject obj) {
					sb.append(obj.toPrettyString(indent + 1));
				} else if (element instanceof JsonArray arr) {
					sb.append(arr.toPrettyString(indent + 1));
				} else {
					sb.append(element.toJson());
				}
			}

			sb.append(System.lineSeparator()).append("  ".repeat(indent)).append("]");
			return sb.toString();
		}
	}

	record JsonString(String value) implements JsonValue {
		@Override
		public String toJson() {
			return "\"" + value.replace("\"", "\\\"") + "\"";
		}
	}

	record JsonNumber(double value) implements JsonValue {
		@Override
		public String toJson() {
			// Remove ".0" for integer values
			if (value == Math.floor(value) && !Double.isInfinite(value)) {
				return String.format("%.0f", value);
			}
			return Double.toString(value);
		}
	}

	record JsonBoolean(boolean value) implements JsonValue {
		@Override
		public String toJson() {
			return Boolean.toString(value);
		}
	}

	record JsonNull() implements JsonValue {
		@Override
		public String toJson() {
			return "null";
		}
	}
}
