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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class JsonParserTest {
	private JsonParser reader;

	@BeforeEach
	void setUp() {
		reader = new JsonParserImpl();
	}

	@Test
	void parseStep1Valid() {
		try (var is = getClass().getClassLoader().getResourceAsStream("json/step1/valid.json")) {
			if (is != null) {
				var node = reader.parse(is);
				System.out.println(node);
				Assertions.assertNotNull(node);
			} else {
				Assertions.fail("File not found");
			}
		} catch (JsonException | IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	void parseStep1Invalid() {
		try (var is = getClass().getClassLoader().getResourceAsStream("json/step1/invalid.json")) {
			if (is != null) {
				Assertions.assertThrowsExactly(JsonLexicalException.class, () -> reader.parse(is));
			} else {
				Assertions.fail("File not found");
			}
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}
}
