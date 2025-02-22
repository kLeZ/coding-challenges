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

import java.io.FileInputStream;
import java.io.IOException;

public class ccwc {
	private static final String USAGE_HELP = "Usage: ccwc (-c | -l | -w | -m) <filename>";

	public static void main(String[] args) {
		WordCount wc = new WordCountImpl();
		switch (args.length) {
			case 2 -> {
				String flag = args[0];
				String filename = args[1];
				standardWc(filename, flag, wc);
			}
			case 1 -> {
				if (args[0].startsWith("-")) {
					String flag = args[0];
					pipeWc(flag, wc);
				} else {
					String filename = args[0];
					defaultWc(filename, wc);
				}
			}
			default -> {
				var stats = wc.countAll(System.in);
				System.out.printf("% 8d % 8d % 8d%n", stats.words(), stats.lines(), stats.bytes());
			}
		}
	}

	private static void pipeWc(String flag, WordCount wc) {
		switch (flag) {
			case "-c" -> System.out.printf("% 8d%n", wc.countBytes(System.in));
			case "-l" -> System.out.printf("% 8d%n", wc.countLines(System.in));
			case "-w" -> System.out.printf("% 8d%n", wc.countWords(System.in));
			case "-m" -> System.out.printf("% 8d%n", wc.countCharacters(System.in));
			default -> System.err.printf("Invalid flag: %s%n", flag);
		}
	}

	private static void defaultWc(String filename, WordCount wc) {
		try (var is = new FileInputStream(filename)) {
			var stats = wc.countAll(is);
			System.out.printf("% 8d % 8d % 8d %s%n", stats.words(), stats.lines(), stats.bytes(), filename);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void standardWc(String filename, String flag, WordCount wc) {
		try (var is = new FileInputStream(filename)) {
			switch (flag) {
				case "-c" -> System.out.printf("% 8d %s%n", wc.countBytes(is), filename);
				case "-l" -> System.out.printf("% 8d %s%n", wc.countLines(is), filename);
				case "-w" -> System.out.printf("% 8d %s%n", wc.countWords(is), filename);
				case "-m" -> System.out.printf("% 8d %s%n", wc.countCharacters(is), filename);
				default -> System.err.printf("Invalid flag: %s%n", flag);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
