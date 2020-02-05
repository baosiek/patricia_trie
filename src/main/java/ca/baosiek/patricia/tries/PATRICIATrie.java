/**
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so.
 *
 * This Software is only meant to serve as accompany support to the story "PATRICIA Trie's Nuts and Bolts"
 * published on Medium.com
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHOR OR COPYRIGHT HOLDER BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.baosiek.patricia.tries;

import ca.baosiek.patricia.analysers.StringAnalyser;
import ca.baosiek.patricia.nodes.PATRICIATrieNode;

/**
 * This class follows the implementation of the two pseudocodes
 * found in the story PATRICIA Trie's Nuts and Bolts"
 * published on Medium.com
 */
public class PATRICIATrie<Value> {

    private PATRICIATrieNode<Value> root; //the root of this data structure
    private long size; //number of elements in it.
    private final StringAnalyser analyser; //helper class to analyse strings

    public PATRICIATrie(StringAnalyser analyzer) {

        this.analyser = analyzer;
        
        /*
         Following PATRICIA algorithm, i.e. a child node's different bit has to be higher than
         its parent's, here we define root as the first node with an empty string, having 
         first different bit equal to zero. Additionaly root's left child is pointed to root.
         root's right child is the only null pointer in the whole structure
         */
        root = new PATRICIATrieNode<>("", null, 0);
        root.setLeft(root);
    }

    public boolean put(String key, Value value) {

        // Key cannot be null
        if (key == null) return false;

        //Key length in bits
        int lengthInBits = analyser.lengthInBits(key);

        /*
         If it is the first insertion assign key as root.
         By convention root left child is assigned to itself while
         it's the right child will remain null. Actually the only null
         pointer in the whole data structure
         */
        

        /*
         The only place an empty string can be inserted is at the root.
         */
        if (key.isEmpty()) {

            // size is incremented only if root's value is null,
            // meaning it'k key is the first time it is being processed.
            if(root.getValue() == null){
                size++;
            }

            root.setValue(value);
            return true;
        }

        PATRICIATrieNode<Value> found = getNearestKey(key, lengthInBits);

        if (found.getKey().equals(key)){

            found.setValue(value);
            return true;
        }

        int firstDifferentBit = analyser.firstDifferentBitIndex(found.getKey(), key);

        /*
        We traverse the trie with two pointers:
        parent; and child. The key being inserted is compared to
        child's. Once the spot of the new entry is found parent left
        or right child shall point to the new entry. Initially parent
        points to root and child, by convention, points to left child.
         */
        PATRICIATrieNode<Value> parent = root;
        PATRICIATrieNode<Value> child = root.getLeft();

        while (child.getIndexBit() > parent.getIndexBit() && child.getIndexBit() < firstDifferentBit) {

            boolean isBitSet = analyser.isBitSet(key, child.getIndexBit(), lengthInBits);
            parent = child;

            if (!isBitSet) child = child.getLeft();
            else child = child.getRight();
        }

        PATRICIATrieNode<Value> entry = new PATRICIATrieNode<>(key, value, firstDifferentBit);
        boolean isBitSet = analyser.isBitSet(key, firstDifferentBit, lengthInBits);
        if (!isBitSet){
            entry.setLeft(entry);
            entry.setRight(child);
        } else {
            entry.setRight(entry);
            entry.setLeft(child);
        }

        isBitSet = analyser.isBitSet(key, parent.getIndexBit(), lengthInBits);
        if (!isBitSet || parent == root){
            parent.setLeft(entry);
        } else {
            parent.setRight(entry);
        }

        size++;
        return true;
    }

    public PATRICIATrieNode<Value> getNearestKey(String key, int lengthInBits){

        if (root.getKey().equals(key)) return root;
        if (root.getLeft().getKey().equals(key)) return root.getLeft();

        PATRICIATrieNode<Value> parent = root;
        PATRICIATrieNode<Value> current = root.getLeft();

        while (current.getIndexBit() > parent.getIndexBit()) {

            boolean isBitSet = analyser.isBitSet(key, current.getIndexBit(), lengthInBits);

            // Parent becomes current
            parent = current;

            // and current becomes:
            if (!isBitSet || parent == root) {
                // go left
                current = current.getLeft();
            } else {
                // go right;
                current = current.getRight();
            }
        }

        return current;

    }

    public Value get(String key){

        if (key == null) return null;
        if (root == null) return null;

        int lengthInBits = analyser.lengthInBits(key);
        PATRICIATrieNode<Value> found = getNearestKey(key, lengthInBits);
        if (found.getKey().compareTo(key) == 0) return found.getValue();
        else return null;
    }

    public boolean containsKey(String key){

        return get(key) != null;
    }

    public long size(){

        return size;
    }
}
