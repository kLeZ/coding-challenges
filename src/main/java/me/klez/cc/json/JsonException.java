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

/// An exception thrown by the JSON parser
public class JsonException extends Exception {
	/// Creates a new JSON exception
	///
	/// @param message
	/// 		The exception message
	public JsonException(String message) {
		super(message);
	}

	/// Creates a new JSON exception
	///
	/// @param message
	/// 		The exception message
	/// @param cause
	/// 		The exception cause
	public JsonException(String message, Throwable cause) {
		super(message, cause);
	}

	/// Creates a new JSON exception
	///
	/// @param cause
	/// 		The exception cause
	public JsonException(Throwable cause) {
		super(cause);
	}

	/// Creates a new JSON exception
	public JsonException() {
		super();
	}

	/// Creates a new JSON exception
	///
	/// @param message
	/// 		The exception message
	/// @param cause
	/// 		The exception cause
	/// @param enableSuppression
	/// 		Whether suppression is enabled or disabled
	/// @param writableStackTrace
	/// 		Whether the stack trace should be writable
	public JsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
