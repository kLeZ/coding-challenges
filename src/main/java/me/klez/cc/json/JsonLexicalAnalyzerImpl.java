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

import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

class JsonLexicalAnalyzerImpl implements JsonLexicalAnalyzer {
	@NonNull
	@Override
	public JsonTokenTable analyze(@NonNull final Reader reader) throws JsonLexicalException {
		try {
			var tokens = new LinkedList<JsonToken>();
			int intch;
			JsonToken token;
			boolean terminated = false;
			while ((intch = reader.read()) != -1) {
				char ch = (char) intch;
				terminated = true;
				token = switch (ch) {
					case '{' -> new JsonToken.BraceToken(true);
					case '}' -> new JsonToken.BraceToken(false);
					case '[' -> new JsonToken.BracketToken(true);
					case ']' -> new JsonToken.BracketToken(false);
					case ',' -> new JsonToken.CommaToken();
					case ':' -> new JsonToken.ColonToken();
					default -> {
						terminated = false;
						yield null;
					}
				};
				if (terminated) tokens.add(token);
			}
			if (tokens.isEmpty()) throw new JsonLexicalException("Invalid Json: Document is empty.");
			return new JsonTokenTable(tokens);
		} catch (IOException e) {
			throw new JsonLexicalException(e);
		}
	}
}
