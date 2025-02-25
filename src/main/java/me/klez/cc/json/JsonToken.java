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

sealed interface JsonToken {
	sealed interface IdentifierToken extends JsonToken {
		record NumberToken(String value) implements IdentifierToken {
		}

		record StringToken(String value) implements IdentifierToken {
		}

		record NullToken() implements IdentifierToken {
		}

		record TrueToken() implements IdentifierToken {
		}

		record FalseToken() implements IdentifierToken {
		}
	}

	record BraceToken(boolean open) implements JsonToken {
	}

	record BracketToken(boolean open) implements JsonToken {
	}

	record CommaToken() implements JsonToken {
	}

	record ColonToken() implements JsonToken {
	}
}
