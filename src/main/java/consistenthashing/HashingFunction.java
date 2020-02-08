package consistenthashing;

import java.security.DigestException;

public interface HashingFunction {
    long hash(String key) throws DigestException;
}
