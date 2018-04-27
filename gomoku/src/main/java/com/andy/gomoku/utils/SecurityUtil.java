package com.andy.gomoku.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.LookupTranslator;

/**
 * 加密工具类
 */
public class SecurityUtil {
	//换行符
	public static final String ENTER = "(\r\n|\r|\n|\n\r)";
	
	/**
	 * MD5加密
	 * @param text
	 * @return
	 */
	public static String md5(String text) {
		MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch(Exception e) {
            return "";
        }

        byte[] byteArray = text.getBytes();
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for(int i=0;i<md5Bytes.length;i++) {
            int val = ((int)md5Bytes[i]) & 0xff;
            if(val < 16) hexValue.append("0");
            
            hexValue.append(Integer.toHexString(val));
        }
        
        return hexValue.toString();
	}
	
	/**
	 * SHA-1加密
	 * @param text 要加密的文本
	 * @return
	 */
	public static String sha1(String text) {
		return sha1(text,null);
	}
	
	/**
	 * SHA-1加密
	 * @param text 要加密的文本
	 * @param salt
	 * @return
	 */
	public static String sha1(String text, String salt) {
		// SHA1签名生成
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			if(salt != null){
				md.reset();
				md.update(salt.getBytes());
			}
			byte[] digest = md.digest(text.getBytes());
			return encode(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 解密
	 * @param cipherText 要解密的密文
	 * @param key 解密密钥
	 * @return
	 */
	public static String decrypt(String cipherText, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
		cipher.init(Cipher.DECRYPT_MODE, skey);
		
		return new String(cipher.doFinal(decode(cipherText)));
	}
	
	/**
	 * MD5加密
	 * @param content
	 * @param key
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String content, String key) throws NoSuchAlgorithmException {
		char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		
		content = content.replaceAll(ENTER, "");
		messageDigest.update((key + content + key).getBytes());
		byte[] md = messageDigest.digest();

		int k = 0, j = md.length;
		char str[] = new char[j * 2];
		for(int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}
	
	
	/**
	 * BASE64编码
	 * @param bytes
	 * @return
	 */
	public static String encode(byte[] bytes) {
		return new String(Base64.encodeBase64(bytes));
	}
	
	/**
	 * BASE64解码
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static byte[] decode(String str) throws IOException {
		return Base64.decodeBase64(str);
	}
	
	public static final CharSequenceTranslator ESCAPE_HTML = 
	        new AggregateTranslator(
	            new LookupTranslator(new String[][]{
	                    {"&", "&amp;"},   // & - ampersand
	                    {"<", "&lt;"},    // < - less-than
	                    {">", "&gt;"},    // > - greater-than
	                    {"'", "&apos;"},    // ' - 
	                })
	        );
	
	public static String cleanXSS(String input) {
		return ESCAPE_HTML.translate(input);
	}
	
	public static void main(String[] args) {
		System.out.println(sha1("123456", "admin"));
	}
	
}