import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import java.util.Comparator;

public class Solver {

    private MinPQ<Node> pq;
    private Stack<Board> stack;
    private Node root, neighbour, searchNode;
    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {

        if (initial == null) throw new IllegalArgumentException();
        this.moves = 0;
    
        root = new Node();
        root.data = initial;
        root.manhattan = initial.manhattan();
        root.moves = this.moves;
        root.prev = null;

        pq = new MinPQ<Node>(new PriorityOrder());
        pq.insert(root);

        stack = new Stack<Board>();
        searchNode = pq.delMin();

        while (!searchNode.data.isGoal()) {
            this.moves = searchNode.moves + 1;
            for (Board each: searchNode.data.neighbors()) {
                if (searchNode.prev != null) if (each.equals(searchNode.prev.data)) continue;
                neighbour = new Node();
                neighbour.data = each;
                neighbour.manhattan = each.manhattan();
                neighbour.moves = this.moves;
                neighbour.prev = searchNode;
                pq.insert(neighbour);
            }
            searchNode = pq.delMin();
        }

        while(searchNode != null) {
            stack.push(searchNode.data);
            searchNode = searchNode.prev;
        }

    }

    private class Node implements Comparable<Node>{

        private Board data;
        private int manhattan;
        private int moves;
        private Node prev;

        public int compareTo(Node that) {
            int p1 = this.manhattan + this.moves;
            int p2 = that.manhattan + that.moves;
            if(p1 == p2) return 0;
            else if(p1 > p2) return 1;
            else return -1;
        }

    }

    private class PriorityOrder implements Comparator<Node>{

        @Override
        public int compare(Node a, Node b){
            return a.compareTo(b);
        }

    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if(isSolvable() == false) return -1;
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if(isSolvable() == false) return null;
        return stack;
    }

    // test client (see below) 
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}