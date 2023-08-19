package com.sevador.utility.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

@SuppressWarnings("unused")
public class Test {

	private static final String ohai = null;

	private final String olhai = null;
	private static final String ohjai = null;
	private final String ohgai = null;
	private final String ohedai = null;
	private final String ohgeai = null;
	private static final String ohdsai = null;
	private final String ohaqqi = null;
	// private Test test = new Test();
	private int lol;

	private static enum Lol {
		LOL(0), LOOL(1), LOOOL(2), LOOOOL(3), LOOOOOL(4);
		private int lol;

		private Lol(int lol) {
			this.lol = lol;
		}

		public int toInteger() {
			return lol;
		}
	}

	public static void main(String[] arguments) {
		if (true) {
			try {
				KeyFactory factory = KeyFactory.getInstance("RSA");
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
				keyGen.initialize(1024);
				KeyPair keypair = keyGen.genKeyPair();
				PrivateKey privateKey = keypair.getPrivate();
				PublicKey publicKey = keypair.getPublic();

				RSAPrivateKeySpec privSpec = factory.getKeySpec(privateKey,
						RSAPrivateKeySpec.class);

				writeKey("private rsa key: ", privSpec.getModulus(),
						privSpec.getPrivateExponent());

				RSAPublicKeySpec pubSpec = factory.getKeySpec(publicKey,
						RSAPublicKeySpec.class);

				writeKey("rsapub", pubSpec.getModulus(),
						pubSpec.getPublicExponent());
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				InetAddress[] ohai = InetAddress.getAllByName(InetAddress
						.getLocalHost().getHostName());
				for (int j = 0; j < ohai.length; j++) {
					System.out.println(Arrays.toString(ohai[j].getAddress()));
					// System.out.format("%02X%s", ohai[j], (j < ohai.length -
					// 1) ? "-" : "");
				}
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			return;
		}
		while (true) {
			int count = 0;
			long start = System.currentTimeMillis();
			for (int i = 0; i < 100000000; i++) {
				count = Lol.LOL.toInteger() + Lol.LOOL.toInteger()
						+ Lol.LOOOL.toInteger() + Lol.LOOOOL.toInteger()
						+ Lol.LOOOOOL.toInteger();
			}
			System.out.println("Took " + (System.currentTimeMillis() - start)
					+ " milliseconds for first test.");
			count = 0;
			start = System.currentTimeMillis();
			for (int i = 0; i < 100000000; i++) {
				count = Lol.LOL.ordinal() + Lol.LOOL.ordinal()
						+ Lol.LOOOL.ordinal() + Lol.LOOOOL.ordinal()
						+ Lol.LOOOOOL.ordinal();
			}
			if (false) {
				break;
			}
			System.out.println("Took " + (System.currentTimeMillis() - start)
					+ " milliseconds for second test.");
		}
		System.out.println("Region x, y: " + ((14133 >> 8) << 6) + ", "
				+ ((14133 & 0xFF) << 6));
		System.out.println(Modifier.toString(Test.class.getDeclaredFields()[0]
				.getModifiers()));
		checkFields(new Test());
	}

	public static void writeKey(String file, BigInteger modulus,
			BigInteger exponent) {
		System.out.println(file);
		System.out
				.println("private static final BigInteger RSA_MODULUS = new BigInteger(\""
						+ modulus.toString() + "\");");
		System.out
				.println("private static final BigInteger RSA_EXPONENT = new BigInteger(\""
						+ exponent.toString() + "\");");
	}

	private static final void checkFields(Object o) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					"./data/test.txt")));
			Field[] fields = o.getClass().getDeclaredFields();
			String name;
			for (Field f : fields) {
				if ((name = Modifier.toString(f.getModifiers()))
						.contains("static") || name.contains("transient")) {
					continue;
				}
				if (!f.getType().toString().contains("com.sevador")) {
					if (f.getType().isPrimitive()) {
						System.out.println("Saving field: " + f.getName()
								+ ", type: " + f.getType());
						continue;
					}
					for (Class<?> c : f.getType().getInterfaces()) {
						if (c.equals(Serializable.class)) {
							System.out.println("Saving field: " + f.getName()
									+ ", type: " + f.getType());
						}
					}
					continue;
				} else {
					// TODO
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}