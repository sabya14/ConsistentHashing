package consistenthashing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VirtualNodeTest {

    @Test
    void shouldReturnKeyAppendedWithCount(){
        PhysicalNode physicalNode = new PhysicalNode("127.0.0.1", "80");
        VirtualNode virtualNode = new VirtualNode(physicalNode, 1);
        assertEquals(virtualNode.getKey(), physicalNode.getKey() + "_1");
    }

    @Test
    void shouldGetPhysicalNodeKeyFromVirtualNodeObject(){
        PhysicalNode physicalNode = new PhysicalNode("127.0.0.1", "80");
        VirtualNode virtualNode = new VirtualNode(physicalNode, 1);
        assertEquals(virtualNode.getParentKey(), physicalNode.getKey());
    }
}