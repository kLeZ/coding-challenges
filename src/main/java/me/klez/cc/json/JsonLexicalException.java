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

/// An exception thrown by the JSON lexical analyzer
public class JsonLexicalException extends JsonException {
	/// Creates a new lexical exception
	///
	/// @param message
	/// 		the exception message
	public JsonLexicalException(String message) {
		super(message);
	}

	/// Creates a new lexical exception
	///
	/// @param message
	/// 		the exception message
	/// @param cause
	/// 		the exception cause
	public JsonLexicalException(String message, Throwable cause) {
		super(message, cause);
	}

	/// Creates a new lexical exception
	///
	/// @param cause
	/// 		the exception cause
	public JsonLexicalException(Throwable cause) {
		super(cause);
	}

	/// Creates a new lexical exception
	public JsonLexicalException() {
		super();
	}

	/// Creates a new lexical exception
	///
	/// @param message
	/// 		the exception message
	/// @param cause
	/// 		the exception cause
	/// @param enableSuppression
	/// 		whether suppression is enabled or disabled
	/// @param writableStackTrace
	/// 		whether the stack trace should be writable
	public JsonLexicalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
