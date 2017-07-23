import java.awt.*;
import java.util.*;

public class HamiltonPath {
    static int [][] constraints =   {
                                        {0  ,0  ,0 },
                                        {0  ,0  ,0 },
                                        {0  ,5  ,0 },
                                    };
    static ArrayList<Integer>[] possibleLocations=new ArrayList[9];

    static ArrayList<int[]> solutions=new ArrayList<>();

    public static void main(String[] args) {
        for(int i=0;i<9 ;i++){
            //init possible locations arrays
            possibleLocations[i]=new ArrayList<>();
        }
        checkGraph nGraph=new checkGraph(9,0);//create an instance of class checkGraph
        HamiltonPath obj = new HamiltonPath();
        int[][] graph =nGraph.getGraph();//get graph with single random hamilton path
        obj.printMat(graph);
        obj.allHamiltonPath(graph,solutions);//find all possible solutions of hamilton graph
        nGraph.maskGraph(graph);//add random edges to graph to mask it
        count=0;
        ArrayList<int[]>[] maskedsolutions=new ArrayList[10];// create arrays to store solutions of random graphs


        maskedsolutions[0]=new ArrayList<>();
        nGraph.setP(0.55);
        int[][] masked=nGraph.maskGraph(graph);
        obj.printMat(masked);
        obj.allHamiltonPath(masked, maskedsolutions[0]);
        if(compareSolutions(solutions,maskedsolutions[0])){//check if we got the solution we expected
            System.out.println("Got correct solution");
        }
        else{
            System.out.println("Didnt find correct solution");
        }
        System.out.println("Found " + maskedsolutions[0].size() + " solutions");
        if(maskedsolutions[0].size()>0){
            System.out.println("One solution is:");
            for(int i=0;i<maskedsolutions[0].get(0).length;i++){
                System.out.print(maskedsolutions[0].get(0)[i] + ",");
            }
        }
        System.out.println();
        int counter=1;
        for(ArrayList<Integer> location:possibleLocations){
            System.out.println("Possible locations for " + counter + " place:");
            for(Integer i:location){
                System.out.print(i + ", ");
            }
            counter++;
            System.out.println();

        }
    }

