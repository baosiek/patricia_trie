package ca.baosiek.patricia.apps;

import ca.baosiek.patricia.nodes.PATRICIATrieNode;

public class WordCount {

    public static void main(String[] args) {

        PATRICIATrieNode<Integer> node1 = new PATRICIATrieNode<>("Coisuquinha", 1, 1);
        PATRICIATrieNode<Integer> node2 = new PATRICIATrieNode<>("Hugo", 1, 1);
        PATRICIATrieNode<Integer> node3 = node1;

        System.out.printf("Node [%s] hash code is: [%d]\n", node1.getKey(), node1.hashCode());
        System.out.printf("Node [%s] hash code is: [%d]\n", node2.getKey(), node2.hashCode());
        System.out.printf("Node [%s] hash code is: [%d]\n", node3.getKey(), node3.hashCode());

        node1.setLeft(node2);
        node1.setRight(node1);

        System.out.println(node1.toString());
        System.out.println(node2.toString());
        System.out.println(node3.toString());
    }
}
