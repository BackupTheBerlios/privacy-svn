/**
 *     This file is part of Diki.
 *
 *     Copyright (C) 2009 jtheuer
 *     Please refer to the documentation for a complete list of contributors
 *
 *     Diki is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Diki is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Diki.  If not, see <http://www.gnu.org/licenses/>.
 */
	
package de.jtheuer.diki.gui.utils;
import java.io.StringReader;
import java.util.logging.*;

import junit.framework.TestCase;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class HtmlMetadataExtractorTestCase extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(HtmlMetadataExtractorTestCase.class.getName());

	private static final String EXPECTED = "Ich bin ein Title";
	private static final String TEST1_TITLE = "<html><head><title>"+EXPECTED+"</title>";
	private static final String TEST2_EMPTY = "<html><head><title></title>";
	private static final String TEST3_TITLE = "<html><head><title>\n\n  "+EXPECTED+"\n\n  </title>";
	private static final String TEST4_BROKEN_HTML = "<html><head><title>"+EXPECTED+"</title>";
	private static final String TEST5 = "<html><head><title>\nIch\n bin  \n  ein Title\n\n</title>";
	private static final String META = "<html><head><title>foo</title><meta content=\""+EXPECTED+"\" name=\"description\" /></head></html>";

	private HtmlMetadataExtractor hme;
	/**
	 * @param name
	 */
	public HtmlMetadataExtractorTestCase(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		hme = new HtmlMetadataExtractor();
	}

	public final void testStartMatchingTitle1() {
		hme.startMatching(new StringReader(TEST1_TITLE));
		assertEquals(EXPECTED, hme.getTitle());
	}

	public final void testStartMatchingTitle2() {
		hme.startMatching(new StringReader(TEST2_EMPTY));
		assertEquals(null, hme.getTitle());
	}

	public final void testStartMatchingTitle3() {
		hme.startMatching(new StringReader(TEST3_TITLE));
		assertEquals(EXPECTED, hme.getTitle());
	}

	public final void testStartMatchingTitle4() {
		hme.startMatching(new StringReader(TEST4_BROKEN_HTML));
		assertEquals(EXPECTED, hme.getTitle());
	}
//  test5 isn't easily parseable, yet
//	public final void testStartMatchingTitle5() {
//		hme.startMatching(new StringReader(TEST5));
//		assertEquals(EXPECTED, hme.getTitle());
//	}
	public final void testStartMatchingDescription() {
		hme.startMatching(new StringReader(META));
		assertEquals(EXPECTED, hme.getDescription());
	}

}
