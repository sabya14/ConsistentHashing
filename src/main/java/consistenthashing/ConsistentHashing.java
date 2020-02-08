package consistenthashing;

import java.security.DigestException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class ConsistentHashing {
    private final SortedMap<Long, Node> ring = new TreeMap<>();
    private int replicationFactor = 0;
    private MD5Hashing hashing;
    private final int maxServers;

    public ConsistentHashing(List<PhysicalNode> physicalNodes, MD5Hashing hashing, int maxServers) throws NoSuchAlgorithmException {
        this.maxServers = maxServers;
        this.hashing = hashing;
        physicalNodes.forEach(physicalNode -> {
            try {
                addToRing(physicalNode);
            } catch (DigestException e) {
                e.printStackTrace();
            }
        });
    }

    public ConsistentHashing(List<PhysicalNode> physicalNodes, MD5Hashing hashing, int maxServers, int replicationFactor) {
        this.maxServers = maxServers;
        this.hashing = hashing;
        this.replicationFactor = replicationFactor;
        physicalNodes.forEach(physicalNode -> {
            try {
                addToRing(physicalNode);
            } catch (DigestException e) {
                e.printStackTrace();
            }
        });
    }


    private void addToRing(PhysicalNode physicalNode) throws DigestException {
        ring.put(hashing.hash(physicalNode.getKey()), physicalNode);
        if (replicationFactor > 0) {
            IntStream.range(1, replicationFactor).forEach(count -> {
                VirtualNode virtualNode = new VirtualNode(physicalNode, count);
                ring.put(hashing.hash(virtualNode.getKey()), virtualNode);
            });
        }
    }

    public List<Node> getAllNodes() {
        return new ArrayList<Node>(ring.values());
    }

    public void addNodes(Node node) throws DigestException {
        addToRing((PhysicalNode) node);
    }

    public void removeNode(Node node) {
        long nodeHash = hashing.hash(node.getKey());
        ArrayList<Long> virtualNodesToRemove = new ArrayList<Long>();
        if (replicationFactor > 0) {
            ring.values().stream().forEach(currentNode -> {
                if (currentNode instanceof VirtualNode && ((VirtualNode) currentNode).getParentKey().equals(node.getKey())) {
                    long virtualNodeHash = hashing.hash(currentNode.getKey());
                    virtualNodesToRemove.add(virtualNodeHash);
                }
            });
        }
        virtualNodesToRemove.forEach(ring::remove);
        ring.remove(nodeHash);
    }

    public String routeTo(String requestObject) {
        long requestHash = hashing.hash(requestObject);
        SortedMap<Long, Node> nodesToLeftOfRequestedHash = this.ring.tailMap(requestHash);
        Long routerKey = !nodesToLeftOfRequestedHash.isEmpty() ? nodesToLeftOfRequestedHash.firstKey() : ring.firstKey();
        return ring.get(routerKey).getKey();
    }
}
