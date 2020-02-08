package consistenthashing;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.security.MessageDigest.getInstance;

public class MD5Hashing implements HashingFunction {
    private MessageDigest messageDigest;
    private int modulo;

    public MD5Hashing(int noOfNodes) throws NoSuchAlgorithmException {
        messageDigest = getInstance("MD5");
        modulo = noOfNodes;
    }

    @Override
    public long hash(String key) {
        messageDigest.reset();
        byte[] digest = messageDigest.digest(key.getBytes());
        BigInteger number = new BigInteger(1, digest);
        return (number.longValue() % this.modulo);
    }
}
