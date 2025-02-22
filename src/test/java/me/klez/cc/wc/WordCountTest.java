package me.klez.cc.wc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordCountTest {
	private WordCount wc;

	@BeforeEach
	void setUp() {
		wc = new WordCountImpl();
	}

	@Test
	void countWords() {
		try (var is = getClass().getClassLoader().getResourceAsStream("wc/test.txt")) {
			if (is != null) {
				assertEquals(58164, wc.countWords(is));
			} else {
				Assertions.fail("File not found");
			}
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	void countLines() {
		try (var is = getClass().getClassLoader().getResourceAsStream("wc/test.txt")) {
			if (is != null) {
				assertEquals(7145, wc.countLines(is));
			} else {
				Assertions.fail("File not found");
			}
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	void countCharacters() {
		try (var is = getClass().getClassLoader().getResourceAsStream("wc/test.txt")) {
			if (is != null) {
				assertEquals(339292, wc.countCharacters(is));
			} else {
				Assertions.fail("File not found");
			}
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	void countBytes() {
		try (var is = getClass().getClassLoader().getResourceAsStream("wc/test.txt")) {
			if (is != null) {
				assertEquals(342190, wc.countBytes(is));
			} else {
				Assertions.fail("File not found");
			}
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}

	@Test
	void countAll() {
		try (var is = getClass().getClassLoader().getResourceAsStream("wc/test.txt")) {
			if (is != null) {
				var stats = wc.countAll(is);
				assertEquals(58164, stats.words());
				assertEquals(339292, stats.characters());
				assertEquals(7145, stats.lines());
				assertEquals(342190, stats.bytes());
			} else {
				Assertions.fail("File not found");
			}
		} catch (IOException e) {
			Assertions.fail(e);
		}
	}
}
