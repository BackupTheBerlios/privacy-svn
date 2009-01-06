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
import java.math.BigInteger;
import java.security.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bouncycastle.openpgp.*;
import org.openrdf.concepts.foaf.Person;
import org.openrdf.elmo.ElmoManager;

import com.xmlns.wot.PubKey;
import com.xmlns.wot.User;

import de.jtheuer.diki.lib.NetworkConnection;
import de.jtheuer.diki.lib.connectors.xmpp.XMPPConnector;
import de.jtheuer.diki.lib.friends.UserController;
import de.jtheuer.sesame.QNameURI;
import de.jtheuer.sesame.SimpleSet;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 * 
 * Manages all security related aspects. Initially it creates a public-private
 * keypair.
 */
public class SecurityHandler {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(SecurityHandler.class.getName());

	private NetworkConnection connection;

	private PGPHandler pgphandler;


	protected SecurityHandler() {

	}

	public SecurityHandler(NetworkConnection connection, Properties properties) {
		this();
		this.connection = connection;

		/* get xmpp passphrase to protect the key */
		char[] passphrase = properties.getProperty(XMPPConnector.KEY_PASSWORD).toCharArray();

		String identity = connection.getUserFactory().getEmailRepresentation();

		try {
			setUpPGPHandler(passphrase,identity);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "failed to generate public and private key", e);
		}
	}

	public String encrypt(String input, User pgpuser) throws PGPException, IOException {
		/* first check the keyid, then take the corresponding id */
		PubKey key = SimpleSet.first(pgpuser.getHasKey());
		String hexid = SimpleSet.first(key.getHex_id());
		long id = Long.parseLong(hexid, 16);
		
		return pgphandler.encrypt(input, id);
	}
	
	public String decrypt(String message) throws IOException, PGPException {
		return pgphandler.decrypt(message);
	}

	/**
	 * Generates a new private/public key protected with the given passphrase
	 * 
	 * Keys are stored in the homedirectory supplied by
	 * {@link NetworkConnection#getHomeDirectory()}. The identity is the
	 * Namespace from the {@link UserController}, i.e. the xmpp uri
	 * 
	 * @param passphrase
	 * @throws InvalidKeyException
	 * @throws NoSuchProviderException
	 * @throws SignatureException
	 * @throws IOException
	 * @throws PGPException
	 * @throws NoSuchAlgorithmException
	 */
	private void setUpPGPHandler(char[] passphrase, String identity) throws InvalidKeyException, NoSuchProviderException, SignatureException, IOException, PGPException,
			NoSuchAlgorithmException {

		/* check files in homedir */
		File home = connection.getHomeDirectory();
		if (home != null) {
			File seckeyFile = new File(home.getAbsolutePath() + File.separator + "secret.key");
			File pubkeyFile = new File(home.getAbsolutePath() + File.separator + "public.key");
			
			/* check if the keys already exist */
			if (!seckeyFile.exists()) {
				LOGGER.info("generating new keyfiles in " + home.getAbsolutePath());
				FileOutputStream pub = new FileOutputStream(pubkeyFile);
				FileOutputStream secret = new FileOutputStream(seckeyFile);
				
				/* generate a new keypair and export it */
				pgphandler = PGPHandler.generate(passphrase,identity);
				pgphandler.exportKeyPair(secret, pub);
			} else {
				/* load existing keys */
				LOGGER.info("loading existing secret key file " + seckeyFile.getAbsolutePath());
				pgphandler = new PGPHandler(new FileInputStream(seckeyFile),passphrase,identity,null);
			}
			
			updateLocalUser();

		}
	}
	
	private void updateLocalUser() {
		PGPPublicKey publicKey = pgphandler.getPublickey();
		
		/* set up pgp details for the person */
		Person localAgent = connection.getUserFactory().getLocalUser();
		ElmoManager manager = localAgent.getElmoManager();

		LOGGER.info("Updateing PGP information for " + QNameURI.toString(localAgent.getQName()));
		
		/* check if the localAgent already is a pgp User, else designate the schema */
		User pgpuser;
		if(localAgent instanceof User) {
			pgpuser = (User) localAgent;
		} else {
			pgpuser = manager.designateEntity(localAgent,User.class);
		}
		QNameURI pgpkey_qname = connection.getNamespaceFactory().constructNamespace("publickey");
		
		/* fill in the public key values */
		PubKey pgpkey = manager.designate(pgpkey_qname.toQName(),PubKey.class);

		pgpkey.setFingerprints(SimpleSet.create(PGPHandler.fingerprintOf(publicKey)));
		pgpkey.setHex_id(SimpleSet.create(Long.toString(publicKey.getKeyID(),16)));
		pgpkey.setLengths(SimpleSet.create(BigInteger.valueOf(publicKey.getBitStrength())));
		
		//pgpkey.setPubkeyContent(SimpleSet.create(pgphandler.exportPublicKey());
		pgpkey.setIdentity(pgpuser);
		pgpuser.setHasKey(SimpleSet.create(pgpkey));
	}

}
