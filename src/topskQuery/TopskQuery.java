package topskQuery;

import util.Input;
import util.preprocessing.Object;
import util.preprocessing.Quadtree;
import util.preprocessing.TYPE;

import java.util.*;


public class TopskQuery {
    private static boolean signFlag;

    private static boolean checkSignature(String mCode, Quadtree e) {
        if (mCode.equals("")) {
            signFlag = true;
        } else
            switch (mCode.substring(0, 2)) {
                case "00":
                    if (!e.SW.getType().equals(TYPE.WHITE_LEAF))
                        checkSignature(mCode.substring(2), e.SW);
                    break;
                case "01":
                    if (!e.SE.getType().equals(TYPE.WHITE_LEAF))
                        checkSignature(mCode.substring(2), e.SE);
                    break;
                case "10":
                    if (!e.NW.getType().equals(TYPE.WHITE_LEAF))
                        checkSignature(mCode.substring(2), e.NW);
                    break;
                case "11":
                    if (!e.NE.getType().equals(TYPE.WHITE_LEAF))
                        checkSignature(mCode.substring(2), e.NE);
            }
        return signFlag;
    }


    public static List<Object> topSk(int user_index, Input input) throws Exception {
        long start = System.nanoTime();
        Map<String, Quadtree> quadtrees = new HashMap<>();
        Quadtree node = new Quadtree();
        for (String term : input.getUserTerms(user_index)) {
            node = Quadtree.constuctQuadtree(term);
            quadtrees.put(term, node);
        }
        Queue<Quadtree> H = new LinkedList<>();
        boolean singatureTestPassed, flag;
        List<Object> R = new ArrayList<>();
        List<Quadtree> qtree_roots = new ArrayList<>();
        if (input.getUserTerms(user_index).size() != 1) {
            int x = 0;
            for (String term : input.getUserTerms(user_index))
                if (x == 0) {
                    H.add(quadtrees.get(term));
                    x++;
                } else
                    qtree_roots.add(quadtrees.get(term));

            while (!H.isEmpty()) { //loop at line 4
                Quadtree e = H.poll();
                if (e.getType().equals(TYPE.BLACK_LEAF)) { //if loop at line 6
                    singatureTestPassed = true;
                    for (Quadtree qtree_root : qtree_roots) { //for loop at line 8
                        signFlag = false;
                        flag = checkSignature(e.getSign(), qtree_root);
                        //System.out.println("checkSignature(\"" + e.getSign() + "\"," + qtree_root + ")--->" + flag);
                        if (!flag) {
                            singatureTestPassed = false;
                            break;
                        }
                    }
                    if (singatureTestPassed) {
                        /*ArrayList<Object> objectList = e.getObjectList();
                        int oHit = 0;
                        double minDistance = 1000000000;
                        for (Object object : objectList) {
                            for(String word:object.getName().split(" ")){
                                if (input.getUserTerms(user_index).contains(word))
                                    oHit++;
                            }
                            if (oHit == quadtrees.size()) {
                                double distance = Calculator.calculateDistance(input.getLatitude(user_index), input.getLongitude(user_index), input.getLatitude(user_index), input.getLongitude(user_index));
                                if (distance >= minDistance)
                                    break;
                                else {
                                    minDistance = distance;
                                    R.add(object);
                                }
                            }
                        }*/
                        R.addAll(e.getAllObjects());
                    }
                } else {
                    if (!e.SW.getType().equals(TYPE.WHITE_LEAF))
                        H.add(e.SW);
                    if (!e.SE.getType().equals(TYPE.WHITE_LEAF))
                        H.add(e.SE);
                    if (!e.NW.getType().equals(TYPE.WHITE_LEAF))
                        H.add(e.NW);
                    if (!e.NE.getType().equals(TYPE.WHITE_LEAF))
                        H.add(e.NE);
                }
            }
        } else
            R.addAll(node.getAllObjects());
        System.out.println("\nTotal Objects Returned from First Algorithm :" + R.size());
        System.out.println("time taken : " + (double) (System.nanoTime() - start) / 1000000000);
        return R;
    }
}
