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
package de.jtheuer.diki.lib.pgp;

import java.io.*;
import java.security.*;
import java.util.logging.*;

import org.bouncycastle.openpgp.*;

import junit.framework.TestCase;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 */
public class PGPHandlerTestCase extends TestCase {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(PGPHandlerTestCase.class.getName());
	private static final String IDENTITY = "user@domain.tld";
	private static final String MESSAGE = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

	/**
	 * @param name
	 */
	public PGPHandlerTestCase(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public final void testGenerate() throws Exception {
		char[] pass = new char[] { '1', '2', '3' };
		PGPHandler handler = PGPHandler.generate(pass, IDENTITY);

		ByteArrayOutputStream secstream = new ByteArrayOutputStream();
		ByteArrayOutputStream pubstream = new ByteArrayOutputStream();
		handler.exportKeyPair(secstream, pubstream);
		byte[] seckey = secstream.toByteArray();
		// byte[] pubkey = pubstream.toByteArray();

		PGPHandler handler2 = new PGPHandler(new ByteArrayInputStream(seckey), pass, IDENTITY);

		ByteArrayOutputStream secstream2 = new ByteArrayOutputStream();
		ByteArrayOutputStream pubstream2 = new ByteArrayOutputStream();
		handler.exportKeyPair(secstream2, pubstream2);
		byte[] seckey2 = secstream2.toByteArray();

		assertTrue(seckey.length == seckey2.length);

		/* check if keys are still similar */
		for (int i = 0; i < seckey.length; i++) {
			assertEquals(seckey[i], seckey2[i]);
		}
	}

	public final void testEncrypt() throws NoSuchProviderException, PGPException, IOException, InvalidKeyException, NoSuchAlgorithmException,
			SignatureException {
		char[] pass = new char[] { '1', '2', '3' };
		PGPHandler handler = PGPHandler.generate(pass, IDENTITY);

		String encrypted = handler.encrypt(MESSAGE, handler.getPublickey().getKeyID());
		assertFalse(encrypted.equals(MESSAGE));

		String decrypted = handler.decrypt(encrypted);
		assertEquals(MESSAGE, decrypted);

	}
	
	public final void testFingerprint() throws Exception {
		char[] pass = new char[] { '1', '2', '3' };
		PGPHandler handler = PGPHandler.generate(pass, IDENTITY);

		LOGGER.info("New key is: " + handler.exportPublicKey());
		LOGGER.info("New key fingerprint is: " + PGPHandler.fingerprintOf(handler.getPublickey()));
		
		/* assert a multiple of 4+1 length */
		assertTrue((PGPHandler.fingerprintOf(handler.getPublickey()).length() + 1) % 5 == 0);
	}
}
