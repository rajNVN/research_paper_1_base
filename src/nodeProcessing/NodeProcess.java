package nodeProcessing;

import btopskQuery.BtopskQuery;
import btopskQuery.VirtualQuadtree;
import util.Calculator;
import util.Input;
import util.preprocessing.Object;
import util.preprocessing.TYPE;

import java.util.*;

public class NodeProcess {
    public static void nodeProcess(Input input) {
        System.out.println("\n<><><><><><> Entered Firth Algorithm<><><><><><><><><><><><><><>");
        BtopskQuery.noObjects++;
        Map<String, HashMap<Double, Object>> R = input.R;
        VirtualQuadtree e = input.e;
        List<String> Q = input.Q_star;
        long start5 = System.nanoTime();
        //line 1
        for (String subQuery : Q) {
            //line 2
            int hit = 0;
            //line 3
            for (String term : subQuery.split(" ")) {
                //line 4
                if (e.termandNode.get(term).getType().equals(TYPE.BLACK_LEAF)) {
                    //line 5
                    hit++;
                    //line 6
                    if (e.termandObjects.get(term).isEmpty())
                        //line 7
                        e.termandObjects.get(term).addAll(e.termandNode.get(term).getObjectList());
                }
            }
            //System.out.println("hit count is " + hit + " & terms.size() is " + subQuery.split(",").length);
            //line 8
            if (hit == subQuery.split(" ").length) {
                //line 9
                ArrayList<Object> objs = e.getObjectList();
                double maxDistance = 1000000000;
                //line 10
                for (Object object : objs) {
                    //for line 11
                    double distance = Calculator.calculateDistance(input.getLatitude(input.getIndex(subQuery)), input.getLongitude(input.getIndex(subQuery)), object.getLatitude(), object.getLongitude());
                    //line 11
                    if (distance < maxDistance) {
                        //line 12
                        System.out.print("Object added--> ");
                        object.print();
                        R.get(subQuery).put(distance, object);
                        //line 13
                        maxDistance = distance;
                    }
                }
            } else
                e.addSubQuery(subQuery);
        }
        //System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^results^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        /*for(Map.Entry<String,HashMap<Double, Object>> entry:R.entrySet()){
            String query=entry.getKey();
            int count=0;
            Map<Double, Object> map=new TreeMap<>(entry.getValue());
            for(Map.Entry<Double, Object> entry1:map.entrySet()) {
                if(count>5)
                    break;
                else
                    System.out.println(query + " ---> " + entry1);
                count++;
            }
        }*/
        System.out.println(" AND time taken : " + (System.nanoTime() - start5) / 1000 + " microSeconds");
    }
}
