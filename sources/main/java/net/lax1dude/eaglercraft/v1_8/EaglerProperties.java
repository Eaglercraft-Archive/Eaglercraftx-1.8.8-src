/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package net.lax1dude.eaglercraft.v1_8;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

public class EaglerProperties extends Properties {

	public void load(Reader reader) throws IOException {
		load0(new LineReader(reader));
	}

	public void load(InputStream inStream) throws IOException {
		load0(new LineReader(inStream));
	}

	private void load0(LineReader lr) throws IOException {
		StringBuilder outBuffer = new StringBuilder();
		int limit;
		int keyLen;
		int valueStart;
		boolean hasSep;
		boolean precedingBackslash;
		boolean first = true;

		while ((limit = lr.readLine()) >= 0) {
			keyLen = 0;
			valueStart = limit;
			hasSep = false;
			if(first && limit > 0) {
				if(lr.lineBuf[0] == 65279) {
					keyLen = 1;
				}
			}
			first = false;

			// System.out.println("line=<" + new String(lineBuf, 0, limit) + ">");
			precedingBackslash = false;
			while (keyLen < limit) {
				char c = lr.lineBuf[keyLen];
				// need check if escaped.
				if ((c == '=' || c == ':') && !precedingBackslash) {
					valueStart = keyLen + 1;
					hasSep = true;
					break;
				} else if ((c == ' ' || c == '\t' || c == '\f') && !precedingBackslash) {
					valueStart = keyLen + 1;
					break;
				}
				if (c == '\\') {
					precedingBackslash = !precedingBackslash;
				} else {
					precedingBackslash = false;
				}
				keyLen++;
			}
			while (valueStart < limit) {
				char c = lr.lineBuf[valueStart];
				if (c != ' ' && c != '\t' && c != '\f') {
					if (!hasSep && (c == '=' || c == ':')) {
						hasSep = true;
					} else {
						break;
					}
				}
				valueStart++;
			}
			String key = loadConvert(lr.lineBuf, 0, keyLen, outBuffer);
			String value = loadConvert(lr.lineBuf, valueStart, limit - valueStart, outBuffer);
			put(key, value);
		}
	}

	/*
	 * Read in a "logical line" from an InputStream/Reader, skip all comment and
	 * blank lines and filter out those leading whitespace characters (\u0020,
	 * \u0009 and \u000c) from the beginning of a "natural line". Method returns the
	 * char length of the "logical line" and stores the line in "lineBuf".
	 */
	private static class LineReader {
		LineReader(InputStream inStream) {
			this.inStream = inStream;
			inByteBuf = new byte[8192];
		}

		LineReader(Reader reader) {
			this.reader = reader;
			inCharBuf = new char[8192];
		}

		char[] lineBuf = new char[1024];
		private byte[] inByteBuf;
		private char[] inCharBuf;
		private int inLimit = 0;
		private int inOff = 0;
		private InputStream inStream;
		private Reader reader;

