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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.bcpg.ArmoredInputStream;
import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.util.encoders.Hex;

/**
 * @author Jan Torben Heuer <jan.heuer@uni-muenster.de>
 *
 */
public class PGPHandler {
	/* automatically generated Logger */@SuppressWarnings("unused")
	private static final Logger LOGGER = Logger.getLogger(PGPHandler.class.getName());

	private String identity;
	private PGPSecretKey secretkey;
	private PGPPrivateKey privatekey;
	private PGPPublicKey publickey;
	private PGPPublicKeyRingCollection keyring;
	
	/**
	 * Creates a new PGPHandler which is not very usable. This constructor is intedded to be used in
	 * JUnit tests and subclasses
	 */
	protected PGPHandler() {
		/* add security provider for PGP keys */
		Security.addProvider(new BouncyCastleProvider());
	}
	
	/**
	 * Creates a new {@link PGPHandler} from an existing {@link PGPSecretKey} and a public key ring.
	 * @param secretkey
	 * @param passphrase
	 * @param identity
	 * @param keyring
	 * @throws NoSuchProviderException
	 * @throws PGPException
	 */
	public PGPHandler(PGPSecretKey secretkey, char[] passphrase, String identity, PGPPublicKeyRingCollection keyring) throws NoSuchProviderException, PGPException {
		this();
		init(secretkey, passphrase, identity);
		this.keyring = keyring;
	}

	/**
	 * Creates a new {@link PGPHandler} from already existing PGP keys which are normally loaded from the
	 * file-system using a {@link FileInputStream}. 
	 * @param privatekeystream
	 * @param passphrase
	 * @param identity
	 * @param publickeyring
	 * @throws IOException
	 * @throws PGPException
	 * @throws NoSuchProviderException
	 */
	public PGPHandler(InputStream privatekeystream, char[] passphrase, String identity, InputStream publickeyring) throws IOException, PGPException, NoSuchProviderException {
		this();
		PGPSecretKeyRing secretKeyring = new PGPSecretKeyRing(privatekeystream);
		init(secretKeyring.getSecretKey(), passphrase, identity);
		
		if(publickeyring!=null) {
			keyring = new PGPPublicKeyRingCollection(publickeyring);
		} else {
			keyring = new PGPPublicKeyRingCollection(new LinkedList<PGPPublicKey>());
		}
	}

	/**
	 * Creates a new {@link PGPHandler} from already existing PGP keys which are normally loaded from the
	 * file-system using a {@link FileInputStream}. 
	 * @param privatekeystream
	 * @param passphrase
	 * @param identity
	 * @throws IOException
	 * @throws PGPException
	 * @throws NoSuchProviderException
	 */
	public PGPHandler(InputStream privatekeystream, char[] passphrase, String identity) throws IOException, PGPException, NoSuchProviderException {
		this(privatekeystream,passphrase,identity,null);
	}
	
	
	/**
	 * Called by the constructor normally: Stores all necessary keys in the class fields.
	 * @param key
	 * @param passphrase
	 * @param identity a {@link Provider}
	 * @throws NoSuchProviderException
	 * @throws PGPException
	 */
	protected void init(PGPSecretKey key, char[] passphrase, String identity) throws NoSuchProviderException, PGPException {
		this.secretkey = key;
		this.privatekey = secretkey.extractPrivateKey(passphrase,new BouncyCastleProvider());
		this.publickey = secretkey.getPublicKey();
		this.identity = identity;
	}

	/**
	 * Encryptes the given input and writes into the output. If the key doesn't exist nothing will happen.
	 * @param input input content
	 * @param output output as {@link ArmoredOutputStream} (BASE64)
	 * @param keyID the keyID of the PublicKey that should be used
	 * @throws PGPException
	 * @throws IOException
	 * @throws NoSuchProviderException
	 */
	public void encrypt(InputStream input, OutputStream output, long keyID) throws PGPException, IOException {
		PGPPublicKey encryptkey=null;
		
		/* take the own key if that is the ID... */
		if(keyID == publickey.getKeyID()) {
			encryptkey = publickey;
		} else {
			encryptkey = keyring.getPublicKey(keyID);
		}

		if (encryptkey != null) {
			
			PGPEncryptedDataGenerator encryptor = new PGPEncryptedDataGenerator(PGPEncryptedDataGenerator.TRIPLE_DES, new SecureRandom(), "BC");

			try {
				encryptor.addMethod(encryptkey);
			} catch (NoSuchProviderException e) {
				LOGGER.log(Level.SEVERE,"provider hasn't been loaded successfully! There is a problem with the Bouncycastle system!", e);
			}
			ArmoredOutputStream armoredOut = new ArmoredOutputStream(output);
			OutputStream encryptedOut = encryptor.open(armoredOut, new byte[256]);
			PGPCompressedDataGenerator compressor = new PGPCompressedDataGenerator(PGPCompressedDataGenerator.ZIP);
			OutputStream compressedOut = compressor.open(encryptedOut);

			PGPLiteralDataGenerator literalor = new PGPLiteralDataGenerator();
			OutputStream literalOut = literalor.open(compressedOut, PGPLiteralDataGenerator.TEXT, "internal", new Date(), new byte[256]);

			/* copy from input to output */
			IOUtils.copy(input, literalOut);

			literalor.close();
			compressor.close();
			encryptor.close();
			armoredOut.close();
		}
	}
	
	public String encrypt(String message, long keyID) throws PGPException, IOException {
		InputStream in = new ByteArrayInputStream(message.getBytes());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encrypt(in, out, keyID);
		return new String(out.toByteArray());
	}

