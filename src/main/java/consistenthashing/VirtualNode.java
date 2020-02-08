package consistenthashing;

import lombok.Getter;

@Getter
public class VirtualNode implements Node {
    private PhysicalNode physicalNode;
    private String key;
    public VirtualNode(PhysicalNode physicalNode, int count) {
        this.physicalNode = physicalNode;
        this.key = physicalNode.getKey().concat("_" + count);
    }

    public String getParentKey() {
        return physicalNode.getKey();
    }

}
