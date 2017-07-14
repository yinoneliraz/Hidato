import java.awt.*;
import java.awt.List;
import java.util.*;

/**
 * Created by Yinon on 09/06/2017.
 */
public class checkGraph {
    Random rnd=new Random();
    int[][] graph;
    double p;
    int size=0;

    public checkGraph(int size, double p){
        this.size=size;
        graph=new int[size][size];
        this.p=p;
    }

    private void printMat(int[][] hidato) {
        for(int[] row: hidato){
            for(int element:row){
                System.out.print(element + "\t");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();

    }

    public int[][] getGraph(){
        graph=genHamiltonPath(size);
        return graph;
    }

    private int[][] genHamiltonPath(int size) {
        buildPath main = new buildPath();
        int[][] solution=new int[size][size];
        main.solution = new int[size];//stores the solution; index 0 is not used, i will use indexes from 1 to 9
        main.candidates = new HashMap<Integer, java.util.List<Integer>>();//for each position (1 to 9) in the solution, stores a list of candidate elements for that position

        java.util.List<Integer> values = new LinkedList<Integer>();
        for(int i=1;i<=size;i++){
            values.add(i);
        }
        /*
         * because no solution can start from matrix elements 2,4,6 or 8,
         * for the sake of optimization, the above list can be written as
         * Arrays.asList(1,3,5,7,9)
         * the way it is right now is useful to follow the way the program
         * does the backtracking, when it accidentally starts with either 2,4,6 or 8
         */
        Collections.shuffle(values);//a random permutation of the list
        main.candidates.put(1, values);
        main.setSize(size);
        int[] sol = main.getSol(1);
        if(sol==null)
            return null;
        Double d=Math.sqrt(size);
        for(int index=0;index<size-1;index++){
            solution[sol[index]-1][sol[index+1]-1]=1;
            solution[sol[index+1]-1][sol[index]-1]=1;
        }
        printMat(solution);
        return solution;
    }

    private boolean detect(ArrayList<Point> path,Point target){
        for(Point temp:path){
            if(temp.equals(target))
                return true;
        }
        return false;
    }

    private ArrayList<Point> getNeighbours(int size, Point current) {
        ArrayList<Point> ret=new ArrayList<>();
        if(current.x==0){
            if(current.y==0){
                ret.add(new Point(0,1));
                ret.add(new Point(1,1));
                ret.add(new Point(1,0));
            }
            else if(current.y==size-1){
                ret.add(new Point(0,size-2));
                ret.add(new Point(1,size-1));
                ret.add(new Point(1,size-2));
            }
            else{
                ret.add(new Point(0,current.y-1));
                ret.add(new Point(1,current.y-1));
                ret.add(new Point(1,current.y));
                ret.add(new Point(1,current.y+1));
                ret.add(new Point(0,current.y+1));
            }
        }
        else if(current.x==size-1){
            if(current.y==0){
                ret.add(new Point(size-2,0));
                ret.add(new Point(size-2,1));
                ret.add(new Point(size-1,1));
            }
            else if(current.y==size-1){
                ret.add(new Point(size-1,size-2));
                ret.add(new Point(size-2,size-2));
                ret.add(new Point(size-2,size-1));
            }
            else{
                ret.add(new Point(current.x,current.y-1));
                ret.add(new Point(current.x-1,current.y-1));
                ret.add(new Point(current.x-1,current.y));
                ret.add(new Point(current.x-1,current.y+1));
                ret.add(new Point(current.x,current.y+1));
            }
        }
        else if(current.y==0){
            ret.add(new Point(current.x-1,0));
            ret.add(new Point(current.x-1,1));
            ret.add(new Point(current.x,1));
            ret.add(new Point(current.x+1,1));
            ret.add(new Point(current.x+1,0));
        }
        else if(current.y==size-1){
            ret.add(new Point(current.x-1,current.y));
            ret.add(new Point(current.x-1,current.y-1));
            ret.add(new Point(current.x,current.y-1));
            ret.add(new Point(current.x+1,current.y-1));
            ret.add(new Point(current.x+1,current.y));
        }
        else{
            ret.add(new Point(current.x-1,current.y-1));
            ret.add(new Point(current.x-1,current.y));
            ret.add(new Point(current.x-1,current.y+1));
            ret.add(new Point(current.x,current.y-1));
            ret.add(new Point(current.x,current.y+1));
            ret.add(new Point(current.x+1,current.y-1));
            ret.add(new Point(current.x+1,current.y));
            ret.add(new Point(current.x+1,current.y+1));
        }
        return ret;
    }

    public void setP(double p) {
        this.p = p;
    }
}
