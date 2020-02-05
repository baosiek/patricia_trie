/*
  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
  documentation files (the “Software”), to deal in the Software without restriction, including without
  limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
  of the Software, and to permit persons to whom the Software is furnished to do so.
  <p>
  This Software is only meant to serve as accompany support to the story "PATRICIA Trie's Nuts and Bolts"
  published on Medium.com
  <p>
  THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
  TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
  IN NO EVENT SHALL THE AUTHOR OR COPYRIGHT HOLDER BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
  OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.baosiek.patricia.symbolTableUtils;

/**
 * A simple factory to instantiate underlying data structure.
 * Case 0: instantiate a wrapper for a Hash Map
 * Case 1: instantiate PATRICIA Trie (developed as support to the story "PATRICIA Trie's Nuts and Bolts"
 *         published on Medium.com
 * Case 2: instantiate Apache Common's implementation of PATRICIA Trie
 */
public class SymbolTableFactory {

    public static <S> SymbolTable<S> createSymbolTable(String instanceOfDataStructure) {

        switch (instanceOfDataStructure) {

            case "0":
                System.out.println("Using [HashMap] as data structure to support symbol table.");
                return new HashMapSymbolTable<>();
            case "1":
                System.out.println("Using [PATRICIA Trie] as data structure to support symbol table.");
                return new PatriciaSymbolTable<>();
            case "2":
                System.out.println("Using [Apache's Commons Collection PATRICIA Trie] as data structure to support symbol table.");
                return new ApachePatriciaSymbolTable<>();
            default:
                System.out.println("Invalid symbol table type.");
                return null;
        }
    }
}
