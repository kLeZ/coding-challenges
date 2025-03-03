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
import lombok.extern.slf4j.Slf4j;
import me.klez.cc.json.JsonToken.IdentifierToken.FalseToken;
import me.klez.cc.json.JsonToken.IdentifierToken.NullToken;
import me.klez.cc.json.JsonToken.IdentifierToken.StringToken;
import me.klez.cc.json.JsonToken.IdentifierToken.TrueToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.LinkedList;

import static me.klez.cc.json.JsonToken.*;
import static me.klez.cc.json.JsonToken.IdentifierToken.NumberToken;

@Slf4j
class JsonLexicalAnalyzerImpl implements JsonLexicalAnalyzer {
	private int line;
	private int column;

	@NonNull
	@Override
	public JsonTokenTable analyze(@NonNull final InputStream input) throws JsonLexicalException {
		var tokens = new LinkedList<JsonToken>();
		try (PushbackInputStream pbis = new PushbackInputStream(input, 8)) {
			skipWhitespace(pbis);
			int intch;
			while ((intch = read(pbis)) != -1) {
				char ch = (char) intch;
				JsonToken token = switch (ch) {
					case '{' -> new LeftBraceToken();
					case '}' -> new RightBraceToken();
					case '[' -> new LeftBracketToken();
					case ']' -> new RightBracketToken();
					case ':' -> new ColonToken();
					case ',' -> new CommaToken();
					case '"' -> readString(pbis);
					case '-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> readNumber(pbis, ch);
					case 't', 'f', 'n' -> readKeyword(pbis, ch);
					default -> {
						if (Character.isWhitespace(ch)) {
							yield null;
						} else {
							throw new JsonLexicalException("Unexpected character: '%s'".formatted(ch));
						}
					}
				};
				if (token == null) {
					continue;
				}
				log.trace("Character '{}' recognized as token {}", ch, token);
				tokens.add(token);
			}
		} catch (IOException e) {
			throw new JsonLexicalException(e);
		}
		if (tokens.isEmpty()) {
			throw new JsonLexicalException("Invalid Json: Document is empty.");
		}
		tokens.add(new EndOfFileToken());
		JsonTokenTable jsonTokenTable = new JsonTokenTable(tokens);
		log.trace("Json Token Table is: {}", jsonTokenTable);
		return jsonTokenTable;
	}

	// Reads the next character from the input stream
	private int read(@NonNull final PushbackInputStream input) throws JsonLexicalException {
		try {
			int c = input.read();
			if (c == '\n') {
				line++;
				column = 0;
			} else {
				column++;
			}
			return c;
		} catch (IOException e) {
			throw new JsonLexicalException("I/O error: " + e.getMessage());
		}
	}

	// Puts a character back into the input stream
	private void unread(@NonNull final PushbackInputStream input, int c) throws JsonLexicalException {
		if (c == -1) {
			return;
		}

		try {
			input.unread(c);
			if (c == '\n') {
				line--;
				// We can't correctly restore column here, but it's usually not needed
			} else {
				column--;
			}
		} catch (IOException e) {
			throw new JsonLexicalException("I/O error during pushback: " + e.getMessage());
		}
	}

	private void skipWhitespace(@NonNull final PushbackInputStream input) throws JsonLexicalException {
		int c;
		while ((c = read(input)) != -1) {
			if (!Character.isWhitespace(c)) {
				unread(input, c);
				break;
			}
		}
	}

	private JsonToken readKeyword(@NonNull final PushbackInputStream input, char firstChar) throws JsonLexicalException {
		StringBuilder sb = new StringBuilder();
		sb.append(firstChar);

		while (true) {
			int c = read(input);
			if (c == -1) {
				break;
			}

			if (Character.isLetter(c)) {
				sb.append((char) c);
			} else {
				unread(input, c);
				break;
			}
		}

		String keyword = sb.toString();
		return switch (keyword) {
			case "true" -> new TrueToken();
			case "false" -> new FalseToken();
			case "null" -> new NullToken();
			default -> throw new JsonLexicalException("Unknown keyword: " + keyword);
		};
	}

	private JsonToken readNumber(@NonNull final PushbackInputStream input, char firstChar) throws JsonLexicalException {
		StringBuilder sb = new StringBuilder();
		sb.append(firstChar);

		boolean hasDecimal = firstChar == '.';
		boolean hasExponent = false;

		while (true) {
			int c = read(input);
			if (c == -1) {
				break;
			}

			if (Character.isDigit(c)) {
				sb.append((char) c);
			} else if (c == '.' && !hasDecimal && !hasExponent) {
				sb.append((char) c);
				hasDecimal = true;
			} else if ((c == 'e' || c == 'E') && !hasExponent) {
				sb.append((char) c);
				hasExponent = true;

				// Check for sign after exponent
				int nextChar = read(input);
				if (nextChar == '+' || nextChar == '-') {
					sb.append((char) nextChar);
				} else if (nextChar != -1) {
					unread(input, nextChar);
				}
			} else {
				unread(input, c);
				break;
			}
		}

		try {
			if (sb.charAt(0) == '0' && sb.length() > 1 && sb.charAt(1) != '.')
			    throw new JsonLexicalException("Numbers cannot have leading zeroes");
			double value = Double.parseDouble(sb.toString());
			return new NumberToken(value);
		} catch (NumberFormatException e) {
			throw new JsonLexicalException("Invalid number format: " + sb);
		}
	}

	private JsonToken readString(@NonNull final PushbackInputStream input) throws JsonLexicalException {
		StringBuilder sb = new StringBuilder();
		boolean escaped = false;

		while (true) {
			int c = read(input);
			if (c == -1) {
				throw new JsonLexicalException("Unterminated string at line " + line + ", column " + column);
			}

			if (escaped) {
				switch (c) {
					case '"', '\\', '/' -> sb.append((char) c);
					case 'b' -> sb.append('\b');
					case 'f' -> sb.append('\f');
					case 'n' -> sb.append('\n');
					case 'r' -> sb.append('\r');
					case 't' -> sb.append('\t');
					case 'u' -> {
						// Read 4 hex digits
						StringBuilder hexCode = new StringBuilder();
						for (int i = 0; i < 4; i++) {
							int hexChar = read(input);
							if (hexChar == -1) {
								throw new JsonLexicalException("Unterminated Unicode escape sequence");
							}
							hexCode.append((char) hexChar);
						}
						try {
							int codePoint = Integer.parseInt(hexCode.toString(), 16);
							sb.append((char) codePoint);
						} catch (NumberFormatException e) {
							throw new JsonLexicalException("Invalid Unicode escape sequence: \\u" + hexCode);
						}
					}
					default -> throw new JsonLexicalException("Invalid escape sequence: \\" + (char) c);
				}
				escaped = false;
			} else if (c == '\\') {
				escaped = true;
			} else if (c == '"') {
				break; // End of string
			} else {
				sb.append((char) c);
			}
		}

		return new StringToken(sb.toString());
	}
}
