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
import me.klez.cc.json.JsonToken.BracketToken;
import me.klez.cc.json.JsonToken.ColonToken;

import static me.klez.cc.json.JsonToken.*;
import static me.klez.cc.json.JsonToken.IdentifierToken.*;

class JsonSyntacticAnalyzerImpl implements JsonSyntacticAnalyzer {
	@NonNull
	@Override
	public JsonNode parse(@NonNull final JsonTokenTable jsonTokenTable) throws JsonSyntacticException {
		JsonNode node = null;
		JsonToken prev = null;
		for (var token : jsonTokenTable.tokens()) {
			switch (token) {
				case BraceToken brace -> {
					if (brace.open()) {
						if (prev == null || prev instanceof CommaToken || prev instanceof ColonToken) {
							node = new JsonNode(NodeType.OBJECT, null);
							prev = token;
						} else {
							throw new JsonSyntacticException("Can't have open brace in this place!");
						}
					}
				}
				case BracketToken bracket -> {
				}
				case ColonToken colon -> {
				}
				case CommaToken comma -> {
				}
				case FalseToken f -> {
				}
				case TrueToken t -> {
				}
				case NullToken n -> {
				}
				case NumberToken num -> {
				}
				case StringToken str -> {
				}
			}
		}
		if (node == null) throw new JsonSyntacticException("Invalid document");
		return node;
	}
}
