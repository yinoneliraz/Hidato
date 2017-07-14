import sun.rmi.server.InactiveGroupException;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;



public class buildPath {

    int[] solution;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    int size;
    HashMap<Integer, List<Integer>> candidates;

    public int[] getSol(int k){
        buildSol(k);
        return solution;
    }

    //backtracking
    public void buildSol(int k)
    {
        if(k==size+1)
        {
            System.out.print("the solution is ");
            return;
        }

        List<Integer> candList = candidates.get(k);
        if(candList.isEmpty())
        {
            cleanSolution(k);
            buildSol(k-1); //if no candidates for current step, go one step back
        }
        else
        {
            int firstCandidate = candList.get(0);
            candList.remove(0);
            candidates.put(k, candList);
            solution[k-1] = firstCandidate;//for the position k in the solution, pick the first element in the candidates list
            Double d=Math.sqrt(size);
            int index=solution[k-1]-1;
            Point current=new Point(index/d.intValue(),index%d.intValue());
            List<Point> neighborsPoint = getNeighbors(current);
            List<Integer> neighbors=new ArrayList<>();
            for(Point p:neighborsPoint){
                neighbors.add(p.x*d.intValue()+p.y+1);
            }
            List<Integer> prevElems = getPreviousElementsInSolution(k);
            candidates.put(k+1, generateCandidates(neighbors, prevElems));//while being at step k, generate candidate elements for step k+1
            //these candidates are the neighbors (in the matrix) of the current element (solution[k]),
            //which are not already part of the solution at an earlier position

            System.out.println("step "+k);
            System.out.print("partial solution: ");
            System.out.println();


            buildSol(k+1);//go to next step
        }
    }



    //candidates are those elements which are neighbors, and have not been visited before
    public List<Integer> generateCandidates(List<Integer> neighbors, List<Integer> previousElements)
    {
        List<Integer> cnd = new ArrayList<Integer>();
        for(int i=0;i<neighbors.size();i++)
            if(!previousElements.contains(neighbors.get(i)))
                cnd.add(neighbors.get(i));

        return cnd;
    }

    //get the set of previous elements in the solution, up to  solution[k]
    public List<Integer> getPreviousElementsInSolution(int step)
    {
        List<Integer> previousElements = new ArrayList<Integer>();
        for(int i=0; i<=step-1;i++)
            previousElements.add(solution[i]);

        return previousElements;
    }

    //get neighbors of the matrix element which corresponds to solution[k]
    private ArrayList<Point> getNeighbors(Point current) {
        ArrayList<Point> ret=new ArrayList<>();
        Double d=Math.sqrt(this.size);
        int size=d.intValue();
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

//    public  List<Integer> getNeighbors(int element) {
//
//        List<Integer> neighboursList = new ArrayList<Integer>();
//
//        switch (element) {
//
//            case 1: neighboursList = Arrays.asList(2, 4);
//                break;
//
//            case 2: neighboursList = Arrays.asList(1, 3, 5);
//                break;
//
//            case 3: neighboursList = Arrays.asList(2, 6);
//                break;
//
//            case 4: neighboursList = Arrays.asList(1, 5, 7);
//                break;
//
//            case 5: neighboursList = Arrays.asList(2, 4, 6, 8);
//                break;
//
//            case 6: neighboursList = Arrays.asList(3, 5, 9);
//                break;
//
//            case 7: neighboursList = Arrays.asList(4, 8);
//                break;
//
//            case 8: neighboursList = Arrays.asList(5, 7, 9);
//                break;
//
//            case 9: neighboursList = Arrays.asList(6, 8);
//                break;
//
//            default: neighboursList = new ArrayList<Integer>();
//                break;
//        }
//
//        return neighboursList;
//    }


    public void cleanSolution(int k)
    {
        for(int i=k;i<solution.length;i++)
            solution[i]=0;
    }
}
