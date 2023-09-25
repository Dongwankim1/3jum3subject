package com.example.jobis.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * packageName    : com.example.jobis.util
 * fileName       : EncryptTest
 * author         : mac
 * date           : 2023/09/25
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/09/25        mac       최초 생성
 */

class CryptoTest {



    @Test
    public void 암호화테스트() throws Exception {
        Crypto crypto = new Crypto();
        String testWord = "aqwfqwf";
        String dummy = crypto.encryptAES256(testWord);

        Assertions.assertEquals(testWord, crypto.decryptAES256(dummy));

    }

}