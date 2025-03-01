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

/// An exception thrown by the JSON syntactic analyzer
public class JsonSyntacticException extends JsonException {
	/// Constructs a new exception with the specified detail message.
	///
	/// @param message
	/// 		the detail message
	public JsonSyntacticException(String message) {
		super(message);
	}

	/// Constructs a new exception with the specified detail message and cause.
	///
	/// @param message
	/// 		the detail message
	/// @param cause
	/// 		the cause
	public JsonSyntacticException(String message, Throwable cause) {
		super(message, cause);
	}

	/// Constructs a new exception with the specified cause.
	///
	/// @param cause
	/// 		the cause
	public JsonSyntacticException(Throwable cause) {
		super(cause);
	}

	/// Constructs a new exception with no detail message.
	public JsonSyntacticException() {
		super();
	}

	/// Constructs a new exception with the specified detail message, cause, suppression enabled or disabled, and writable stack trace enabled or disabled.
	///
	/// @param message
	/// 		the detail message
	/// @param cause
	/// 		the cause
	/// @param enableSuppression
	/// 		whether or not suppression is enabled or disabled
	/// @param writableStackTrace
	/// 		whether or not the stack trace should be writable
	public JsonSyntacticException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
