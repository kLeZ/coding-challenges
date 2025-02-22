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

package me.klez.cc.wc;

import lombok.NonNull;

import java.io.InputStream;

/// WordCount - a Java implementation of the famous Unix command line tool wc
///
/// The TL/DR version is: wc – word, line, character, and byte count.
public interface WordCount {
	/// Count words in a text
	long countWords(@NonNull final InputStream text);

	/// Count lines in a text
	long countLines(@NonNull final InputStream text);

	/// Count characters in a text
	long countCharacters(@NonNull final InputStream text);

	/// Count bytes in a text
	long countBytes(@NonNull final InputStream text);

	/// Count everything in a text
	TextStatistics countAll(@NonNull final InputStream text);
}
