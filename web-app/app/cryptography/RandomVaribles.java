package cryptography;

import java.security.SecureRandom;
import java.math.BigInteger;

public class RandomVaribles {

    public static String getRandomString(int length){
        SecureRandom random = new SecureRandom();
        return new BigInteger(length*6, random).toString(32);
    }
}
