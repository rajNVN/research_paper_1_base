package util;

import util.preprocessing.Quadtree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.Object;

public class Converter {
    public static long binaryStringToLong(String binaryString)
    {
        long longNumber = 0L;
        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == '1') {
                longNumber ^= 1L << (63 - i);
            }
        }
        return longNumber;
    }

    public static byte[] getByteFromQuadtree(Quadtree quadtree){

        byte[] byteArrayObject = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(quadtree);
            oos.close();
            bos.close();
            byteArrayObject = bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return byteArrayObject;
        }
        return byteArrayObject;
    }

    public static Quadtree getQuadtreeObject(byte[] convertObject){
        Quadtree objSimpleExample = null;

        ByteArrayInputStream bais;
        ObjectInputStream ins;
        try {

            bais = new ByteArrayInputStream(convertObject);

            ins = new ObjectInputStream(bais);
            objSimpleExample =(Quadtree)ins.readObject();

            ins.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return objSimpleExample;
    }
}
