import java.util.*;
import java.util.stream.IntStream;

// ----- Node -----
class Node {
    int val;
    Node left, right;
    Node(int v) { val = v; }

    void printTree() { printTree(this, 0); }
    private void printTree(Node n, int d) {
        if (n == null) return;
        printTree(n.right, d + 1);
        for (int i = 0; i < d; i++) System.out.print("    ");
        System.out.println(n.val);
        printTree(n.left, d + 1);
    }
}

// ----- Exception for "no solution" -----
class TreeDoesNotExistException extends Exception {
    TreeDoesNotExistException(String msg) { super(msg); }
}

// ----- Textbook algorithm (O(n^2) style with slices + set checks) -----
public class BuildBinaryTree {

    // Build from inorder IO[0..n-1] and postorder PO[0..n-1]
    public static Node buildBinaryTree(int[] IO, int[] PO) throws TreeDoesNotExistException {
        if (IO == null || PO == null) return null;
        if (IO.length != PO.length) throw new TreeDoesNotExistException("lengths differ");
        int n = IO.length;
        if (n == 0) return null;

        int rootVal = PO[n - 1];

        int k = indexOf(rootVal, IO);
        if (k == -1) throw new TreeDoesNotExistException("root not in inorder");

        int[] IOLeft  = Arrays.copyOfRange(IO, 0, k);
        int[] IORight = Arrays.copyOfRange(IO, k + 1, n);
        int[] POLeft  = Arrays.copyOfRange(PO, 0, k);
        int[] PORight = Arrays.copyOfRange(PO, k, n - 1);

        if (!sameSet(IOLeft, POLeft) || !sameSet(IORight, PORight))
            throw new TreeDoesNotExistException("left/right labels mismatch");

        Node left  = buildBinaryTree(IOLeft,  POLeft);
        Node right = buildBinaryTree(IORight, PORight);

        Node root = new Node(rootVal);
        root.left = left;
        root.right = right;
        return root;
    }

    // ----- helpers -----
    private static int indexOf(int x, int[] a) {
        for (int i = 0; i < a.length; i++) if (a[i] == x) return i;
        return -1;
    }
    private static boolean sameSet(int[] a, int[] b) {
        if (a.length != b.length) return false;   // unique labels ⇒ size equality must hold
        HashSet<Integer> s1 = new HashSet<>(), s2 = new HashSet<>();
        for (int v : a) s1.add(v);
        for (int v : b) s2.add(v);
        return s1.equals(s2);
    }

    // Build postorder for a balanced tree when inorder is 0..n-1
    public static int[] makeBalancedPostFromInorder0N(int n) {
        int[] out = new int[n];
        int[] idx = new int[]{0};
        buildBalancedPost(0, n - 1, out, idx);
        return out;
    }
    private static void buildBalancedPost(int lo, int hi, int[] out, int[] idx) {
        if (lo > hi) return;
        int mid = (lo + hi) >>> 1;
        buildBalancedPost(lo, mid - 1, out, idx);
        buildBalancedPost(mid + 1, hi, out, idx);
        out[idx[0]++] = mid;
    }

    // timing
    static Node timeBuild(String label, int[] inorder, int[] postorder) {
        long t0 = System.nanoTime();
        try {
            Node r = buildBinaryTree(inorder, postorder);
            long t1 = System.nanoTime();
            System.out.printf("%s -> %.3f ms%n", label, (t1 - t0) / 1e6);
            return r;
        } catch (TreeDoesNotExistException e) {
            System.out.printf("%s -> No solution: %s%n", label, e.getMessage());
            return null;
        }
    }

    // ----- tests per assignment -----
    public static void main(String[] args) {
        System.out.println("Project 2 – Textbook Algorithm (inorder + postorder)");

        // Empty
        timeBuild("Empty", new int[0], new int[0]);

        // No-solution example (mismatched sets)
        int[] inorderBad = {0,2,4,6,8};
        int[] postBad    = {1,3,5,7,4};
        timeBuild("No-solution", inorderBad, postBad);

        // n = 20, 50, 100, 200
        int[] Ns = {20, 50, 100, 200};
        for (int n : Ns) {
            int[] inorder = IntStream.range(0, n).toArray();      // 0..n-1
            int[] post    = makeBalancedPostFromInorder0N(n);     // balanced postorder
            Node r = timeBuild("n=" + n, inorder, post);
            if (n == 20 && r != null) {                           // small printout once
                System.out.println("Shape (n=20):");
                r.printTree();
            }
        }
    }
}
