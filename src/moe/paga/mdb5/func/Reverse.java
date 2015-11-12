package moe.paga.mdb5.func;

import java.util.function.UnaryOperator;

/**
 * Reverse each byte of the byte array.
 * 
 * @author johnchen902
 */
public class Reverse implements UnaryOperator<byte[]> {
	@Override
	public byte[] apply(byte[] s) {
		byte[] t = new byte[s.length];
		for (int i = 0; i < t.length; i++)
			t[i] = (byte) (255 - s[i] & 0xFF);
		return t;
	}
}
