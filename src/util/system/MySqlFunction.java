package util.system;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MySqlFunction {

	public static String password(byte[] input)  {
		byte[] digest = null;
		
		// Stage 1
		digest = getHash(input);
		// Stage 2
		digest = getHash(digest);

		StringBuilder sb = new StringBuilder(1 + digest.length);
		sb.append("*");
		sb.append(ByteUtils.toHexString(digest).toUpperCase());
		return sb.toString();
	}
	
    public static byte[] getHash(byte[] input) {
        try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			return md.digest(input);
		} catch (NoSuchAlgorithmException e) {
			// 일어날 경우가 없다고 보지만 만약을 위해 Exception 발생
			throw new RuntimeException("SHA1" + " Algorithm Not Found", e);
		}
    }

	public static String password(String input) {
		if (input == null) {
			return null;
		}
		return password(input.getBytes());
	}

	public static String password(String input, String charsetName) throws UnsupportedEncodingException {
		if (input == null) {
			return null;
		}
		return password(input.getBytes(charsetName));
	}
	
	
	
	
	
}
