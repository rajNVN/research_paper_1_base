package btopskQuery;

import nodeProcessing.NodeProcess;
import util.Input;
import util.preprocessing.Object;
import util.preprocessing.Quadtree;

import java.util.*;

public class BtopskQuery {
    public static int noObjects = 0;

    public static Map<String, HashMap<Double, Object>> btopSk(Input input) {
        ArrayList<String> Q = input.Query;
        //line 1
        Map<String, HashMap<Double, Object>> R = new HashMap<>();
        Stack<VirtualQuadtree> H = new Stack<>();
        double maxDistanceQuery = 1000000000;
        VirtualQuadtree e = new VirtualQuadtree();

        //line 3
        long start4 = System.nanoTime();
        for (String subQuery : Q) {
            R.put(subQuery, new HashMap<>());
            e.addSubQuery(subQuery);

            //line 8
            for (String term : subQuery.split(" ")) {
                if (!e.isTermPresent(term)) {
                    e.addTerm(term);
                    //e.addTermandRoot(term, Quadtrees.constuctQuadtree(term));
                    e.addTermandRoot(term, Quadtree.constructQuadtreeFromCluster(term, input));
                }
            }
        }
/*
        System.out.println("<=============After first phase : ===================>");
        System.out.println("Queries : ");
        for(String subQuery:e.getQ())
            System.out.println(subQuery+"--->"+R.get(subQuery));
        System.out.println("Terms : ");
        for(String term:e.T)
            System.out.println(term+"--->"+e.termandNode.get(term));*/
        e.syncRootNode();

        //line 12
        //H.add(e);
        H.push(e);
        //line 13
        while (!H.isEmpty()) {
            e = H.pop();
//            System.out.println("<=====================================================" + e + "level " + e.level);
            //          System.out.println(e.min(input) + "\n");

            //line 15 & 16
            if (e.min(input) >= maxDistanceQuery)
                break;
            //for line 17
            ArrayList<String> dashQ = new ArrayList<>();

//            System.out.println("Q_dash");

            //line 17
            for (String subQuery : e.getQ()) {
                int count = 0;
                for (String term : subQuery.split(" "))
                    if (e.isTermPresent(term))
                        count++;
                if (count == subQuery.split(" ").length) {
//                    System.out.println("adding query "+subQuery);

                    dashQ.add(subQuery);
                }
            }
            //line 18 & 19

//            System.out.println("\nQ_dash is Empty : " + dashQ.isEmpty()+"\n");

            if (dashQ.isEmpty())
                continue;
            //line 20
            for (String term : e.T) {

//                System.out.println("util.Object list of childrens for term " + term + " is " + !e.isChildrenObjectsEmpty(term));

                if (!e.isChildrenObjectsEmpty(term))
                    e.syncChildrens(term);
            }

//            System.out.println("\nchildrens returned-> ");
//            for (VirtualQuadtree child : e.getChildrens())
//                System.out.print(child + "           ");

            //line 23
            for (VirtualQuadtree child : e.getChildrens()) {
//                System.out.println("\n                  <----------------------------------" + child);

                // if (true && true) {
                //line 25
                ArrayList<String> starQ = new ArrayList<>();

                //                   System.out.println("Q_star");

                for (String subQuery : dashQ) {
                    int count = 0;
                    for (String term : subQuery.split(" "))
                        if (child.isTermPresent(term))
                            count++;
                    if (count == subQuery.split(" ").length) {
                        starQ.add(subQuery);

                        //                           System.out.println(subQuery + " query is added");

                    }
                }
                //line 26
                if (!starQ.isEmpty()) {

                    input.Q_star = starQ;
                    input.e = child;
                    input.R = R;

                    //line 27
                    if (child.level >= input.minDepth)
                        //line 28
                        //System.out.println("fourth");
                        NodeProcess.nodeProcess(input);
                        //line 29
                    else {
                        //line 30
                        child.setQ(starQ);
                        //line 31
                        H.push(child);
                    }
                }
            }
        }
        //}
        System.out.println("\n<^><^><^><^><^><^><^><^> TOTAL TIME For Fourth Algorithm: " + (double) (System.nanoTime() - start4) / 1000000000+" <^><^><^><^><^><^><^><^>");
        //line 32
        return input.R;
    }
}
