package com.dckap.kothai.service;

public interface EncryptionDecryptionService {

	String encrypt(String stringToEncrypt, String secret);

	String decrypt(String stringToDecrypt, String secret);

}
