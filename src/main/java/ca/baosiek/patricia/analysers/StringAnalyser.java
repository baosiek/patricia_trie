/**
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the “Software”), to deal in the Software without restriction, including without
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so.
 *
 * This Software is only meant to serve as an accompany support to the story "PATRICIA Trie's Nuts and Bolts"
 * published on Medium.com
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHOR OR COPYRIGHT HOLDER BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.baosiek.patricia.analysers;

/**
 * This is a helper class to compute the number of bits is a key,
 * the first different bit comparing two keys and so on.
 */

public class StringAnalyser {

    private final int LENGTH = Character.SIZE;
    private final int NULL = -1;
    private final int EQUAL = -2;
    private final int EOS = 0x0000; // equivalent to 0000000000000000 in bits
    private final int BIT_MASK = 0x8000; // equivalent to 1000000000000000 in bits

    /**
     * @param key to compute length in bits
     * @return the length in bits
     */
    public int lengthInBits(String key) {

        return key.length() * LENGTH;
    }

    /**
     * @param key1 first key to be compared
     * @param key2 second key to be compared
     * @return the index of the last bit equal in both strings
     */
    public int compareStrings(String key1, String key2) {

        int i = -1; // index to compute the total number of bits
        int c1 = 0; // character of key1 to be compared
        int c2 = 0; // character of key2 to be compared

        /*
        This while loop always start with c1 == c2.
        In the first iteration the first character of both strings are compared
        and so on, until finding the first different character or when end of string
        (characterized with 0x0000) is reached in either key
         */
        while (c1 == c2) {

            i++;
            /*
            charAtIndex guarantees that always a character is returned. This
            character will be the one at position when i is within string range or
            end of string (EOS) character, characterized with 0x0000, when outside of
            string range. This helper method is relevant when strings have different sizes.
             */
            c1 = charAtIndex(key1, i);
            c2 = charAtIndex(key2, i);

            /*
            Condition to leave the loop. I.e. EOS reached for either key
             */
            if (c1 == EOS || c2 == EOS) break;
        }

        return c1 - c2;
    }

    /*
    charAtIndex is a helper method that guarantees that always a character is returned. This
    character will be the one at position when i is within string range or
    end of string (EOS) character, characterized with 0x0000, when outside of
    string range. This helper method is relevant when strings have different sizes.
    */
    private int charAtIndex(String s, int d) {

        if (d < s.length()) return s.charAt(d);
        else return EOS;
    }

    public boolean isBitSet(String key, int bitIndex, int lengthInBits){

        if (key == null || bitIndex >= lengthInBits){
            return false;
        }

        /*
        Characters are 16 bits long. So index is the integer part
        of the division, meaning the number of full characters and
        mod the reaming bits.
        Ex: if bitIndex is 47, index == 2 (result of 47 / 16) and mod == 15 (
        the mod of 47 / 16).
        */
        int index = bitIndex / LENGTH;
        int mod = bitIndex % LENGTH;

        /*
        Return true or false. This will indicate if from the node at hand
        we go either to the left child (if false) or to the right child (if true);
         */
        return (key.charAt(index) & mask(mod)) != 0;

    }

    private int mask(int bit){

        /*
        Unsigned Rotate bit times BIT_MASK to the left
         */
        return BIT_MASK >>> bit;

    }

    public int firstDifferentBitIndex(String key1, String key2){

        int i = -1; // index to compute the total number of bits
        int c1 = 0; // character of key1 to be compared
        int c2 = 0; // character of key2 to be compared

        /*
        If both strings are empty return -1
         */
        if (key1.length() == 0 && key2.length() == 0) {
            return NULL;
        }


        /*
        This while loop always start with c1 == c2.
        In the first iteration the first character of both strings are compared
        and so on, until finding the first different character or when end of string
        (characterized with 0x0000) is reached in either key
         */
        while (c1 == c2) {

            i++;
            /*
            charAtIndex guarantees that always a character is returned. This
            character will be the one at position when i is within string range or
            end of string (EOS) character, characterized with 0x0000, when outside of
            string range. This helper method is relevant when strings have different sizes.
             */
            c1 = charAtIndex(key1, i);
            c2 = charAtIndex(key2, i);

            /*
            Condition to leave the loop. I.e. EOS reached for either key
             */
            if (c1 == EOS || c2 == EOS) break;
        }

        if (c1 != c2) {

            /*
            (i * LENGTH) finds the number of bits for i chars.
            c1 ^ c2 XOR sets the first different bit equal to 1.
            Integer.numberOfLeadingZeros(c1 ^ c2) returns the number of
            0s before the 1.
            (- LENGTH) is because the static numberOfLeadingZeros method returns
            an integer with 32 bits. Characters in Java are 16 bit long, so the first 16
            bits needs to be subtracted to reach the correct bit.
             */
            return i * LENGTH + Integer.numberOfLeadingZeros(c1 ^ c2) - LENGTH;
        }

        return EQUAL;
    }

    public String[] splitPrefix(String s1, String s2){

        int i = -1; // index to compute the total number of bits
        int c1 = 0; // character of key1 to be compared
        int c2 = 0; // character of key2 to be compared

        /*
        This while loop always start with c1 == c2.
        In the first iteration the first character of both strings are compared
        and so on, until finding the first different character or when end of string
        (characterized with 0x0000) is reached in either key
         */
        while (c1 == c2) {

            i++;
            /*
            charAtIndex guarantees that always a character is returned. This
            character will be the one at position when i is within string range or
            end of string (EOS) character, characterized with 0x0000, when outside of
            string range. This helper method is relevant when strings have different sizes.
             */
            c1 = charAtIndex(s1, i);
            c2 = charAtIndex(s2, i);

            /*
            Condition to leave the loop. I.e. EOS reached for either key
             */
            if (c1 == EOS || c2 == EOS) break;
        }

        /*
        result is an array containing three strings. The first is the common prefix.
        The second is always the lesser of the two prefixes. The third is always
        the greater of both suffixes. If strings are equal the array result is null.
        If one string is the prefix of the other the first suffix (result[1]) will be
        empty.
         */
        String[] result = new String[3];
        if ((c1 - c2) > 0) {
            result[0] = s1.substring(0, i);
            result[1] = s2.substring(i);
            result[2] = s1.substring(i);
        } else if ((c1 - c2) < 0){
            result[0] = s1.substring(0, i);
            result[1] = s1.substring(i);
            result[2] = s2.substring(i);
        }

        return result;

    }

    public String toBitSequence(String s){

        char[] array = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sb.append(Integer.toBinaryString(array[i]));
            sb.append(" ");
        }

        return sb.toString();
    }
}
