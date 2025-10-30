package com.gf.career.space.util;

import org.bouncycastle.crypto.digests.SM3Digest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

public class SM3PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return sm3Hash(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || rawPassword == null) {
            return false;
        }
        
        // 对原始密码进行SM3哈希并与存储的哈希值比较
        String rawPasswordHash = sm3Hash(rawPassword.toString());
        return rawPasswordHash.equals(encodedPassword);
    }

    private String sm3Hash(String input) {
        SM3Digest digest = new SM3Digest();
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        digest.update(inputBytes, 0, inputBytes.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}