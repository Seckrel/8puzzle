import java.util.Arrays;
import java.util.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    private int[] tiles;
    private int N;
    private int n;
    public Board(int[][] tiles){
        this.n = tiles.length;
        this.tiles = new int[n*n];
        this.N = this.tiles.length;
        for(int i = 0; i<n; i++){
            for(int j = 0; j<n; j++){
                this.tiles[i * n + j]= tiles[i][j];
            }
        }
    }

    private Board(int[] tiles){
        this.N = tiles.length;
        this.tiles = tiles;
        this.n = (int) Math.sqrt(this.N);
    }
                                           
    // string representation of this board
    public String toString(){
        StringBuilder s = new StringBuilder();
        s.append(this.n + "\n");
        int j = 0;
        for (int i = 0; i < N; i++){
            s.append(String.format("%2d ", tiles[i]));
            j++;
            if(j == this.n){
                s.append("\n");
                j = 0;
            }    
        }
        return s.toString();
    }

    // board dimension n
    public int dimension(){return (int) this.n;}

    // number of tiles out of place
    public int hamming(){
        int distance = 0;
        for(int i = 0; i < this.N; i++){
            if(this.tiles[i] == 0) continue;
            if( i+1 != this.tiles[i])
                distance++;     
        }
        return distance;
    }


    // sum of Manhattan distances between tiles and goal
    public int manhattan(){
        int distance = 0;
        for(int i = 0; i < this.N; i++){
            if(this.tiles[i] == 0) continue;
            if(this.tiles[i]-1 != i){
                int curr_x = i/this.n;
                int curr_y = i%this.n;
                int goal_x = (this.tiles[i]-1)/this.n;
                int goal_y = (this.tiles[i]-1)%this.n;
                distance += ( Math.abs(curr_x-goal_x) + Math.abs(curr_y-goal_y) );
            }
            
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal(){return manhattan() == 0 && hamming() == 0;}

    // does this board equal y?
    public boolean equals(Object y){
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        if (this.N != that.N) return false;
        for(int i = 0; i<this.N; i++){
            if (this.tiles[i] != that.tiles[i]) return false;
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors(){
        int null_x = 0, null_y = 0;
        for(int i = 0; i<this.N; i++)
            if (this.tiles[i] == 0){
                null_x = i/this.n;
                null_y = i%this.n;
                break;
            }
        Stack<Board> stack = new Stack<Board>();
        if (null_x == 0 && null_y == 0) {
            stack.push(bottom(null_x, null_y));
            stack.push(right(null_x, null_y));
        } else if (null_x == 0 && null_y == n - 1) {
            stack.push(bottom(null_x, null_y));
            stack.push(left(null_x, null_y));
        } else if (null_x == n - 1 && null_y == 0) {
            stack.push(top(null_x, null_y));
            stack.push(right(null_x, null_y));
        } else if (null_x == n - 1 && null_y == n - 1) {
            stack.push(top(null_x, null_y));
            stack.push(left(null_x, null_y));
        } else if (null_x == 0) {
            stack.push(left(null_x, null_y));
            stack.push(bottom(null_x, null_y));
            stack.push(right(null_x, null_y));
        } else if (null_x == n - 1) {
            stack.push(top(null_x, null_y));
            stack.push(right(null_x, null_y));
            stack.push(left(null_x, null_y));
        } else if (null_y == 0) {
            stack.push(bottom(null_x, null_y));
            stack.push(top(null_x, null_y));
            stack.push(right(null_x, null_y));
        } else if (null_y == n - 1) {
            stack.push(top(null_x, null_y));
            stack.push(left(null_x, null_y));
            stack.push(bottom(null_x, null_y));
        } else {
            stack.push(top(null_x, null_y));
            stack.push(bottom(null_x, null_y));
            stack.push(left(null_x, null_y));
            stack.push(right(null_x, null_y));
        }
        return stack;
    }

    private void exch(int[] temp, int x, int null_x, int null_y, int y){
        int hold = temp[x * this.n + y];
        temp[x * this.n + y] = temp[null_x * this.n + null_y];
        temp[null_x * this.n + null_y] = hold;
    }

    private Board top(int null_x, int null_y){
        int[] temp = Arrays.copyOf(this.tiles, this.N);
        int x = null_x-1;
        exch(temp, x, null_x, null_y, null_y);
        return new Board(temp);
    }

    private Board bottom(int null_x, int null_y){
        int[] temp = Arrays.copyOf(this.tiles, this.N);
        int x = null_x + 1;
        exch(temp, x, null_x, null_y, null_y);
        return new Board(temp);
    }

    private Board left(int null_x, int null_y){
        int[] temp = Arrays.copyOf(this.tiles, this.N);
        int y = null_y - 1;
        exch(temp, null_x, null_x, null_y, y);
        return new Board(temp);
    }

    private Board right(int null_x, int null_y){
        int[] temp = Arrays.copyOf(this.tiles, this.N);
        int y = null_y + 1;
        exch(temp, null_x, null_x, null_y, y);
        return new Board(temp);
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin(){
        int tile1_x = -1, tile1_y=-1;
        int tile2_x = 0, tile2_y = 0;
        int[] temp = Arrays.copyOf(tiles, this.N);
        for(int i = 0; i<this.N; i++){
            if(this.tiles[i] == 0) continue;
            if(tile1_x == -1){
                tile1_x = i/this.n; 
                tile1_y = i%this.n;
            }else{
                tile2_x = i/this.n;
                tile2_y = i%this.n;
            }
            if(tile1_x == tile2_x && tile1_y == tile2_y) continue;
        }
        exch(temp, tile1_x, tile2_x, tile2_y, tile1_y);
        return new Board(temp);
    }


    

    // unit testing (not graded)
    public static void main(String[] args){
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);
    StdOut.println("tostring\n" + initial.toString());
    StdOut.println("dimen " + initial.dimension());
    StdOut.println("hamming " + initial.hamming());
    StdOut.println("manhattan: " + initial.manhattan());
    StdOut.println("goal: " + initial.isGoal());
    
    for(int i = 0; i<6; i++){
    Board newBoard = initial.twin();
    StdOut.println("tostring\n" + newBoard.toString());
    }

    // StdOut.println("tostring\n" + newBoard.toString());
    // StdOut.println("dimen " + newBoard.dimension());
    // StdOut.println("hamming " + newBoard.hamming());
    // StdOut.println("manhattan: " + newBoard.manhattan());
    // StdOut.println("goal: " + newBoard.isGoal());

    // for(Board b : initial.neighbors()){
    //     StdOut.println("tostring\n" + newBoard.toString());
    // StdOut.println("dimen " + b.dimension());
    // StdOut.println("hamming " + b.hamming());
    // StdOut.println("manhattan: " + b.manhattan());
    // StdOut.println("goal: " + b.isGoal());
    // }

    }

}