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

package ca.baosiek.patricia.symbolTableUtils;

import ca.baosiek.patricia.analysers.StringAnalyser;
import ca.baosiek.patricia.tries.PATRICIATrie;

/**
 * A wrapper to working with PATRICIA Trie as the underlying symbol table
 * @param <Value>
 */
public class PatriciaSymbolTable<Value> implements SymbolTable<Value> {

    // The underlying data structure of this wrapper
    PATRICIATrie<Value> symbolTable = new PATRICIATrie<>(new StringAnalyser());

    @Override
    public void put(String key, Value value) {

        symbolTable.put(key, value);
    }

    @Override
    public Value get(String key) {

        return symbolTable.get(key);
    }

    @Override
    public boolean containsKey(String key) {

        return symbolTable.containsKey(key);
    }

    @Override
    public long size() {

        return symbolTable.size();
    }
}
