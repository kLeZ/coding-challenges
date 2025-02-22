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
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class WordCountImpl implements WordCount {
	/// Count words in a text
	@Override
	public long countWords(@NonNull final InputStream text) {
		return countAll(text).words();
	}

	/// Count lines in a text
	@Override
	public long countLines(@NonNull final InputStream text) {
		return countAll(text).lines();
	}

	/// Count characters in a text
	@Override
	public long countCharacters(@NonNull final InputStream text) {
		return countAll(text).characters();
	}

	/// Count bytes in a text
	@Override
	public long countBytes(@NonNull final InputStream text) {
		return countAll(text).bytes();
	}

	/// Count everything in a text
	@Override
	public TextStatistics countAll(@NonNull final InputStream text) {
		try {
			byte[] bytes = text.readAllBytes();
			String string = new String(bytes, StandardCharsets.UTF_8);
			return new TextStatistics(string.split("\\s+").length, string.lines().count(), string
			                                                                                     .length(), bytes.length);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		return new TextStatistics(0, 0, 0, 0);
	}
}
