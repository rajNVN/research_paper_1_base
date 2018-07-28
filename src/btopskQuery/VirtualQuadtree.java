package btopskQuery;

import util.Calculator;
import util.Input;
import util.preprocessing.Object;
import util.preprocessing.Quadtree;
import util.preprocessing.TYPE;

import java.util.*;

public class VirtualQuadtree {
    private ArrayList<String> Q = new ArrayList<>();
    public Map<String, Quadtree> termandNode = new HashMap<>();
    public Map<String, ArrayList<Object>> termandObjects = new HashMap<>();
    public Set<String> T = new HashSet<>();
    private VirtualQuadtree SW;
    private VirtualQuadtree SE;
    private VirtualQuadtree NW;
    private VirtualQuadtree NE;
    int level;

    public void setQ(ArrayList<String> Q) {
        this.Q = Q;
    }

    public void addSubQuery(String query) {
        Q.add(query);
    }

    public void addTermandRoot(String term, Quadtree node) {
        termandNode.put(term, node);
    }

    public void addTerm(String term) {
        if (!T.contains(term))
            T.add(term);
    }

    public ArrayList<String> getQ() {
        return Q;
    }

    public boolean isTermPresent(String term) {
        for (String eachTerm : T)
            if (eachTerm.equals(term))
                return true;
        return false;
    }

    public void syncRootNode() {
        for (HashMap.Entry<String, Quadtree> entry : termandNode.entrySet())
            termandObjects.put(entry.getKey(), entry.getValue().getAllObjects());
        this.level = 0;
    }

    public ArrayList<Object> getObjectList() {
        ArrayList<Object> objectSet = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Object>> entry : termandObjects.entrySet())
            objectSet.addAll(entry.getValue());
        return objectSet;
    }

    public ArrayList<VirtualQuadtree> getChildrens() {
        ArrayList<VirtualQuadtree> virtualQuadtreeArrayList = new ArrayList<>();
        if (this.NW != null)
            virtualQuadtreeArrayList.add(NW);
        if (this.NE != null)
            virtualQuadtreeArrayList.add(NE);
        if (this.SW != null)
            virtualQuadtreeArrayList.add(SW);
        if (this.SE != null)
            virtualQuadtreeArrayList.add(SE);
        return virtualQuadtreeArrayList;
    }

    public double min(Input input) {
        double minDistance = 1000000000;
        for (String term : T)
            for (Object object : termandObjects.get(term))
                minDistance = Double.min(minDistance, Calculator.calculateDistance(input.latitude, input.longitude, object.getLatitude(), object.getLongitude()));
        return minDistance;
    }


    public boolean isChildrenObjectsEmpty(String term) {
        Quadtree node = this.termandNode.get(term);
        return node == null || node.getType().equals(TYPE.WHITE_LEAF);
    }


    void syncChildrens(String term) {
        Quadtree node = termandNode.get(term);
        if (!node.SW.getType().equals(TYPE.WHITE_LEAF)) {
            if (this.SW == null)
                this.SW = new VirtualQuadtree();
            this.SW.addTermandRoot(term, node.SW);
            this.SW.addTerm(term);
            this.SW.termandObjects.put(term, node.SW.getAllObjects());
            this.SW.level = this.level + 1;
        }
        if (!node.SE.getType().equals(TYPE.WHITE_LEAF)) {
            if (this.SE == null)
                this.SE = new VirtualQuadtree();
            this.SE.addTermandRoot(term, node.SE);
            this.SE.addTerm(term);
            this.SE.termandObjects.put(term, node.SE.getAllObjects());
            this.SE.level = this.level + 1;
        }
        if (!node.NW.getType().equals(TYPE.WHITE_LEAF)) {
            if (this.NW == null)
                this.NW = new VirtualQuadtree();
            this.NW.addTermandRoot(term, node.NW);
            this.NW.addTerm(term);
            this.NW.termandObjects.put(term, node.NW.getAllObjects());
            this.NW.level = this.level + 1;
        }
        if (!node.NE.getType().equals(TYPE.WHITE_LEAF)) {
            if (this.NE == null)
                this.NE = new VirtualQuadtree();
            this.NE.addTermandRoot(term, node.NE);
            this.NE.addTerm(term);
            this.NE.termandObjects.put(term, node.NE.getAllObjects());
            this.NE.level = this.level + 1;
        }
    }
}
