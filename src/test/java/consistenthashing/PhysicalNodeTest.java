package consistenthashing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PhysicalNodeTest {
    @Test
    void shouldReturnCorrectKey() {
        assertEquals(new PhysicalNode("127.0.0.1", "80").getKey(), "127.0.0.1:80");
    }
}