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
        main.candidates = new HashMap<>();//for each position (1 to 9) in the solution, stores a list of candidate elements for that position

        java.util.List<Integer> values = new LinkedList<>();
        for(int i=1;i<=size;i++){
            values.add(i);
        }
        Collections.shuffle(values);//a random permutation of the list
        for(int index=0;index<size-1;index++){
            solution[values.get(index)-1][values.get(index+1)-1]=1;
            solution[values.get(index+1)-1][values.get(index)-1]=1;
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
        for(int i=0;i<size;i++){
            ret.add(new Point(i/size,i%size));
        }
        return ret;
    }

    public void setP(double p) {
        this.p = p;
    }

    public int[][] maskGraph(int[][] graph) {
        int[][] masked=new int[graph.length][graph.length];
        for(int i=0;i<graph.length;i++){
            Double d=Math.sqrt(graph.length);
            Point current=new Point(i/d.intValue(),i%d.intValue());
            java.util.List<Point> neighborsPoint = getNeighbours(d.intValue(),current);
            java.util.List<Integer> neighbors=new ArrayList<>();
            for(Point p:neighborsPoint){
                neighbors.add(p.x*d.intValue()+p.y);
            }
            for(int j=i+1;j<graph.length;j++){
                double x=rnd.nextDouble();
                if(x<p){
                    masked[i][j]=1;
                    masked[j][i]=1;
                }
                else{
                    masked[i][j]=graph[i][j];
                    masked[j][i]=graph[j][i];
                }
            }
        }
        return masked;
    }
}
