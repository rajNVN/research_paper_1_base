package grouping;
import util.preprocessing.Object;

import java.sql.SQLException;
import java.util.*;
import static grouping.Group.*;
import static grouping.STRelevance.*;

public class GroupAlgo {

    private static HashMap<String, List<Object>> GroupList5[];

    public Map<String,List<String>>[] Grouping(String []querylog ,int k) throws SQLException
    {
        Map<String,List<String>> query_m=new HashMap<String,List<String>>();
        query_m=getMortonCode(querylog);
    //    System.out.println(query_m);
        HashMap<String,List<Object>> query_o=objLis;
        Map<String,String> centroid[]=new HashMap[k];
        Map<String,List<String>> GroupList[]=new HashMap[k];
        GroupList5=new HashMap[k];
        int i;
        for(i=0;i<k;i++)
        {
            GroupList[i]=new HashMap<String,List<String>>();
            centroid[i]=new HashMap<String,String>();
            GroupList5[i]=new HashMap<String, List<Object>>();
        }

        /*
         *07/01/2018
         * Darshan
         * Assigning Centroid for k groups
         */
        List<String> sq=new ArrayList<String>();
        for(String h:query_m.keySet())
        {
            sq.add(h);
        }
        String qmain[]=sq.toArray(new String[sq.size()]);
        for(i=0;i<k;i++)
        {
            List<String> qlist=query_m.get(qmain[i]);
            List<String> al=new ArrayList<>();
            al.add(qlist.get(0));
            GroupList[i].put(qmain[i],al);
            centroid[i].put(qmain[i],qlist.get(0));
          //  System.out.println(centroid[i]);
            List<Object> olist=query_o.get(qmain[i]);
            List<Object> ol=new ArrayList<>();
            ol.add(olist.get(0));
            GroupList5[i].put(qmain[i],ol);
        }


        /*
         *07/01/2018
         * Darshan
         * Main Algorithm start...
         */
      //   for(String h:querylog)
        //     System.out.println("From query log :"+h);

        for(String h:query_m.keySet())
        {

        }
        int cou=0;
        intialize(qmain,k);
        intializeCluster(centroid,k,qmain);
         for(String sd:query_m.keySet())
         {
            // System.out.println("For QQQQ:"+sd);
             List<String> lsel=query_m.get(sd);
             List<Object> osel=query_o.get(sd);
             int e;
             for(e=0;e<lsel.size();e++) {
              //   System.out.println("For a String :"+lsel.get(e));
                 String sel=lsel.get(e);
                 Object oel=osel.get(e);
                 double score = 999999999;
                 String as="";
                 String ss="";
                 Object os=null;
                 int ch = 0,get=0;
                 int j;
                 for(j=0;j<k;j++)
                 {
                     int y=0,found=0;
                     while(y<k)
                     {
                         if(centroid[y].containsValue(sel))
                         {
                             System.out.println("Object found USBSGAND :"+sel);
                             found=1;
                             cou++;
                         }
                         y++;
                     }
                     if(found==1)
                         break;

                     else {
                         get=1;
                         as=sd;
                         ss=sel;
                         os=oel;
                         double textrel = calculateTextRel(sd, j, 0.3);
                         double sptrel=calculateSpatialRel(sel,j);
                         double qscore=calculateScore(textrel,sptrel,0.3);
                         if(qscore<score)
                         {
                             score=qscore;
                             ch=j;
                         }
                        // System.out.println("Textual relevance for query :" + querylog[i] + "----subquery :" + e + "------For cluster :" + (j + 1) + "====>" + textrel+" Spatial Rel : "+sptrel+" Selected : "+ch+" For LongLat:====>"+sel);

                     }
                 }
                        //print();
                 if(get==1) {
                     boolean tof = updateCluster(ch, sd, sel);
                     if (tof) {
                         if(GroupList[ch].containsKey(as))
                         {
                         //    System.out.println("Added");
                             List<String> h=GroupList[ch].get(as);
                             h.add(ss);
                             GroupList[ch].put(as,h);
                             List<Object> o=GroupList5[ch].get(as);
                             o.add(os);
                             GroupList5[ch].put(as,o);
                         }
                         else {
                              List<String> ne=new ArrayList<String>();
                              ne.add(ss);
                              List<Object> no=new ArrayList<Object>();
                              no.add(os);
                             GroupList[ch].put(as,ne);
                             GroupList5[ch].put(as,no);
                         }

                     }
                     System.out.println("Query :" +as+"  Cluster :"+ch);
                 }
             }

         }

            //System.out.println("============================>GROUP DETAILS<============================");
         for(i=0;i<k;i++)
         {
             int f=0;
             for(String s:query_m.keySet()) {

                 if(GroupList[i].containsKey(s))
                 {
                     if(f==0) {
                         //System.out.println("GROUP CLUSTER " + (i + 1));
                         //System.out.println("************************");
                     }
                     f=1;
                     List<String> w=GroupList[i].get(s);
                    /* for(int ke=0;ke<w.size();ke++)
                     {
                         System.out.println(s+"=============>"+w.get(ke));
                     }*/
                 }
             }
         }

         return GroupList;
    }

    public static HashMap<String,List<Object>>[] getClustersObject()
    {
        return  GroupList5;
    }

}