	/**
	 * @param input a BASE64 encoded {@link InputStream}
	 * @param output
	 * @throws IOException
	 * @throws PGPException 
	 */
	public void decrypt(InputStream input, OutputStream output) throws IOException, PGPException {
		PGPObjectFactory in = new PGPObjectFactory(new ArmoredInputStream(input));
		Object object = in.nextObject();
		if (object instanceof PGPEncryptedDataList) {
			PGPEncryptedDataList pgpstream = (PGPEncryptedDataList) object;
			Iterator<?> it = pgpstream.getEncryptedDataObjects();
			
			/* iterate over content until a message has been found */
			while(it.hasNext()) {
				Object o = it.next();
				if (o instanceof PGPPublicKeyEncryptedData) {
					PGPPublicKeyEncryptedData pgp = (PGPPublicKeyEncryptedData) o;
					InputStream decrypted = pgp.getDataStream(privatekey, new BouncyCastleProvider());
					
					/* the stream is still zipped, so we have to unzip it... */
					PGPObjectFactory unzip_object = new PGPObjectFactory(decrypted);
					PGPCompressedData unzipped = (PGPCompressedData) unzip_object.nextObject();
					
					/* and literal ... */
					PGPObjectFactory literal_object = new PGPObjectFactory(unzipped.getDataStream()); 
					PGPLiteralData literal = (PGPLiteralData) literal_object.nextObject();
					
					IOUtils.copy(literal.getDataStream(), output);
					
					break;
				}
			}
		} else {
			throw new PGPException("Stream is not a PGPEncryptedDataList stream :"+ object.getClass().getSimpleName());
		}
	}
	
	/**
	 * Decryptes a BASE64 PGP encoded string with the private key of this {@link PGPHandler}
	 * @param input
	 * @return
	 * @throws IOException 
	 * @throws PGPException 
	 */
	public String decrypt(String input) throws IOException, PGPException {
		InputStream in = new ByteArrayInputStream(input.getBytes());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		decrypt(in, out);
		return new String(out.toByteArray());
	}
	
	/**
	 * Creates a new PGPHandler with freshly generated keys
	 * @param passphrase
	 * @param identity
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws SignatureException
	 * @throws IOException
	 * @throws PGPException
	 */
	public static PGPHandler generate(char[] passphrase, String identity) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, SignatureException, IOException, PGPException {
		PGPHandler handler = new PGPHandler();
		PGPSecretKey key = handler.generateKeypair(passphrase, identity);
		handler.init(key, passphrase, identity);
		
		return handler;
	}
	


	/**
	 * Generates a new private/public key protected with the given passphrase.
	 * 
	 * @param passphrase
	 * @param identity
	 * @param secret
	 * @param pub
	 * @return the generated key pair
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws IOException
	 * @throws PGPException
	 */
	private PGPSecretKey generateKeypair(char[] passphrase, String identity) throws NoSuchAlgorithmException,
			NoSuchProviderException, InvalidKeyException, SignatureException, IOException, PGPException {
		/* initialize generator */
		KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", "BC");
		kpg.initialize(2048);

		KeyPair kp = kpg.generateKeyPair();
		secretkey = new PGPSecretKey(PGPSignature.DEFAULT_CERTIFICATION, PGPPublicKey.RSA_GENERAL, kp.getPublic(), kp.getPrivate(), new Date(), identity,
				PGPEncryptedData.CAST5, passphrase, null, null, new SecureRandom(), "BC");
		
		return secretkey;

	}
	
	/**
	 * Stores the secret- and the public key into the specified output streams
	 * @param secretOut
	 * @param publicOut
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchProviderException
	 * @throws SignatureException
	 * @throws PGPException
	 */
	public void exportKeyPair(OutputStream secretOut, OutputStream publicOut) throws IOException, InvalidKeyException, NoSuchProviderException, SignatureException, PGPException {
		//secretOut = new ArmoredOutputStream(secretOut);
		secretkey.encode(secretOut);
		secretOut.close();

		//publicOut = new ArmoredOutputStream(publicOut);
		publickey.encode(publicOut);
		publicOut.close();
	}
	
	/**
	 * Exports the public key of the user
	 * @return
	 * @throws IOException
	 */
	public String exportPublicKey() throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ArmoredOutputStream publicOut = new ArmoredOutputStream(baos);
		publickey.encode(publicOut);
		publicOut.close();
		return new String(baos.toByteArray());
	}

	public String getIdentity() {
		return identity;
	}

	public PGPPrivateKey getPrivatekey() {
		return privatekey;
	}

	public PGPPublicKey getPublickey() {
		return publickey;
	}
	
	/**
	 * Adds a new public key to the current keyring
	 * @param inputstream a BASE64 encoded (armored) PGP Key stream
	 * @throws IOException 
	 */
	public void addPublicKey(InputStream inputstream) throws IOException {
		PGPPublicKeyRing key = new PGPPublicKeyRing(new ArmoredInputStream(inputstream));
		addPublicKey(key);
	}
	
	/**
	 * Adds a new public key to the current keyring
	 * @param key
	 * @throws IOException 
	 */
	public void addPublicKey(PGPPublicKeyRing key) {
		keyring = PGPPublicKeyRingCollection.addPublicKeyRing(keyring, key);
	}
	
	/**
	 * @param key any public key, e.g. {@link PGPHandler#getPublickey()}
	 * @return the key fingerprint like 6c0d a461 83f5 71b7 1dd2 206e 59ab 335d 987f ff27
	 */
	public static String fingerprintOf(PGPPublicKey key) {
		byte[] encoded = org.bouncycastle.util.encoders.Hex.encode(key.getFingerprint());
		StringBuffer fingerprint = new StringBuffer(new String(encoded));

		for(int i=4;i<fingerprint.length();i=i+5) {
			fingerprint.insert(i," ");
		}
		
		return fingerprint.toString();
	}
	
}
