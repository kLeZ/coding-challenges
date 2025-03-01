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

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonParserTest {
	private final JsonParser parser = new JsonParserImpl();

	@ParameterizedTest
	@CsvFileSource(resources = "valid-sources.csv", numLinesToSkip = 1)
	void parseValid(String filepath) {
		System.out.printf("Parsing %s%n", filepath);
		try (var is = getClass().getClassLoader().getResourceAsStream(filepath)) {
			if (is != null) {
				var bais = new ByteArrayInputStream(is.readAllBytes());
				var node = parser.parse(bais);
				assertThat(node).isNotNull();
				bais.reset();
				String fileContents = new String(bais.readAllBytes());
				assertThatJson(node.toJson()).isEqualTo(fileContents);
			} else {
				Assertions.fail("File not found");
			}
		} catch (JsonException | IOException e) {
			Assertions.fail(e);
		}
	}

	@ParameterizedTest
	@CsvFileSource(resources = "invalid-sources.csv", numLinesToSkip = 1)
	void parseInvalid(String filepath, String exceptionType) {
		System.out.printf("Parsing %s trying to catch exception of type %s%n", filepath, exceptionType);
		try (var is = getClass().getClassLoader().getResourceAsStream(filepath)) {
			if (is != null) {
				assertThatThrownBy(() -> parser.parse(is)).isInstanceOf(Class.forName(exceptionType)
				                                                             .asSubclass(JsonException.class));
			} else {
				Assertions.fail("File not found");
			}
		} catch (IOException | ClassNotFoundException e) {
			Assertions.fail(e);
		}
	}
}
