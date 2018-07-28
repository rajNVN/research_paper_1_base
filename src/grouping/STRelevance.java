package grouping;
import java.util.*;
public class STRelevance {

    /*
    *07/01/2018
    * Darshan
    * class meant for calculating spatial and textual relevance
     */

     public static  Map<String,Integer> countobj[];         // total number of individual objects present in cluster
     public static Map<String,Integer> numbobj=new HashMap<String,Integer>();
     public static int tobj[];                  //total number of objects in cluster
     public static int total=0;
     public static BoundingRect rect[];
     public static int k1=0;
     public static double  maxdistance[];

     public static void intialize(String querylog[],int k)
     {
         /*
            *07/01/2018
            * Darshan
            * initializing term frequency and number of terms in querylist
          */
         int i;
         k1=k;
         for(i=0;i<querylog.length;i++)
         {
             String temp[]=querylog[i].split(" ");
             total=total+temp.length;
             int j;
             for(j=0;j<temp.length;j++)
             {
                 if(!numbobj.containsKey(temp[j]))
                 {
                     numbobj.put(temp[j],1);
                 }
                 else{
                     int x=numbobj.get(temp[j]);
                     x++;
                     numbobj.put(temp[j],x);
                 }
             }
         }
         tobj=new int[k];
         maxdistance=new double[k];
         countobj=new HashMap[k];
         rect=new BoundingRect[k];
         for(i=0;i<k;i++)
         {
             countobj[i]=new HashMap<String,Integer>();
             countobj[i].put(querylog[i],1);
             tobj[i]=1;
             maxdistance[i]=1;
         }
         //System.out.println(numbobj);
         //System.out.println(total);
     }

    public static void intializeCluster(Map<String,String> centre[],int k,String q[])
    {
        int i;
        for(i=0;i<k;i++)
        {
            String get=centre[i].get(q[i]);
            String sp[]=get.split(",");
            double x1=Double.parseDouble(sp[0]);
            double y1=Double.parseDouble(sp[1]);
            rect[i]=new BoundingRect(x1,y1);
        }


    }




     public static double calculateTextRel(String query,int j,double ap)
     {

         /*
           *07/01/2018
           * Darshan
           * Calculating textual relevance
          */
         String temp[]=query.split(" ");
         int i;
         double weight[]=new double[temp.length];
         for(i=0;i<temp.length;i++)
         {
             int ft1;
             if(countobj[j].containsKey(temp[i]))
                 ft1=countobj[j].get(temp[i]);
             else ft1=0;
             double part1=(1-ap)*(double)ft1/tobj[j];
             double part2=(ap)*(double)numbobj.get(temp[i])/total;
           //  System.out.println("Part 1:"+part1+"    Part 2:"+part2);
             weight[i]=part1+part2;
         }
          //for(double z:weight)
            //  System.out.println("Weight :"+z);

         double d=calculateTextNext(weight);
         return d;
     }

     public static double calculateTextNext(double w[])
     {
         /*
            *08/01/2018
            * Darshan
            * Calculating textual relevance part-2
          */
         double maxp=calculateMaxP(w);
         double total=1;
         int i=0;
         for(i=0;i<w.length;i++)
         {
            total=total*(w[i]/maxp);
         }
         double res=1-total;
         return res;
     }

     public static double calculateMaxP(double w[])
     {
         /*
            *08/01/2018
            * Darshan
            * Calculating maxP for textual relevance

          */
         int i;
         double maxp=0,temp=0;
         for(i=0;i<w.length;i++)
         {
             temp=temp+w[i];
         }
         for(i=0;i<w.length;i++)
         {
             double temp2=w[i]/temp;
             if(temp2>maxp)
                 maxp=temp2;
         }
         return maxp;
     }



    public static double calculateSpatialRel(String centroid,int k)
    {
        double res=0;
        double x1=rect[k].getX1();
        double y1=rect[k].getY1();

        String sp[]=centroid.split(",");
        double x2=Double.parseDouble(sp[0]);
        double y2=Double.parseDouble(sp[1]);
        res=calculateDistance(x1,y1,x2,y2);
        res=res/maxdistance[k];
        if(res<0)
            res=-1*res;
        return res;
    }

    public static double calculateDistance(double x1,double y1,double x2,double y2)
    {

        double t1=(x2-x1)*(x2-x1);
        double t2=(y2-y1)*(y2-y1);

       double res=Math.sqrt(t1+t2);
       return res;
    }

    public static double calculateScore(double a,double b,double apha)
    {
        double res=0;
        res=(apha*b)+((1-apha)*a);
        return res;
    }

     public static void print()
     {
         System.out.println("Before Updating..");
         for(int i=0;i<k1;i++)
         {
             //System.out.println("Total Objects in Cluster :"+(i+1)+" :"+tobj[i]);
             //ystem.out.println("Count Of Objects in Cluster :"+(i+1)+" : "+countobj[i]);
         }
     }

    public static boolean updateCluster(int ch,String query,String ll)
    {

        String sp[]=ll.split(",");
        double x2=Double.parseDouble(sp[0]);
        double y2=Double.parseDouble(sp[1]);
        rect[ch].intialize(x2,y2);
        double x1=rect[ch].getX1();
        double y1=rect[ch].getY1();

        double res=calculateDistance(x1,y1,x2,y2);

        if(res>maxdistance[ch])
        {
            maxdistance[ch]=res;
        }

        tobj[ch]=tobj[ch]+1;
        if(countobj[ch].containsKey(query))
        {
            int x=countobj[ch].get(query);
            x++;
            countobj[ch].put(query,x);
        }
        else countobj[ch].put(query,1);

        return true;
    }
}
