package ca.baosiek.patricia.symbolTableUtils;

import org.apache.commons.collections4.trie.PatriciaTrie;

/**
 * A wrapper to working with PATRICIA Trie implemented at Apache Commons library
 * as the underlying symbol table.
 * @param <Value>
 */

public class ApachePatriciaSymbolTable<Value> implements SymbolTable<Value> {

    // The underlying data structure of this wrapper
    private PatriciaTrie<Value> symbolTable = new PatriciaTrie<>();

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
