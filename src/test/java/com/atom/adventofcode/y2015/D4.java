package com.atom.adventofcode.y2015;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Hex;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class D4 {

    public String hash(String content) {
        return DigestUtils.md5Hex(content);
    }

    @Test
    public void testMining() {
        assertEquals(609043, mining("abcdef", "00000"));

        assertEquals(117946, mining("ckczppom", "00000"));
    }

    @Test
    public void testMiningForSix() {
        assertEquals(3938038, mining("ckczppom", "000000"));
    }

    private int mining(String key, String criteria) {
        for(int i=0; i<Integer.MAX_VALUE; i++) {
            String completeKey = key + i;
            String res = hash(completeKey);
            if(res.startsWith(criteria)) {
                return i;
            }
        }
        return -1;
    }
}
