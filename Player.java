import java.util.*;
import java.io.*;
import java.math.*;
import java.awt.Point;


class PointQueue implements Comparable<PointQueue>{
    private Point point;
    private Integer priority;
    
    PointQueue(Point point, Integer priority){
        super();
        this.point = point;
        this.priority = priority;
    }
    
    public Point getPoint(){
        return this.point;
    }
    
    public Integer getPriority(){
        return this.priority;
    }
    
    public int compareTo(PointQueue pq){
        return this.getPriority().compareTo(pq.getPriority());
    }
}
 
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int R = in.nextInt(); // number of rows.
        int C = in.nextInt(); // number of columns.
        int A = in.nextInt(); // number of rounds between the time the alarm countdown is activated and the time the alarm goes off.
        String[] grid = new String[R];
        Point exit = null;
        Point entrance = null;
        Stack directionStack = new Stack();
        Stack exitPath = new Stack();
        //Stack frontier = new Stack();
        // game loop
        while (true) {
            
            int KR = in.nextInt(); // row where Kirk is located.
            int KC = in.nextInt(); // column where Kirk is located.
            Point currentPoint = new Point(KC, KR);
            if(entrance == null){
                entrance = new Point ((int)currentPoint.getX(), (int)currentPoint.getY());
            }
            for (int i = 0; i < R; i++) {
                grid[i] = in.next(); // C of the characters in '#.TC?' (i.e. one line of the ASCII maze.
            }
            
            
            
            if(exit == null)
            {
                System.err.println("EXIT NOT FOUND");
                exit = perimeterScan(grid, KC, KR);
                if(directionStack.empty())
                {
                    pathAlg(grid, directionStack, currentPoint);
                } 
                Point direction = (Point)directionStack.pop();
                System.err.println("DIRECTION TO TRAVEL: " + direction);
                movement(currentPoint, direction);
                
            } 
            else 
            {
                if (exitPath.empty())
                {
                    System.err.println("EXIT FOUND");
                    pathAlg(grid, exitPath, exit, entrance);
                    if(exitPath.size() <= A && exitPath.size() > 0){
                        System.err.println("EXIT PATH FOUND 1");
                        directionStack.clear();
                        pathAlg(grid, directionStack, currentPoint, exit);
                        Point direction = (Point)directionStack.pop();
                        System.err.println(direction);
                        movement(currentPoint, direction);
                    } 
                    else 
                    {
                        exitPath.clear();
                        if(directionStack.empty()){
                            pathAlg(grid, directionStack, currentPoint);
                        } 
                        Point direction = (Point)directionStack.pop();
                        System.err.println(direction);
                        movement(currentPoint, direction);
                      
                    }
                } 
                else 
                {
                    System.err.println("EXIT PATH FOUND 2");
                    if(directionStack.empty()){
                        System.err.println("EXIT PATH FOUND 3");
                        Point direction = (Point)exitPath.pop();
                        System.err.println(direction);
                        movement(currentPoint, direction);
                      
                    } else {
                        Point direction = (Point)directionStack.pop();
                        System.err.println(direction);
                        movement(currentPoint, direction);
                      
                    }
                }
            }
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            //System.out.println("RIGHT"); // Kirk's next move (UP DOWN LEFT or RIGHT).
        }
    }
    
    public static void movement(Point origin, Point destination){
        int x = (int)(destination.getX() - origin.getX());
        int y = (int)(destination.getY() - origin.getY());
        if(x > 0){
            System.out.println("RIGHT"); // Kirk's next move (UP DOWN LEFT or RIGHT).
        } else if(x < 0){
            System.out.println("LEFT"); // Kirk's next move (UP DOWN LEFT or RIGHT).
        } else if(y < 0){
            System.out.println("UP"); // Kirk's next move (UP DOWN LEFT or RIGHT).
        } else if(y > 0){
            System.out.println("DOWN"); // Kirk's next move (UP DOWN LEFT or RIGHT).
        }
    }
    
    public static Point perimeterScan(String[] grid, int KC, int KR){
            
        for(int i = KR - 2; i <= KR + 2; i++){
            if(i > 0 && i < grid.length){
                if(KC - 2 > 0){
                    if(grid[i].charAt(KC - 2) == 'C'){
                        return new Point(KC - 2, i);
                    }
                }
                if(KC + 2 < grid[i].length()){
                    if(grid[i].charAt(KC + 2) == 'C'){
                        return new Point(KC + 2, i);
                    }
                }
            }
        }
        
        for(int i = KC - 1; i <= KC + 1; i++){
            if(i > 0 && i < grid[KR].length()){
                if(KR - 2 > 0){
                    if(grid[KR - 2].charAt(i) == 'C'){
                        return new Point(i, KR - 2);
                    }
                }
                if(KR + 2 < grid.length){
                    if(grid[KR + 2].charAt(i) == 'C'){
                        return new Point(i, KR + 2);
                    }
                }
            }
        }
        return null;
    }
    
    public static Point searchAlg(String[] grid, Point origin, char destination){
            Queue<Point> searchQueue = new LinkedList<>();
            boolean[][] searched = new boolean[grid[0].length()][grid.length];
            searchQueue.add(origin);
            searched[(int)origin.getX()][(int)origin.getY()] = true;
            while(!searchQueue.isEmpty()){
                Point currentLocation = searchQueue.remove();
                   
                int x = (int)currentLocation.getX();
                int y = (int)currentLocation.getY();
                   
                if(grid[y].charAt(x) == destination){
                    System.err.println("UNEXPLORED POINT: X:" + x + " Y:" + y);
                    return currentLocation;
                }
                
                if(!searched[x + 1][y] && grid[y].charAt(x + 1) != '#' && x + 1 < grid[0].length()){
                    searchQueue.add(new Point(x + 1, y));
                    searched[x + 1][y] = true;
                }
                
                if(!searched[x - 1][y] && grid[y].charAt(x - 1) != '#' && x - 1 > 0){
                    searchQueue.add(new Point(x - 1, y));
                    searched[x - 1][y] = true;
                }
                
                if(!searched[x][y + 1] && grid[y + 1].charAt(x) != '#' && y + 1 < grid.length){
                    searchQueue.add(new Point(x, y + 1));
                    searched[x][y + 1] = true;
                }
                
                if(!searched[x][y - 1] && grid[y - 1].charAt(x) != '#' && y - 1 > 0){
                    searchQueue.add(new Point(x, y - 1));
                    searched[x][y - 1] = true;
                }
            }
            return origin;
    }
    
    public static boolean pathAlg(String[] grid, Stack path, Point origin, Point destination){
        Map direction = new HashMap();
        Map distance = new HashMap();
        PriorityQueue<PointQueue> frontier = new PriorityQueue<>();
        boolean success = false;
        
        frontier.add(new PointQueue(origin, 0));
        direction.put(origin, null);
        distance.put(origin, 0);
        System.err.println("PATHFINDING LOOP START: " + origin + " DESTINATION: " + destination);
        
        while(!frontier.isEmpty())
        {
            PointQueue currentSearch = frontier.remove();
            Point currentPoint = currentSearch.getPoint();
            int currentPriority = currentSearch.getPriority();
            //System.err.println("POINT: " + currentPoint + " PRIORITY: " + currentPriority);
            int x = (int)currentPoint.getX();
            int y = (int)currentPoint.getY();
            if(x == (int)destination.getX() && y == (int)destination.getY()){
                frontier.clear();
                success = true;
            } else {
                //queue neighbors
                Point[] neighbors = new Point[4];
                neighbors[0] = new Point(x + 1, y);
                neighbors[1] = new Point(x - 1, y);
                neighbors[2] = new Point(x, y + 1);
                neighbors[3] = new Point(x, y - 1);
                for(int i = 0; i < 4; i++)
                {
                    int u = (int)neighbors[i].getX();
                    int v = (int)neighbors[i].getY();
    
                    if(
                        grid[v].charAt(u) != '#' &&
                        grid[v].charAt(u) != '?' &&
                        u > 0 &&
                        v > 0 &&
                        u < grid[0].length() &&
                        v < grid.length &&
                        !direction.containsKey(neighbors[i])
                        ) 
                    {
                        int newDistance = 1 + (int)distance.get(currentPoint);
                        int newPriority = (int)Math.abs(u - destination.getX()) + (int)Math.abs(v - destination.getY()) + newDistance;
                        frontier.add(new PointQueue(neighbors[i], newPriority));
                        direction.put(neighbors[i], currentPoint);
                        distance.put(neighbors[i], newDistance);
                    }
                }
            }
        }
        if(success){
            Point tempPoint = destination;
            while(tempPoint != origin && tempPoint != null){
                System.err.println("STACK POINT: " + tempPoint);
                path.push(tempPoint);
                tempPoint = (Point)direction.get(tempPoint);
            }
        }
        return success;
    }
    
    public static boolean pathAlg(String[] grid, Stack path, Point origin){
        Map direction = new HashMap();
        Queue<Point> searchQueue = new LinkedList<>();
        Point destination = null;
        
        searchQueue.add(origin);
        direction.put(origin, null);
        System.err.println("PATHFINDING LOOP START: " + origin);
        while(!searchQueue.isEmpty()){
            Point currentPoint = searchQueue.remove();
            System.err.println("POINT: " + currentPoint);
            int x = (int)currentPoint.getX();
            int y = (int)currentPoint.getY();
            if(grid[y].charAt(x) == '?'){
                destination = currentPoint;
                searchQueue.clear();
                System.err.println("DESTINATION AT POINT: " + destination);
            } else {            
                Point[] neighbors = new Point[4];
                neighbors[0] = new Point(x + 1, y);
                neighbors[1] = new Point(x - 1, y);
                neighbors[2] = new Point(x, y + 1);
                neighbors[3] = new Point(x, y - 1);
                for(int i = 0; i < 4; i++){
                    int u = (int)neighbors[i].getX();
                    int v = (int)neighbors[i].getY();
                    if(
                        grid[v].charAt(u) != '#' &&
                        grid[v].charAt(u) != 'C' &&
                        u > 0 &&
                        v > 0 &&
                        u < grid[0].length() &&
                        v < grid.length &&
                        !direction.containsKey(neighbors[i])
                    )
                    {
                        direction.put(neighbors[i], currentPoint);
                        searchQueue.add(neighbors[i]);
                    }
                }
            }
        }
        if(destination != null){
            Point tracker = (Point)direction.get(destination);
            while(tracker != null && tracker != origin){
                System.err.println(tracker);
                path.push(tracker);
                tracker = (Point)direction.get(tracker);
            }
            return true;
        } else {
            return false;
        }
    }
}
