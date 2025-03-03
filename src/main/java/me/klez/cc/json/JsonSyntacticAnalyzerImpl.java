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

import lombok.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static me.klez.cc.json.JsonToken.*;
import static me.klez.cc.json.JsonToken.IdentifierToken.*;
import static me.klez.cc.json.JsonValue.*;

class JsonSyntacticAnalyzerImpl implements JsonSyntacticAnalyzer {
	private JsonToken currentToken;
	private JsonTokenTable table;

	@NonNull
	@Override
	public JsonValue parse(@NonNull final JsonTokenTable jsonTokenTable) throws JsonSyntacticException {
		init(jsonTokenTable);
		if (!(currentToken instanceof LeftBraceToken) && !(currentToken instanceof LeftBracketToken)) {
			throw new JsonSyntacticException("A JSON payload should be an object or array");
		}
		var result = parseValue();
		if (!(currentToken instanceof EndOfFileToken)) {
			throw new JsonSyntacticException("Expected end of input, found additional content");
		}
		clear();
		return result;
	}

	private void init(JsonTokenTable jsonTokenTable) throws JsonSyntacticException {
		clear();
		this.table = jsonTokenTable;
		advance();
	}

	private void clear() {
		currentToken = null;
		table = null;
	}

	private boolean advance() throws JsonSyntacticException {
		try {
			currentToken = table.nextToken();
		} catch (JsonLexicalException e) {
			throw new JsonSyntacticException(e.getMessage());
		}
		return true;
	}

	private JsonValue parseValue() throws JsonSyntacticException {
		return switch (currentToken) {
			case LeftBraceToken _ -> parseObject();
			case LeftBracketToken _ -> parseArray();
			case StringToken(var value) when advance() -> new JsonString(value);
			case NumberToken(var value) when advance() -> new JsonNumber(value);
			case FalseToken _ when advance() -> new JsonBoolean(false);
			case TrueToken _ when advance() -> new JsonBoolean(true);
			case NullToken _ when advance() -> new JsonNull();
			default -> throw new JsonSyntacticException("Unexpected token: " + currentToken);
		};
	}

	private JsonObject parseObject() throws JsonSyntacticException {
		if (!(currentToken instanceof LeftBraceToken)) {
			throw new JsonSyntacticException("Expected '{', got " + currentToken);
		}
		advance();

		// Handle empty object
		if (currentToken instanceof RightBraceToken) {
			advance();
			return new JsonObject();
		}

		Map<String, JsonValue> object = new LinkedHashMap<>();
		// Parse key-value pairs
		while (true) {
			// Parse key
			if (!(currentToken instanceof StringToken(var strValue))) {
				throw new JsonSyntacticException("Expected string key, got " + currentToken);
			}
			advance();

			// Parse colon
			if (!(currentToken instanceof ColonToken)) {
				throw new JsonSyntacticException("Expected ':', got " + currentToken);
			}
			advance();

			// Parse value
			JsonValue value = parseValue();

			// Add key-value pair to object
			object.put(strValue, value);

			// Check for comma or closing brace
			if (currentToken instanceof RightBraceToken) {
				advance();
				break;
			} else if (currentToken instanceof CommaToken) {
				advance();

				// Check for trailing comma (not allowed in standard JSON)
				if (currentToken instanceof RightBraceToken) {
					throw new JsonSyntacticException("Trailing comma is not allowed in JSON objects");
				}
			} else {
				throw new JsonSyntacticException("Expected ',' or '}', got " + currentToken);
			}
		}

		return new JsonObject(object);
	}

	private JsonArray parseArray() throws JsonSyntacticException {
		if (!(currentToken instanceof LeftBracketToken)) {
			throw new JsonSyntacticException("Expected '[', got " + currentToken);
		}
		advance();

		// Handle empty array
		if (currentToken instanceof RightBracketToken) {
			advance();
			return new JsonArray();
		}

		List<JsonValue> list = new ArrayList<>();
		// Parse elements
		while (true) {
			// Parse value
			JsonValue value = parseValue();

			// Add value to array
			list.add(value);

			// Check for comma or closing bracket
			if (currentToken instanceof RightBracketToken) {
				advance();
				break;
			} else if (currentToken instanceof CommaToken) {
				advance();

				// Check for trailing comma (not allowed in standard JSON)
				if (currentToken instanceof RightBracketToken) {
					throw new JsonSyntacticException("Trailing comma is not allowed in JSON arrays");
				}
			} else {
				throw new JsonSyntacticException("Expected ',' or ']', got " + currentToken);
			}
		}

		return new JsonArray(list);
	}
}