		int readLine() throws IOException {
			// use locals to optimize for interpreted performance
			int len = 0;
			int off = inOff;
			int limit = inLimit;

			boolean skipWhiteSpace = true;
			boolean appendedLineBegin = false;
			boolean precedingBackslash = false;
			boolean fromStream = inStream != null;
			byte[] byteBuf = inByteBuf;
			char[] charBuf = inCharBuf;
			char[] lineBuf = this.lineBuf;
			char c;

			while (true) {
				if (off >= limit) {
					inLimit = limit = fromStream ? inStream.read(byteBuf) : reader.read(charBuf);
					if (limit <= 0) {
						if (len == 0) {
							return -1;
						}
						return precedingBackslash ? len - 1 : len;
					}
					off = 0;
				}

				// (char)(byte & 0xFF) is equivalent to calling a ISO8859-1 decoder.
				c = (fromStream) ? (char) (byteBuf[off++] & 0xFF) : charBuf[off++];

				if (skipWhiteSpace) {
					if (c == ' ' || c == '\t' || c == '\f') {
						continue;
					}
					if (!appendedLineBegin && (c == '\r' || c == '\n')) {
						continue;
					}
					skipWhiteSpace = false;
					appendedLineBegin = false;

				}
				if (len == 0) { // Still on a new logical line
					if (c == '#' || c == '!') {
						// Comment, quickly consume the rest of the line

						// When checking for new line characters a range check,
						// starting with the higher bound ('\r') means one less
						// branch in the common case.
						commentLoop: while (true) {
							if (fromStream) {
								byte b;
								while (off < limit) {
									b = byteBuf[off++];
									if (b <= '\r' && (b == '\r' || b == '\n'))
										break commentLoop;
								}
								if (off == limit) {
									inLimit = limit = inStream.read(byteBuf);
									if (limit <= 0) { // EOF
										return -1;
									}
									off = 0;
								}
							} else {
								while (off < limit) {
									c = charBuf[off++];
									if (c <= '\r' && (c == '\r' || c == '\n'))
										break commentLoop;
								}
								if (off == limit) {
									inLimit = limit = reader.read(charBuf);
									if (limit <= 0) { // EOF
										return -1;
									}
									off = 0;
								}
							}
						}
						skipWhiteSpace = true;
						continue;
					}
				}

				if (c != '\n' && c != '\r') {
					lineBuf[len++] = c;
					if (len == lineBuf.length) {
						lineBuf = new char[len + (len << 1)];
						System.arraycopy(this.lineBuf, 0, lineBuf, 0, len);
						this.lineBuf = lineBuf;
					}
					// flip the preceding backslash flag
					precedingBackslash = (c == '\\') ? !precedingBackslash : false;
				} else {
					// reached EOL
					if (len == 0) {
						skipWhiteSpace = true;
						continue;
					}
					if (off >= limit) {
						inLimit = limit = fromStream ? inStream.read(byteBuf) : reader.read(charBuf);
						off = 0;
						if (limit <= 0) { // EOF
							return precedingBackslash ? len - 1 : len;
						}
					}
					if (precedingBackslash) {
						// backslash at EOL is not part of the line
						len -= 1;
						// skip leading whitespace characters in the following line
						skipWhiteSpace = true;
						appendedLineBegin = true;
						precedingBackslash = false;
						// take care not to include any subsequent \n
						if (c == '\r') {
							if (fromStream) {
								if (byteBuf[off] == '\n') {
									off++;
								}
							} else {
								if (charBuf[off] == '\n') {
									off++;
								}
							}
						}
					} else {
						inOff = off;
						return len;
					}
				}
			}
		}
	}

	/*
	 * Converts encoded &#92;uxxxx to unicode chars and changes special saved chars
	 * to their original forms
	 */
	private String loadConvert(char[] in, int off, int len, StringBuilder out) {
		char aChar;
		int end = off + len;
		int start = off;
		while (off < end) {
			aChar = in[off++];
			if (aChar == '\\') {
				break;
			}
		}
		if (off == end) { // No backslash
			return new String(in, start, len);
		}

		// backslash found at off - 1, reset the shared buffer, rewind offset
		out.setLength(0);
		off--;
		out.append(in, start, off - start);

		while (off < end) {
			aChar = in[off++];
			if (aChar == '\\') {
				// No need to bounds check since LineReader::readLine excludes
				// unescaped \s at the end of the line
				aChar = in[off++];
				if (aChar == 'u') {
					// Read the xxxx
					if (off > end - 4)
						throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = in[off++];
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed \\uxxxx encoding.");
						};
					}
					out.append((char) value);
				} else {
					if (aChar == 't')
						aChar = '\t';
					else if (aChar == 'r')
						aChar = '\r';
					else if (aChar == 'n')
						aChar = '\n';
					else if (aChar == 'f')
						aChar = '\f';
					out.append(aChar);
				}
			} else {
				out.append(aChar);
			}
		}
		return out.toString();
	}

}
