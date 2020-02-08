package consistenthashing;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PhysicalNode implements Node {
    String nodeAddress;
    String nodePort;

    @Override
    public String getKey() {
        return nodeAddress + ":" + nodePort;
    }
}
