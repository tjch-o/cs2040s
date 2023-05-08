/**
 * ScapeGoat Tree class
 * <p>
 * This class contains some basic code for implementing a ScapeGoat tree. This
 * version does not include any of the
 * functionality for choosing which node to scapegoat. It includes only code for
 * inserting a node, and the code for
 * rebuilding a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {
        LEFT, RIGHT
    }

    /**
     * TreeNode class.
     * <p>
     * This class holds the data for a node in a binary tree.
     * <p>
     * Note: we have made things public here to facilitate problem set
     * grading/testing. In general, making everything
     * public like this is a bad idea!
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;
        double weight = 1;

        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        int count = 0;
        // no children so nothing to add to count
        if (node.left == null && node.right == null) {
            return 0;
        } else {
            // at least one of the nodes are not null
            if (child == Child.LEFT && node.left != null) {
                /* we just do wishful thinking and recursively check the left and right child of 
                the left node; adding the 1 is counting the current node */
                count = countNodes(node.left, Child.LEFT) +
                        countNodes(node.left, Child.RIGHT) + 1;
            } else if (child == Child.RIGHT && node.right != null) {
                // do the same on the right
                count = countNodes(node.right, Child.LEFT) + countNodes(node.right, Child.RIGHT) + 1;
            } else {}
        }
        return count;
    }

    // returns the index in the array to be filled next by the current node
    public int enumerateNodesHelper(TreeNode[] arr, TreeNode node, int index) {
        if (node == null) {
            return index;
        } else {
            // we use wishful thinking
            /*
             * check the left side of the tree first in a in-order traversal
             * then you update the array starting from index 0 later
             */
            int middleNodeIndex = enumerateNodesHelper(arr, node.left, index);
            // adding the parent node into the array
            arr[middleNodeIndex] = node;
            /*
             * check the right side of the tree and we continue filling the
             * array
             */
            int endIndex = enumerateNodesHelper(arr, node.right,
                    middleNodeIndex + 1);
            return endIndex;
        }
    }

    /**
     * Builds an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    TreeNode[] enumerateNodes(TreeNode node, Child child) {
        int size = countNodes(node, child);
        TreeNode[] allNodes = new TreeNode[size];
        int pointer = 0;

        if (child == Child.LEFT) {
            // modifies and updates the array; what we return here we don't need
            enumerateNodesHelper(allNodes, node.left, pointer);
        } else {
            enumerateNodesHelper(allNodes, node.right, pointer);
        }
        return allNodes;
    }

    public TreeNode buildTreeHelper(TreeNode[] nodeList, int start, int end) {
        if (start > end) {
            return null;
        }

        // middle of in order traversal is the root
        int middle = start + (end - start) / 2;
        TreeNode node = nodeList[middle];
        // we know everything less than middle is on the left hand side
        node.left = buildTreeHelper(nodeList, start, middle - 1);
        // everything higher than middle is on the right hand side
        node.right = buildTreeHelper(nodeList, middle + 1, end);
        return node;
    }

    /**
     * Builds a tree from the list of nodes Returns the node that is the new root of
     * the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    TreeNode buildTree(TreeNode[] nodeList) {
        return buildTreeHelper(nodeList, 0, nodeList.length - 1);
    }

    /**
     * Determines if a node is balanced. If the node is balanced, this should return
     * true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than
     * 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        if (node == null) {
            return true;
        }

        double leftChildWeight;
        double rightChildWeight;
        double twoThirdWeight = node.weight * 2 / 3;

        // we return 0 instead of 1 since the node does not exist
        leftChildWeight = (node.left != null) ? node.left.weight : 0;
        rightChildWeight = (node.right != null) ? node.right.weight : 0;
        if (leftChildWeight > twoThirdWeight || rightChildWeight > twoThirdWeight) {
            return false;
        }
        return true;
    }
    
    // our helper doesnt need the child hence we dont pass it in as a parameter that is only for fixWeight
    public void fixWeightsHelper(TreeNode node) {
        if (node.left == null && node.right == null) {
            node.weight = 1;
        } else if (node.left == null) {
            fixWeightsHelper(node.right);
            // remember our function does not returning anything so cannot add fixWeightsHelper
            // we just wishful thinking that we already have the weight of the right node computed
            node.weight = node.right.weight + 1;
        } else if (node.right == null) {
            fixWeightsHelper(node.left);
            node.weight = node.left.weight + 1;
        } else {
            fixWeightsHelper(node.left);
            fixWeightsHelper(node.right);
            node.weight = node.left.weight + node.right.weight + 1;
        }
    }

    public void fixWeights(TreeNode u, Child child) {
        if (u == null) {
            return ;
        } else if (child == Child.LEFT) {
            fixWeightsHelper(u.left);
        } else {
            fixWeightsHelper(u.right);
        }
    }

    /**
     * Rebuilds the specified subtree of a node.
     *
     * @param node  the part of the subtree to rebuild
     * @param child specifies which child is the root of the subtree to rebuild
     */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null)
            return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }
        // when you rebuild the tree we also fix the weights
        fixWeights(node, child);
    }

    public void insertHelper(int key) {
        // we need to check if everything is balanced
        // we always start off from the root
        TreeNode node = root;

        while (true) {
            if (node == null) {
                break;
            } else {
                if (key <= node.key) {
                    if (checkBalance(node.left)) {
                        // it is already balanced so we can keep going down the tree
                        node = node.left; 
                    } else {
                        rebuild(node, Child.LEFT);
                        break;
                    }
                } else {
                    if (checkBalance(node.right)) {
                        node = node.right; 
                    } else {
                        rebuild(node, Child.RIGHT);
                        break;
                    }
                }
            }
        }
       
    }

    /**
     * Inserts a key into the tree.
     *
     * @param key the key to insert
     */
    public void insert(int key) {
        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;

        while (true) {
            if (key <= node.key) {
                if (node.left == null)
                    break;

                // every time we go down 1 level we increase the current node weight by 1
                // this is only for initialising
                node.weight += 1;
                node = node.left;
            } else {
                if (node.right == null)
                    break;
                node.weight += 1;
                node = node.right;
            }
        }
        // we account for the last node 
        node.weight += 1;

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }
        insertHelper(key);
    }

    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 100; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);
    }
}
