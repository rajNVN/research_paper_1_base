package grouping;
import util.Input;
import util.preprocessing.Object;

import java.util.*;
import java.sql.*;
public class Group {

    private static Input inp;
    private  static HashMap<Integer,List<Object>> mainMap=new HashMap<Integer, List<Object>>();
    public static HashMap<String,List<Object>> objLis=new HashMap<String, List<Object>>();
    public static Map<String,List<String>>[] getData(Input in,HashMap<Integer,List<Object>> m1) throws SQLException
    {
         /*
           *06/1/18
            * Darshan
            * Taking input from the user and calling GroupAlgo method
            */
         inp=in;
         mainMap=m1;
        List<String> main=inp.Query;
        //System.out.println(main);
        int n,i,k=10;
        String querylog[]=main.toArray(new String[main.size()]);
        GroupAlgo ga=new GroupAlgo();
        Map<String,List<String>> ret[]=ga.Grouping(querylog,k);
        return ret;
    }

    public static Map<String,List<String>> getMortonCode(String []querylog) throws SQLException
    {
        /*
            *06/01/18
            * Darshan
            * Mapping Every Query to Corresponding Mortoncode
         */
        Map<String,List<String>> listm=new HashMap<String,List<String>>();
        int i,j;

        for(i=0;i<inp.no_of_users;i++)
        {
            List<Object> l=mainMap.get(i);
            for(j=0;j<l.size();j++)
            {
                Object u=l.get(j);
                String n=u.getName();
                String lonlat=Double.toString(u.getLatitude())+",";
                lonlat=lonlat+Double.toString(u.getLongitude());

                if(listm.containsKey(n))
                {
                    List<Object> m4=objLis.get(n);
                    m4.add(u);
                    objLis.put(n,m4);
                    List<String> m=listm.get(n);
                    m.add(lonlat);
                    listm.put(n,m);
                }
                else{
                    List<String> lm=new ArrayList<String>();
                    List<Object> lo=new ArrayList<Object>();
                    lo.add(u);
                    objLis.put(n,lo);
                    lm.add(lonlat);
                    listm.put(n,lm);
                }
            }
        }
        return listm;
    }
}