    private static boolean compareSolutions(ArrayList<int[]> solutions, ArrayList<int[]> maskedsolutions) {
        for(int[] sol:solutions){
            for(int[] masked : maskedsolutions){
                if (Arrays.equals(masked,sol)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkGraphValidity(int[][] graph) {
        int index=1;
        int dist=0;
        while(index<graph.length){
            Point a=findValue(constraints,index);
            while(a==null) {
                index++;
                dist++;
                a = findValue(constraints, index);
            }
            index++;
            dist++;
            Point b=findValue(constraints,index);
            while(b==null) {
                index++;
                if(index>graph.length)
                    return true;
                dist++;
                b = findValue(constraints, index);
            }
            if(!existPath(graph,a,b,dist))
                return false;
        }
        return true;
    }

    private boolean existPath(int[][] graph, Point a, Point b, int dist) {
        ArrayList<Integer> res=getNeighboursByDistance(graph,dist,a.x* constraints.length+a.y);
        return res.contains(b.x* constraints.length+b.y);
    }

    static int len;
    static int[] path;
    static int count = 0;

    public int[][] hidatoToGraph(int[][] hidato){

        Point last=findValue(hidato,hidato.length*hidato[0].length);
        int[][] graph;
        if(last!=null){
            graph= new int[hidato.length*hidato[0].length+1][hidato.length*hidato[0].length+1];
        }
        else{
            graph= new int[hidato.length*hidato[0].length][hidato.length*hidato[0].length];
        }
        for(int i=1;i<graph.length;i++) {
            Point current = findValue(hidato, i);
            Point next = findValue(hidato, i + 1);
            if(current!=null){
                if (next == null) {
                    makeAllNeighbours(graph, hidato, current,i);
                } else {
                    makeTwoNeighbours(graph, hidato, current, next);
                }
            }
        }
        Point current;
        current=findValue(hidato,0);
        while(current!=null){
            current=findValue(hidato,0);
            if(current!=null){
                makeAllNeighbours(graph, hidato, current,hidato.length*hidato.length);
                hidato[current.x][current.y]=-1;
            }
            current=findValue(hidato,0);
        }
        int size=hidato[0].length;
        if(last!=null) {
            graph[last.x * size + last.y][hidato.length * hidato[0].length] = 1;
            graph[hidato.length * hidato[0].length][last.x * size + last.y] = 1;
        }
        printMat(graph);
        return graph;

    }

    private void findValuesByDistance(int[][] hidato, int[][] graph){
        int start=1;
        Point temp=findValue(hidato,start);
        while(temp==null){
            start++;
            temp=findValue(hidato,start);
        }
        int min=temp.x*hidato.length+temp.y;
        int max=min,i;
        for(i=start+1;i<=hidato.length*hidato.length;i++){
            temp=findValue(hidato,i);
            if(temp!=null){
                max=temp.x*hidato.length+temp.y;
                break;
            }
        }
        if(max==min)
            return;
        ArrayList<Integer> neighbours=new ArrayList<>();
        int oldMax=0,oldMin=0;
        while(hidato[min/hidato.length][min%hidato.length]<graph.length-1 && oldMax!=max && oldMin!=min) {
            oldMin=min;
            oldMax=max;
            int diff=hidato[max/hidato.length][max%hidato.length] - hidato[min/hidato.length][min%hidato.length];
            for (i = 1; i < diff; i++) {
                neighbours.add(min);
                ArrayList<Integer> minNeighbours = getNeighboursByDistance(graph, i, min);
                neighbours.clear();
                neighbours.add(max);
                ArrayList<Integer> maxNeighbours = getNeighboursByDistance(graph, diff-i, max);
                ArrayList<Integer> intersection=new ArrayList<>();
                for (Integer minNode : minNeighbours) {
                    for (Integer maxNode : maxNeighbours) {
                        if(minNode>=hidato.length*hidato.length)
                            continue;
                        if (minNode.equals(maxNode) &&
                                hidato[(minNode) / hidato.length][(minNode) % hidato.length] <= 0)
                            intersection.add(maxNode);
                    }
                }
                if(intersection.size()==1){
                    int node= intersection.get(0);
                    System.out.println("Found node, value " + (hidato[min / hidato.length][min % hidato.length] + i)
                            + " between " + hidato[min / hidato.length][min % hidato.length] + " and " + hidato[max / hidato.length][max % hidato.length]);
                    hidato[node/hidato.length][node%hidato.length]=hidato[min/hidato.length][min%hidato.length]+i;
                    printMat(hidato);
                }
            }
            neighbours.clear();
            min=max;
            for(i=hidato[min/hidato.length][min%hidato.length]+1;i<=hidato.length*hidato.length;i++){
                temp=findValue(hidato,i);
                if(temp!=null){
                    max=temp.x*hidato.length+temp.y;
                    break;
                }
            }
        }
    }

    private ArrayList<Integer> getNeighboursByDistance(int[][] graph, int d, int source) {
        if(d==0)
            return null;
        int[] nodeVisited=new int[graph.length];
        Arrays.fill(nodeVisited,0);
        LinkedList<Integer> q=new LinkedList();
        q.add(source);
        nodeVisited[source]=1;
        while(d>0){
            LinkedList<Integer> tempQ=new LinkedList();
            while(q.size()>0){
                Integer n=q.poll();
                for(int i=0;i<graph.length;i++){
                    if(graph[n][i]==1 && !tempQ.contains(i)){
                        tempQ.add(i);
                        nodeVisited[i]=1;
                    }
                }
            }
            q=tempQ;
            d--;
        }
        ArrayList<Integer> ret=new ArrayList<>();
        for(int i=0;i<nodeVisited.length;i++){
            if(nodeVisited[i]==1)
                ret.add(i);
        }
        return ret;
    }

    private Point findValue(int[][] hidato,int value){
        Point ret=null;
        for(int i=0;i<hidato.length;i++){
            for(int j=0;j<hidato[i].length;j++){
                if(hidato[i][j]==value){
                    ret=new Point(i,j);
                }
            }
        }
        return ret;
    }

    private void makeTwoNeighbours(int[][] graph, int[][] hidato, Point current, Point next){
        if(!getNeighbours(hidato,current).contains(next))
            return;
        int size=hidato[0].length;
        graph[current.x*size+current.y][next.x*size+next.y]=1;
        graph[next.x*size+next.y][current.x*size+current.y]=1;
    }

    private void makeAllNeighbours(int[][] graph, int[][] hidato, Point current, int index){
        ArrayList<Point> neighbours=getNeighbours(hidato,current);
        for(Point neighbour: neighbours){
            makeTwoNeighbours(graph,hidato,current,neighbour);
        }
    }

    private ArrayList<Point> getNeighbours(int[][] hidato, Point current) {
        ArrayList<Point> ret=new ArrayList<>();
        if(current.x==0){
            if(current.y==0){
                ret.add(new Point(0,1));
                ret.add(new Point(1,1));
                ret.add(new Point(1,0));
            }
            else if(current.y==hidato.length-1){
                ret.add(new Point(0,hidato.length-2));
                ret.add(new Point(1,hidato.length-1));
                ret.add(new Point(1,hidato.length-2));
            }
            else{
                ret.add(new Point(0,current.y-1));
                ret.add(new Point(1,current.y-1));
                ret.add(new Point(1,current.y));
                ret.add(new Point(1,current.y+1));
                ret.add(new Point(0,current.y+1));
            }
        }
        else if(current.x==hidato.length-1){
            if(current.y==0){
                ret.add(new Point(hidato.length-2,0));
                ret.add(new Point(hidato.length-2,1));
                ret.add(new Point(hidato.length-1,1));
            }
            else if(current.y==hidato.length-1){
                ret.add(new Point(hidato.length-1,hidato.length-2));
                ret.add(new Point(hidato.length-2,hidato.length-2));
                ret.add(new Point(hidato.length-2,hidato.length-1));
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
        else if(current.y==hidato.length-1){
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

    public void allHamiltonPath(int[][] x, ArrayList<int[]> solutions) {  //List all possible Hamilton path in the graph
        len = x.length;
        path = new int[len];
        int i;
        for (i = 0; i < len; i++) { //Go through column(of matrix)
            path[0] = i + 1;
            findHamiltonpath(x, 0, i, 0,solutions);
        }
    }

    public void HamiltonPath(int[][] x, int start) { //List all possible Hamilton path with fixed starting point
        len = x.length;
        path = new int[len];
        int i=start - 1;
        path[0] = i + 1;
        findHamiltonpath(x, 0, i, 0, solutions);
    }

    private void findHamiltonpath(int[][] M, int x, int y, int l, ArrayList<int[]> solutions){

        int i;
        for(i=x;i<len;i++){         //Go through row
            //Start constraints
            l++;
            Point want;
            want=findValue(constraints,l+1);//find the desired location
            l--;
            if(want!=null && want.x* constraints.length+want.y!=i) {
                continue;
            }
            //End constraints
            if(M[i][y]!=0){      //2 point connect

                if(detect(path,i+1))// if detect a point that already in the path => duplicate
                    continue;
                l++;            //Increase path length due to 1 new point is connected
                path[l]=i+1;    //correspond to the array that start at 0, graph that start at point 1
                if(l==len-1){   //Except initial point already count =>success connect all point
                    count++;
                    if (count ==1)
                        System.out.println("Hamilton path of graph: ");
                    int[] addToList=Arrays.copyOf(path,path.length);
                    solutions.add(addToList);
                    for(int k=0;k<path.length;k++){
                        if(!possibleLocations[k].contains(path[k]))
                            possibleLocations[k].add(path[k]);
                    }
                    l--;
                    continue;
                }

                M[i][y]=M[y][i]=0;  //remove the path that has been get and
                findHamiltonpath(M,0,i,l, solutions); //recursively start to find new path at new end point
                l--;                // reduce path length due to the failure to find new path
                M[i][y] = M[y][i]=1; //and tranform back to the inital form of adjacent matrix(graph)
            }
        }path[l+1]=0;    //disconnect two point correspond the failure to find the..
    }                     //possible hamilton path at new point(ignore newest point try another one)

    private boolean checkPath(int[][] hidato, int[] path) {
        int count=1;
        for(int index:path){
            Point current=new Point(index/hidato.length,index%hidato.length);
            if(hidato[current.x][current.y]>0 && hidato[current.x][current.y]!=count){
                return false;
            }
            count++;
        }
        return true;
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

    private boolean neighbourWith(int[][] m, Point next, Point first) {
        if(first==null || next==null)
            return false;
        return (m[constraints.length*next.x +next.y][constraints.length*first.x +first.y]==1);
    }

    public void display(int[] x) {
        System.out.print(count+" : ");
        for(int i:x){
            System.out.print(i+" ");
        }
        int[][] finalHidato=new int[constraints.length][constraints[0].length];
        System.out.println();
        for(int k=0;k<path.length;k++) {
            if(k>= constraints.length* constraints.length)
                continue;
            int row,col;
            row=(path[k]-1)/finalHidato.length;
            col=(path[k]-1)%finalHidato.length;
            finalHidato[row][col]=k+1;
        }
        printMat(finalHidato);
        System.out.println();
    }

    private boolean detect(int[] x, int target) { //Detect duplicate point in Halmilton path
        boolean t = false;
        for (int i : x) {
            if (i == target) {
                t = true;
                break;
            }
        }
        return t;
    }
}