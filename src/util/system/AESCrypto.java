package util.system;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class AESCrypto {

	public final static String ENCODE = "ENCODE";
	public final static String DECODE = "DECODE";

	private String cryptoKey = "ifit_byNam";
	private SecretKeySpec secretKey = null;
	private Cipher cipher = null;

	/**
	 * 
	 * 키생성 알고리즘은 AES 이며\n데이터 암호화 방식은 AES 의 ECB 블록 방식을 따르며 페딩 규칙은 사용자 정의 페딩 규칙을
	 * 따름\n참조 사이트
	 * http://java.sun.com/j2se/1.5.0/docs/guide/security/jce/JCERefGuide
	 * .html(128b it rijndael)
	 * 
	 * 
	 * 
	 * @author gnoo
	 * 
	 * @version 2005. 3. 25 오전 10:11:43
	 * 
	 * @param plainText
	 *            암호화 하고자 하는 String
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	private String encrypt(String plainText) throws Exception {

		secretKey = new SecretKeySpec(cryptoKey.getBytes(), "AES");

		cipher = Cipher.getInstance("AES/ECB/NoPadding");

		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		byte[] lackBuf = plainText.getBytes("UTF-8");

		byte[] fullBuf = this.addPadding(lackBuf);

		byte[] encBuf = cipher.doFinal(fullBuf);

		char[] encHex = Hex.encodeHex(encBuf);

		String cipherText = new String(encHex);

		return cipherText.toUpperCase();

	}

	/**
	 * 
	 * 키생성 알고리즘은 AES 이며\n데이터 복호화 방식은 AES 의 ECB 블록 방식을 따르며 페딩 규칙은 사용자 정의 페딩 규칙을
	 * 따름
	 * 
	 * 
	 * 
	 * @author gnoo
	 * 
	 * @version 2005. 3. 25 오전 10:11:46
	 * 
	 * @param cipherData
	 *            복호화 하고자 하는 String
	 * 
	 * @return
	 * 
	 * @throws Exception
	 */

	private String decrypt(String cipherData) throws Exception {

		secretKey = new SecretKeySpec(cryptoKey.getBytes(), "AES");

		cipher = Cipher.getInstance("AES/ECB/NoPadding");

		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		String cipherText = new String(cipherData.getBytes("UTF-8"));

		char[] encHex = new char[cipherText.length()];

		for (int i = 0; i < cipherText.length(); i++) {

			encHex[i] = cipherText.charAt(i);

		}

		byte[] decHex = Hex.decodeHex(encHex);

		byte[] decBuf = cipher.doFinal(decHex);

		byte[] cutBuf = this.deletePadding(decBuf);

		String plainText = new String(cutBuf, "UTF-8");

		return plainText;

	}

	/**
	 * 
	 * 사용자 정의 키값으로 암호화 와 복호화를 사용하고자 할경우 사용
	 * 
	 * 
	 * 
	 * @version 2005. 3. 25 오전 10:28:23
	 * 
	 * @param encType
	 * 
	 * @param content
	 * 
	 * @param key
	 * 
	 * @return
	 */

	public static String getSecure(String encType, String content, String key) {

		AESCrypto fc = new AESCrypto();

		String returnStr = null;

		fc.setCryptoKey(key);

		try {

			if (encType != null && encType.equals(ENCODE)) {

				returnStr = fc.encrypt(content);

			} else if (encType != null && encType.equals(DECODE)) {

				returnStr = fc.decrypt(content);

			}

		} catch (Exception e) {

		}

		return returnStr;

	}

	/**
	 * 
	 * 디폴트 키값으로 암호화 와 복호화를 사용하고자 할경우 사용
	 * 
	 * 
	 * 
	 * @version 2005. 3. 25 오전 10:28:23
	 * 
	 * @param encType
	 * 
	 * @param content
	 * 
	 * @return
	 */

	public static String getSecure(String encType, String content) {

		AESCrypto fc = new AESCrypto();

		String returnStr = null;

		try {

			if (encType != null && encType.equals(ENCODE)) {

				returnStr = fc.encrypt(content);

			} else if (encType != null && encType.equals(DECODE)) {

				returnStr = fc.decrypt(content);

			}

		} catch (Exception e) {

		}

		return returnStr;

	}

	/**
	 * 
	 * 16바이트의 배수를 만들기 위해서 0x00(공백) 바이트 추가 함수 (현재 16바이트의 배수일 경우에도 한번은 붙여줌)
	 * 
	 * 
	 * 
	 * @author gnoo
	 * 
	 * @version 2005. 3. 25 오전 10:11:49
	 * 
	 * @param lackBuf
	 * 
	 * @return
	 */

	private byte[] addPadding(byte[] lackBuf) {

		int fullLen = lackBuf.length + (16 - (lackBuf.length % 16));

		byte[] fullBuf = new byte[fullLen];

		System.arraycopy(lackBuf, 0, fullBuf, 0, lackBuf.length);

		for (int i = lackBuf.length; i < fullBuf.length; i++) {

			fullBuf[i] = 0x00;

		}

		return fullBuf;

	}

	/**
	 * 
	 * 암호화시에 추가한 0x00(공백) 바이트 제거 함수
	 * 
	 * 
	 * 
	 * @author gnoo
	 * 
	 * @version 2005. 3. 25 오전 10:11:51
	 * 
	 * @param fullBuf
	 * 
	 * @return
	 */

	private byte[] deletePadding(byte[] fullBuf) {

		int i = 0;

		boolean loop = true;

		while (loop) {

			if (i == fullBuf.length || fullBuf[i] == 0x00) {

				loop = false;

				i--;

			}

			i++;

		}

		byte[] cutBuf = new byte[i];

		System.arraycopy(fullBuf, 0, cutBuf, 0, i);

		return cutBuf;

	}

	/**
	 * 
	 * @param cryptoKey
	 *            The cryptoKey to set.
	 */

	public void setCryptoKey(String cryptoKey) {

		this.cryptoKey = cryptoKey;

	}
	
	
	
	public static String encryptPassword(String data) throws Exception {
		 
        if (data == null) {
            return "";
        }

        byte[] plainText = null; // 평문
        byte[] hashValue = null; // 해쉬값
        plainText = data.getBytes();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        hashValue = md.digest(plainText);

        /*
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(hashValue);
        */
        return new String(Base64.encodeBase64(hashValue));
}
	
	
	
	public static void main(String args[]) {

		//클라이언트
		String data = "seqarg=1,usermail=point@naver.com,pwd=fHV2K87OMjk1MCgXqrhG6aPEr/3KavOSlF+SmVlfcc8=,username=김삿갓,tel1=02,tel2=1234,tel3=1234,phone1=010,phone2=1234,phone3=1234,orgname=openscreen1,orgcode=1452346544,orgzipcode=136-123,orguseraddress=서울시 금천구 가산동 123-123,ceoname=홍길동";
		System.out.println("data :" + data);
		String encodeData = AESCrypto.getSecure(AESCrypto.ENCODE, data);
		System.out.println("encodeData :" + encodeData);
		String url = "http://test.co.kr/test/test.do?q=" + encodeData;
		
		System.out.println(url);
		System.out.println(url.length());
		//서버
//		String params =	request.getParameter("q");
		String params = url.substring(url.indexOf("=") + 1, url.length());
		params = "87E71A155677AFB1B14F92DB3489E9DD8BAF1417F949BBD76011B8A8DFACB1BF24B0530C2B9F3FD8BE5E52DA8A639A5DC1E7AE684D3871216D5DC60D14A2376F29DDD6FD5A81555B6F10464ABE7EB10211013732243234605ECE4900AB4F5390C8FFAF6B564EBCE748093CC574125C9CA04C4434BF686F50CBC7EA3A90E66ABE01B39A1CA697C9E5F6AAA8C6B03713389E733064C72F752DFAECDF65F2FAEA07435448F9280F9600D4FE5D0386947630DCFCDF3AE93940AF8E87D95051C1D5544C16000D6DDD3DF08B1952861300692D240C249894FDF64E6BB2538AD098CD907C2BE90BA5EA11B1179D5777C1DDECC008A16E520CF19FB046EEC375601E2EF8";
		
		System.out.println(params);
		
		String decodeData = AESCrypto.getSecure(AESCrypto.DECODE, params);
		String[] arr = decodeData.split(",");
		
		for(int i = 0 ; i < arr.length ; i++){
			String temp = arr[i];
			String[] tempArr = temp.split("=");
			String name = tempArr[0];
			String value = tempArr[1];
						
			System.out.println("name = " + name + " : value = " + value);
		}
		
		//String test = "fHV2K87OMjk1MCgXqrhG6aPEr/3KavOSlF+SmVlfcc8=";
		
		try {
			System.out.println(AESCrypto.encryptPassword("dmasiasia1"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
