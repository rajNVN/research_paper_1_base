package util.preprocessing;


import util.DataBase.DBUtil;
import util.Input;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Quadtree {
    public Quadtree SW;
    public Quadtree NW;
    public Quadtree NE;
    public Quadtree SE;
    private String sign = "";
    private TYPE type = TYPE.WHITE_LEAF;
    private ArrayList<Object> objectList = new ArrayList<>();
    private static int totalObjects = 0;

    private void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return this.sign;
    }

    public TYPE getType() {
        return type;
    }

    void setType(TYPE type) {
        this.type = type;
    }

    private void insertObject(Object object) {
        objectList.add(object);
    }

    public void printObjects() {
        System.out.println("Objects list of node(" + this.sign + ")");
        for (Object object : objectList)
            object.print();
    }

    public ArrayList<Object> getObjectList() {
        return objectList;
    }

    public ArrayList<Object> getAllObjects() {
        ArrayList<Object> allObjectList = new ArrayList<>();
        if (this.getType().equals(TYPE.BLACK_LEAF)) {
            allObjectList.addAll(getObjectList());
        } else {
            if (!this.SW.getType().equals(TYPE.WHITE_LEAF))
                allObjectList.addAll(this.SW.getAllObjects());
            if (!this.SE.getType().equals(TYPE.WHITE_LEAF))
                allObjectList.addAll(this.SE.getAllObjects());
            if (!this.NW.getType().equals(TYPE.WHITE_LEAF))
                allObjectList.addAll(this.NW.getAllObjects());
            if (!this.NE.getType().equals(TYPE.WHITE_LEAF))
                allObjectList.addAll(this.NE.getAllObjects());
        }
        return allObjectList;
    }

    private static Quadtree node = new Quadtree();

    /**
     * Returns reference to Quadtree partition
     *
     * @param sign Signature of the Partition
     * @return instance of Quadtree
     */
    private Quadtree requestNode(String sign) {
        if (sign.equals(""))
            node = this;
        else
            switch (sign.substring(0, 2)) {
                case "00":
                    this.SW.requestNode(sign.substring(2));
                    break;
                case "01":
                    this.SE.requestNode(sign.substring(2));
                    break;
                case "10":
                    this.NW.requestNode(sign.substring(2));
                    break;
                case "11":
                    this.NE.requestNode(sign.substring(2));
                    break;
            }
        return node;
    }

    /**
     * Places the spatio object under a corresponding partition of Quadtree
     *
     * @param object Spatio object consisting of [id,latitude,longitude,mortoncode,name] (instance of util.Object class)
     * @param sign   Sign of the Quadtree partition where the object need to be placed [binary string] (instance of String)
     */
    private void insertObject(Object object, String sign) {
        //System.out.println("entered insert--->" +sign);
        if (sign.equals("")) {
            this.insertObject(object);
            this.type = TYPE.BLACK_LEAF;
            //object.print();
        } else {
            switch (sign.substring(0, 2)) {
                case "00":
                    if (!(this.SW == null))
                        this.SW.insertObject(object, sign.substring(2));
                    else {
                        this.SW = new Quadtree();
                        this.SW.setSign(this.getSign() + "00");
                        this.SW.insertObject(object, sign.substring(2));
                    }
                    this.setType(TYPE.NON_LEAF);
                    break;
                case "01":
                    if (!(this.SE == null))
                        this.SE.insertObject(object, sign.substring(2));
                    else {
                        this.SE = new Quadtree();
                        this.SE.setSign(this.getSign() + "01");
                        this.SE.insertObject(object, sign.substring(2));
                    }
                    this.setType(TYPE.NON_LEAF);
                    break;
                case "10":
                    if (!(this.NW == null))
                        this.NW.insertObject(object, sign.substring(2));
                    else {
                        this.NW = new Quadtree();
                        this.NW.setSign(this.getSign() + "10");
                        this.NW.insertObject(object, sign.substring(2));
                    }
                    this.setType(TYPE.NON_LEAF);
                    break;
                case "11":
                    if (this.NE != null)
                        this.NE.insertObject(object, sign.substring(2));
                    else {
                        this.NE = new Quadtree();
                        this.NE.setSign(this.getSign() + "11");
                        this.NE.insertObject(object, sign.substring(2));
                    }
                    this.setType(TYPE.NON_LEAF);
            }
        }
        /*
        if (this.SW == null)
            this.SW = new Quadtree();
        if (this.SE == null)
            this.SE = new Quadtree();
        if (this.NW == null)
            this.NW = new Quadtree();
        if (this.NE == null)
            this.NE = new Quadtree();*/
    }

    /*public void insertObject(Quadtree node, String mCode) {
        if (mCode.length()==2) {
            switch (mCode.substring(0, 2)) {
                case "00":
                    this.SW=node;
                    break;
                case "01":
                    this.SE=node;
                    break;
                case "10":
                    this.NW=node;
                    break;
                case "11":
                    this.NE=node;
            }
        } else
            switch (mCode.substring(0, 2)) {
                case "00":
                    if (!(this.SW == null))
                        this.SW.insertObject(node, mCode.substring(2));
                    else {
                        this.SW = new Quadtree();
                        this.SW.setSign(this.getSign() + "00");
                        this.SW.insertObject(node, mCode.substring(2));
                    }
                    break;
                case "01":
                    if (!(this.SE == null))
                        this.SE.insertObject(node, mCode.substring(2));
                    else {
                        this.SE = new Quadtree();
                        this.SE.setSign(this.getSign() + "01");
                        this.SE.insertObject(node, mCode.substring(2));
                    }
                    break;
                case "10":
                    if (!(this.NW == null))
                        this.NW.insertObject(node, mCode.substring(2));
                    else {
                        this.NW = new Quadtree();
                        this.NW.setSign(this.getSign() + "10");
                        this.NW.insertObject(node, mCode.substring(2));
                    }
                    break;
                case "11":
                    if (this.NE != null)
                        this.NE.insertObject(node, mCode.substring(2));
                    else {
                        this.NE = new Quadtree();
                        this.NE.setSign(this.getSign() + "11");
                        this.NE.insertObject(node, mCode.substring(2));
                    }
            }
    }*/

    /**
     * Checks the Quadtree structure by traversing every partitions under Quadtree
     *
     */
    private void traverse(){
        //System.out.println("entering into---> " + this.getSign().length() / 2);
        ///to print node types
        TYPE sw=this.SW.getType(); TYPE se=this.SE.getType(); TYPE nw=this.NW.getType(); TYPE ne=this.NE.getType();
        System.out.println(sw + "\t\t\t\t\t\t" + se + "\t\t\t\t\t\t" + nw + "\t\t\t\t\t\t" + ne + "\t" );
        if (this.getType().equals(TYPE.BLACK_LEAF)) {
            totalObjects++;
            //System.out.println("BLACK_LEAF" + this.getSign());
        }
        else {
            if (!this.SW.getType().equals(TYPE.WHITE_LEAF))
                this.SW.traverse();
            if (!this.SE.getType().equals(TYPE.WHITE_LEAF))
                this.SE.traverse();
            if (!this.NW.getType().equals(TYPE.WHITE_LEAF))
                this.NW.traverse();
            if (!this.NE.getType().equals(TYPE.WHITE_LEAF))
                this.NE.traverse();
            //System.out.println("back from<--- " + this.getSign().length() / 2);
        }
    }

    /**
     * Corrects type of nodes under quadtree
     */
    private void setTypes() {
        if (this.SW != null || this.SE != null || this.NW != null || this.NE != null) {
            if (!this.getType().equals(TYPE.BLACK_LEAF))
                this.setType(TYPE.NON_LEAF);
        }
        if (this.SW != null)
            this.SW.setTypes();
        else
            this.SW = new Quadtree();
        if (this.SE != null)
            this.SE.setTypes();
        else
            this.SE = new Quadtree();
        if (this.NW != null)
            this.NW.setTypes();
        else
            this.NW = new Quadtree();
        if (this.NE != null)
            this.NE.setTypes();
        else
            this.NE = new Quadtree();
    }

    public static Quadtree constuctQuadtree(String term) throws Exception {
        Statement statement = DBUtil.getUtil().getConnection().createStatement();
        ResultSet rs = statement.executeQuery("select latitude,longitude,mCode,name,type from euro where name LIKE '%" + term + "%';");// where name='"+term+"'
        System.out.println("\n<<<<<<<<Constructing ILQuadtree for " + term + ">>>>>>>>>");
        long start= System.nanoTime();
        Quadtree quadtree = new Quadtree();
        int count = 0;
        while (rs.next()) {
            count++;
            Object object = new Object(rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("mCode"), rs.getString("name"), rs.getString("type"));
            //System.out.println(object.getName());
            quadtree.insertObject(object, rs.getString("mCode"));
            //quadtree.insert(object, rs.getString("mCode"));
        }
        System.out.println(count + " objects inserted to ILQuadtree");
        System.out.println("time taken : "+(double)(System.nanoTime()-start)/1000000000);
        quadtree.setType(TYPE.NON_LEAF);
        quadtree.setTypes();
        rs.close();
        statement.close();
        return quadtree;
    }

    /*public void insert(Object object,String sign) {
        Quadtree currentNode = this;
        String currentSign = sign;
        while (!currentSign.equals("")) {
            //System.out.println(currentSign);
            //4.413226846
            switch (currentSign.substring(0, 2)) {
                case "00":
                    currentNode.setType(TYPE.NON_LEAF);
                    if (!(currentNode.SW == null)) {
                        currentNode = currentNode.SW;
                        currentSign = currentSign.substring(2);
                    } else {
                        currentNode.SW = new Quadtree();
                        currentNode.SW.setSign(this.getSign() + "00");
                        currentNode = currentNode.SW;
                        currentSign = currentSign.substring(2);
                    }
                    break;
                case "01":
                    currentNode.setType(TYPE.NON_LEAF);
                    if (!(currentNode.SE == null)) {
                        currentNode = currentNode.SE;
                        currentSign = currentSign.substring(2);
                    } else {
                        currentNode.SE = new Quadtree();
                        currentNode.SE.setSign(this.getSign() + "01");
                        currentNode = currentNode.SE;
                        currentSign = currentSign.substring(2);
                    }
                    break;
                case "10":
                    currentNode.setType(TYPE.NON_LEAF);
                    if (!(currentNode.NW == null)) {
                        currentNode = currentNode.NW;
                        currentSign = currentSign.substring(2);
                    } else {
                        currentNode.NW = new Quadtree();
                        currentNode.NW.setSign(this.getSign() + "10");
                        currentNode = currentNode.NW;
                        currentSign = currentSign.substring(2);
                    }
                    break;
                case "11":
                    currentNode.setType(TYPE.NON_LEAF);
                    if (!(currentNode.NE == null)) {
                        currentNode = currentNode.NE;
                        currentSign = currentSign.substring(2);
                    } else {
                        currentNode.NE = new Quadtree();
                        currentNode.NE.setSign(this.getSign() + "11");
                        currentNode = currentNode.NE;
                        currentSign = currentSign.substring(2);
                    }
            }
        }
        currentNode.insertObject(object);
        currentNode.setType(TYPE.BLACK_LEAF);
    }*/

    public static Quadtree constructQuadtreeFromCluster(String term, Input input) {
        System.out.println("\n<<<<<<<<Searching for " + term + ">>>>>>>>>");
        Quadtree quadtree = new Quadtree();
        int count = 0;
        for (int i = 0; i < input.clusters.length; i++)
            for (List<Object> objects : input.clusters[i].values()) {
                for (Object object : objects)
                    if (object.getName().matches("(?i:.*" + term + ".*)")) {
                        count++;
                        quadtree.insertObject(object, object.getMCode());
                    }
            }
        System.out.println(count + " objects found!");
        quadtree.setType(TYPE.NON_LEAF);
        quadtree.setTypes();
        return quadtree;
    }
}