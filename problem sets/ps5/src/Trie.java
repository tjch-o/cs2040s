import java.util.ArrayList;
import javax.management.openmbean.KeyAlreadyExistsException;
import javax.swing.event.RowSorterListener;

public class Trie {

    // Wildcards
    final char WILDCARD = '.';
    final int numberOfCharacters = 62;
    TrieNode root;

    private class TrieNode {
        // there are 62 characters because of 26 letters x 2 (upper lower) + 0 to 9
        TrieNode[] presentChars = new TrieNode[numberOfCharacters];
        char key;
        boolean isTheEnd;

        // without children
        TrieNode() {
            this.key = WILDCARD;
            this.isTheEnd = false;
        }

        TrieNode(char key) {
            this.key = key;
            this.isTheEnd = false;
        }

        // checks if key is a number
        public boolean isKeyNumber(char key) {
            int x = key;
            // number ranges from 48 to 57
            if (x >= 48 && x <= 57) {
                return true;
            } 
            return false;
        }

        // checks if key is an alphabet but in lower case
        public boolean isKeyLowerCase(char key) {
            int x = key;
            if (x >= 65 && x <= 90) {
                return true;
            }
            return false;
        }

        public boolean contains(char key) {
            int y = key;
            if (isKeyNumber(key)) {
                return this.presentChars[y - 48] != null;
            } else if (isKeyLowerCase(key)) {
                // a to z is from index 10 onwards but the ascii value starts from 65
                return this.presentChars[y - 55] != null;
            } else {
                // A to Z is from index 36 onwards but the ascii value starts from 97
                return this.presentChars[y - 61] != null;
            }
        }

        public TrieNode find(char key) {
            int y = key;
            if (isKeyNumber(key)) {
                return this.presentChars[y - 48];
            } else if (isKeyLowerCase(key)) {
                return this.presentChars[y - 55];
            } else {
                return this.presentChars[y - 61];
            }
        }

        public void insert(TrieNode node) {
            int y = node.key;
            if (isKeyNumber(node.key)) {
                this.presentChars[y - 48] = node;
            } else if (isKeyLowerCase(node.key)) {
                this.presentChars[y - 55] = node;
            } else {
                this.presentChars[y - 61] = node;
            }
        }

        public void flagThisAsEnd() {
            this.isTheEnd = true;
        }
    }

    public Trie() {
        this.root = new TrieNode();
    }

    /**
     * Inserts string s into the Trie.
     *
     * @param s string to insert into the Trie
     */
    void insert(String s) {
        // we do a search but if it does not exist we just add the rest in
        int length = s.length();
        TrieNode currentNode = root;
        char currentChar;

        for (int i = 0; i < length; i += 1) {
            currentChar = s.charAt(i);
            if (currentNode.contains(currentChar)) {
                // we found the current character so we update the currentNode
                currentNode = currentNode.find(currentChar);
            } else {
                // we insert it in
                TrieNode newNodeToInsert = new TrieNode(currentChar);
                currentNode.insert(newNodeToInsert);
                currentNode = newNodeToInsert;
            }
        }
        // we need to know that this node is the end
        currentNode.flagThisAsEnd();
    }

    /**
     * Checks whether string s exists inside the Trie or not.
     *
     * @param s string to check for
     * @return whether string s is inside the Trie
     */
    boolean contains(String s) {
        int length = s.length();
        // we always start from the root
        TrieNode currentNode = root;
        char currentChar;

        for (int i = 0; i < length; i += 1) {
            currentChar = s.charAt(i);
            if (!(currentNode.contains(currentChar))) {
                return false;
            } else {
                // you should move on to the next node
                currentNode = currentNode.find(currentChar);
            }
        }
        // if we reached the end of the string 
        return currentNode.isTheEnd;
    }

