package consistenthashing;

import org.junit.jupiter.api.Test;

import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConsistentHashingTest {

    @Test
    void shouldAddPhysicalNodesToHashRingDuringSetup() throws NoSuchAlgorithmException {
        PhysicalNode p1 = new PhysicalNode("127.0.0.1", "80");
        PhysicalNode p2 = new PhysicalNode("127.0.0.2", "80");
        PhysicalNode p3 = new PhysicalNode("127.0.0.3", "80");
        PhysicalNode p4 = new PhysicalNode("127.0.0.4", "80");
        List<PhysicalNode> physicalNodes = List.of(p1, p2,p3, p4);
        ConsistentHashing consitentHashing = new ConsistentHashing(physicalNodes, new MD5Hashing(100),  100);
        assertEquals(consitentHashing.getAllNodes().size(), 4);
    }

    @Test
    void shouldBeAbleToAddPhysicalNodesToHashRingAfterSetup() throws NoSuchAlgorithmException, DigestException {
        PhysicalNode p1 = new PhysicalNode("127.0.0.1", "80");
        PhysicalNode p2 = new PhysicalNode("127.0.0.2", "80");
        PhysicalNode p3 = new PhysicalNode("127.0.0.3", "80");
        PhysicalNode p4 = new PhysicalNode("127.0.0.4", "80");
        List<PhysicalNode> physicalNodes = List.of(p1, p2,p3, p4);
        ConsistentHashing consitentHashing = new ConsistentHashing(physicalNodes, new MD5Hashing(100),  100);

        PhysicalNode p5 = new PhysicalNode("127.0.0.5", "80");
        consitentHashing.addNodes(p5);
        assertEquals(consitentHashing.getAllNodes().size(), 5);
    }

    @Test
    void shouldBeAbleToRemovePhysicalNodesFromHashRingAfterSetup() throws NoSuchAlgorithmException, DigestException {
        PhysicalNode p1 = new PhysicalNode("127.0.0.1", "80");
        PhysicalNode p2 = new PhysicalNode("127.0.0.2", "80");
        PhysicalNode p3 = new PhysicalNode("127.0.0.3", "80");
        PhysicalNode p4 = new PhysicalNode("127.0.0.4", "80");
        List<PhysicalNode> physicalNodes = List.of(p1, p2,p3, p4);
        ConsistentHashing consitentHashing = new ConsistentHashing(physicalNodes, new MD5Hashing(100),  100);
        consitentHashing.removeNode(p3);
        assertEquals(consitentHashing.getAllNodes().size(), 3);
    }

    @Test
    void shouldBeAbleToRouteRequestToNearestNodeInRing() throws NoSuchAlgorithmException, DigestException {
        MD5Hashing md5Hashing = mock(MD5Hashing.class);

        PhysicalNode p1 = new PhysicalNode("127.0.0.1", "80");
        PhysicalNode p2 = new PhysicalNode("127.0.0.2", "80");
        PhysicalNode p3 = new PhysicalNode("127.0.0.3", "80");
        PhysicalNode p4 = new PhysicalNode("127.0.0.4", "80");
        List<PhysicalNode> physicalNodes = List.of(p1, p2,p3, p4);
        when(md5Hashing.hash(p1.getKey())).thenReturn((long) 24);
        when(md5Hashing.hash(p2.getKey())).thenReturn((long) 49);
        when(md5Hashing.hash(p3.getKey())).thenReturn((long) 74);
        when(md5Hashing.hash(p4.getKey())).thenReturn((long) 99);
        String requestObject = "RequestObject";
        when(md5Hashing.hash(requestObject)).thenReturn((long) 45);
        ConsistentHashing consitentHashing = new ConsistentHashing(physicalNodes, md5Hashing,  100);
        String routerKey = consitentHashing.routeTo(requestObject);
        assertEquals(p2.getKey(), routerKey);
    }

    @Test
    void shouldBeAbleToRouteRequestToFirstKeyIfNoNearestNodeFoundInRing() throws NoSuchAlgorithmException, DigestException {
        MD5Hashing md5Hashing = mock(MD5Hashing.class);
        PhysicalNode p1 = new PhysicalNode("127.0.0.1", "80");
        PhysicalNode p2 = new PhysicalNode("127.0.0.2", "80");
        PhysicalNode p3 = new PhysicalNode("127.0.0.3", "80");
        PhysicalNode p4 = new PhysicalNode("127.0.0.4", "80");
        List<PhysicalNode> physicalNodes = List.of(p1, p2,p3, p4);
        when(md5Hashing.hash(p1.getKey())).thenReturn((long) 24);
        when(md5Hashing.hash(p2.getKey())).thenReturn((long) 49);
        when(md5Hashing.hash(p3.getKey())).thenReturn((long) 74);
        when(md5Hashing.hash(p4.getKey())).thenReturn((long) 90);
        String requestObject = "RequestObject";
        when(md5Hashing.hash(requestObject)).thenReturn((long) 95);
        ConsistentHashing consitentHashing = new ConsistentHashing(physicalNodes, md5Hashing,  100);
        String routerKey = consitentHashing.routeTo(requestObject);
        assertEquals(p1.getKey(), routerKey);
    }

    @Test
    void shouldBeAbleToCreateVirtualNodesAlongWithPhysicalNodesDuringStartup() throws NoSuchAlgorithmException, DigestException {
        PhysicalNode p1 = new PhysicalNode("127.0.0.1", "80");
        PhysicalNode p2 = new PhysicalNode("127.0.0.2", "80");
        PhysicalNode p3 = new PhysicalNode("127.0.0.3", "80");
        PhysicalNode p4 = new PhysicalNode("127.0.0.4", "80");
        List<PhysicalNode> physicalNodes = List.of(p1, p2,p3, p4);
        ConsistentHashing consitentHashing = new ConsistentHashing(physicalNodes, new MD5Hashing(100),  100, 4);
        assertEquals(16, consitentHashing.getAllNodes().size());
    }

    @Test
    void shouldBeAbleToAddVirtualNodesAlongWithPhysicalNodesAfterStartup() throws NoSuchAlgorithmException, DigestException {
        PhysicalNode p1 = new PhysicalNode("127.0.0.1", "80");
        PhysicalNode p2 = new PhysicalNode("127.0.0.2", "80");
        PhysicalNode p3 = new PhysicalNode("127.0.0.3", "80");
        PhysicalNode p4 = new PhysicalNode("127.0.0.4", "80");
        List<PhysicalNode> physicalNodes = List.of(p1, p2,p3, p4);
        ConsistentHashing consitentHashing = new ConsistentHashing(physicalNodes, new MD5Hashing(100),  100, 4);
        PhysicalNode p5 = new PhysicalNode("127.0.0.5", "80");
        consitentHashing.addNodes(p5);
        assertEquals(20, consitentHashing.getAllNodes().size());
    }

    @Test
    void shouldBeAbleToRemoveAllVirtualNodesAlongWithPhysicalNode() throws NoSuchAlgorithmException, DigestException {
        PhysicalNode p1 = new PhysicalNode("127.0.0.1", "80");
        PhysicalNode p2 = new PhysicalNode("127.0.0.2", "80");
        PhysicalNode p3 = new PhysicalNode("127.0.0.3", "80");
        PhysicalNode p4 = new PhysicalNode("127.0.0.4", "80");
        List<PhysicalNode> physicalNodes = List.of(p1, p2,p3, p4);
        ConsistentHashing consitentHashing = new ConsistentHashing(physicalNodes, new MD5Hashing(100),  100, 4);
        consitentHashing.removeNode(p1);
        assertEquals(12, consitentHashing.getAllNodes().size());
    }


}