    // getOtherCharacters allows us to update the arraylist
    void getOtherCharacters(ArrayList<String> results, int limit, TrieNode node, StringBuilder str) {
        if (node.isTheEnd) {
            String text = str.toString();
            // if there is still space, we will add what we found into the array 
            if (results.size() < limit) {
                results.add(text);
            }
        }

        TrieNode current;
        char keyOfCurrent;
        int currentLengthOfStr;
        // we loop through all the children; each loop we store and update a new string into results
        for (int i = 0; i < numberOfCharacters; i += 1) {
            current = node.presentChars[i];
            // any non null children would be good to form strings
            if (current != null) {
                keyOfCurrent = current.key;
                str.append(keyOfCurrent);
                // then we do another recursive call to go deeper with the updated stringbuilder and node
                getOtherCharacters(results, limit, current, str);
                currentLengthOfStr = str.length();
                /* after you are done we delete the character because it is not gonna clear itself; 
                at the end of the day we are storing the combination into the array before removal
                just that we delete each character one by one (a lot of wishful thinking involved) */
                str.deleteCharAt(currentLengthOfStr - 1);
            }
        }
    }

    void prefixSearchHelper(String s, ArrayList<String> results, int limit, TrieNode node, StringBuilder str) {
        // if the prefix is null we should just get other characters to form our string
        if (s.equals("")) {
            getOtherCharacters(results, limit, node, str);
        } else {
            char currentLetter = s.charAt(0);
            /* we then slice the string to pass into the helper later; didnt fail test cases if i 
            put it here instead of inside */  
            s = s.substring(1);
            TrieNode currentNode;
            if (currentLetter == WILDCARD) {
                int length = str.length();
                for (int j = 0; j < numberOfCharacters; j += 1) {
                    currentNode = node.presentChars[j];
                    if (currentNode != null) {
                        char letter = currentNode.key;
                        str.append(letter);
                        // we then slice the string since we know the first one is not a wildcard that needs handling
                        prefixSearchHelper(s, results, limit, currentNode, str);
                        // at this point length != str.length() 
                        str.replace(length, str.length(), "");
                    }
                }
            } else {
                if (node.contains(currentLetter)) {
                    str.append(currentLetter);
                    node = node.find(currentLetter);
                    prefixSearchHelper(s, results, limit, node, str);
                }
            }
        }
    }

    /**
     * Searches for strings with prefix matching the specified pattern sorted by lexicographical order. This inserts the
     * results into the specified ArrayList. Only returns at most the first limit results.
     *
     * @param s       pattern to match prefixes with
     * @param results array to add the results into
     * @param limit   max number of strings to add into results
     */
    void prefixSearch(String s, ArrayList<String> results, int limit) {
        TrieNode node = root;
        StringBuilder str = new StringBuilder();
        
        if (s.contains(".")) {
            // we utilise the wildcard handling we already had in the helper function
            prefixSearchHelper(s, results, limit, node, str);
        } else {
            // we loop through the prefix 
            char currentChar;
            for (int i = 0; i < s.length(); i++) {
                currentChar = s.charAt(i);
                if (node.contains(currentChar)) {
                    str.append(currentChar);
                    node = node.find(currentChar);
                } else {
                    // without this you will fail private test case
                    return ;
                }
            }
            // we use the node we found to get the other characters
            getOtherCharacters(results, limit, node, str);
        }
    }

    // Simplifies function call by initializing an empty array to store the results.
    // PLEASE DO NOT CHANGE the implementation for this function as it will be used
    // to run the test cases.
    String[] prefixSearch(String s, int limit) {
        ArrayList<String> results = new ArrayList<String>();
        prefixSearch(s, results, limit);
        return results.toArray(new String[0]);
    }

    public static void main(String[] args) {
        Trie t = new Trie();
        t.insert("peter");
        t.insert("piper");
        t.insert("picked");
        t.insert("a");
        t.insert("peck");
        t.insert("of");
        t.insert("pickled");
        t.insert("peppers");
        t.insert("pepppito");
        t.insert("pepi");
        t.insert("pik");

        String[] result1 = t.prefixSearch("pe", 10);
        String[] result2 = t.prefixSearch("pe.", 10);
        // result1 should be:
        // ["peck", "pepi", "peppers", "pepppito", "peter"]
        // result2 should contain the same elements with result1 but may be ordered arbitrarily
    }
}
