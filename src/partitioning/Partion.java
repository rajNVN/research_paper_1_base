package partitioning;

import java.sql.*;
import java.util.*;

import util.DataBase.DBConstants;

public class Partion {


    static List<String> querylist=new ArrayList<String>();
    static List<String> hlist2=new ArrayList<>();
    List<String> queryloglist=new ArrayList<String>();

    Scanner manu=new Scanner(System.in);
    int numq;
    int ther;
    String querylog[];
    public void getData() throws SQLException
    {
        System.out.println("Enter number of queries?");
        numq=manu.nextInt();
        querylog=new String[numq];
        int i;
        String dum=manu.nextLine();
        for(i=0;i<numq;i++)
        {
            System.out.println("Enter the query "+i);
            querylog[i]=manu.nextLine();
            String arr[]=querylog[i].split(" ");
            int q=0;
            while(q<arr.length)
            {
                queryloglist.add(arr[q]);
                q++;
            }
        }
        System.out.println("Enter threshold frequency");
        ther=manu.nextInt();
        List<String> lis=new ArrayList<String>();
        lis=SearchAllTerms();
        Algorithm2 algo=new Algorithm2();
        algo.Partition(querylog,lis,ther,0,999999999);
    }




    public List<String> SearchAllTerms() throws SQLException
    {
        Statement con=DriverManager.getConnection(DBConstants.DB_DRIVER_TYPE.getConstValue()+":"+DBConstants.DB_LOCATION.getConstValue()).createStatement();
        List<String> Obj=new ArrayList<String>();
        int i=0;
        ResultSet rs=con.executeQuery("SELECT name FROM mortoncode");
        while(rs.next())
        {
            Obj.add(rs.getString("name"));
        }
        con.close();
        return Obj;

    }


    public static List<String> getListHere(String []arr)
    {
        List<String> ret=new ArrayList<String>();
        int i,j;
        for(i=0;i<arr.length;i++)
        {
            String az[]=arr[i].split("-");
            int q=0;
            while(q<az.length)
            {
                ret.add(az[q]);
                q++;
            }
        }
        return ret;
    }


    public static List<String> getMaximumObj(List<String> O,String []ql,int t)
    {
        List<String> hfreq=new ArrayList<String>();
        int i,j;
        for(i=0;i<ql.length;i++)
        {
            String arr[]=ql[i].split(" ");
            int there=0;
            for(j=0;j<arr.length;j++)
            {
                int count=0;
                int size=O.size();
                int k=0;
                while(k<size)
                {
                    if(O.get(k).equals(arr[j]))
                        count++;
                    k++;
                }
                if(count>=t) {
                    hfreq.add(arr[j]);
                    there++;
                }

            }

            if(there==arr.length)
                querylist.add(ql[i]);
        }

        return hfreq;
    }




    public static List<String> pairTheTerms(List<String> temp)
    {
        List<String> hpset=new ArrayList<String>();
        for(int i=0;i<temp.size()-1;i++)
        {
            for(int j=i+1;j<temp.size();j++)
            {
                String tem=temp.get(i)+"-"+temp.get(j);
                String tem2=temp.get(j)+"-"+temp.get(i);
                System.out.println("Main :"+tem);
                System.out.println("Main Reverse:"+tem2);
                if(!hpset.contains(tem))
                {
                    hpset.add(tem);
                    hlist2.add(tem2);
                }
            }
        }

        return hpset;
    }

    public static List<String> pairObjAgain (List<String> temp)
    {
        List<String> hpset=new ArrayList<String>();
        for(int i=0;i<temp.size()-1;i++)
        {
            for(int j=i+1;j<temp.size();j++)
            {
                String tem=temp.get(i)+"-"+temp.get(j);
                String tem2=temp.get(j)+"-"+temp.get(i);
                if(!hpset.contains(tem))
                {
                    hpset.add(tem);
                }
            }
        }

        return hpset;
    }

    public static List<String> getReversePair()
    {
        return hlist2;
    }

    public static List<String> pairObjTerms(List<String> O,List<String> query) throws SQLException
    {
        Statement con=DriverManager.getConnection(DBConstants.DB_DRIVER_TYPE.getConstValue()+":"+DBConstants.DB_LOCATION.getConstValue()).createStatement();
        List<String> ret=new ArrayList<String>();
        int i,j;
        for(i=0;i<query.size();i++)
        {

            String src[]=query.get(i).split(" ");
            for(int k=0;k<src.length;k++) {
                ResultSet rs = con.executeQuery("SELECT name FROM mortoncode WHERE name LIKE '%" + src[k] + "%'");
                while(rs.next())
                {
                    if(!ret.contains(rs.getString("name")))
                    {
                        ret.add(rs.getString("name"));
                    }
                }
            }
        }
        return ret;
    }



    public static List<String> getQlist()
    {
        return querylist;
    }

    public static void removeQlist()
    {
        querylist.clear();
    }

    public static List<String> removeTrueHits(List<String> l1,List<String> l2)
    {
        int i,j;
        List<String> ret=new ArrayList<String>();
        for(i=0;i<l2.size();i++) {
            String src=l2.get(i);
            String src2[]=l2.get(i).split("-");
            String temp=src2[1]+"-"+src2[0];
            //System.out.println(temp);
            for(j=0;j<l1.size();j++)
            {
                if((!l1.get(j).equals(temp))&&(!l1.get(j).equals(src))&&(!ret.contains(l1.get(j))))
                {
                    ret.add(l1.get(j));
                }
            }
        }

        return ret;
    }


    public static List<String> AgainRemove(List<String> fp,List<String> fl)
    {
        int i=0,j;
        for(i=0;i<fl.size();i++)
        {
            if(fp.contains(fl.get(i)))
            {
                fp.remove(fl.get(i));
            }
        }

        return fp;
    }

    public static HashMap<Double,List<List<String>>> getFinal(HashMap<Double,List<List<String>>> hp,List<String> cl,double min)
    {
        if(hp.containsKey(min))
        {
            List<List<String>> inter=hp.get(min);
            inter.add(cl);
            hp.put(min,inter);
        }
        else {
            List<List<String>> newo=new ArrayList<List<String>>();
            newo.add(cl);
            hp.put(min,newo);
        }

        return hp;
    }


    public static List<String> getFalseCount(List<String> O,String src)
    {
        int i;
        // System.out.println("For Source"+src);
        List<String> ret=new ArrayList<String>();
        for(i=0;i<O.size();i++)
        {
            String src1=O.get(i).replaceAll("\\s","");
            String src2=src.replaceAll("\\s","");
            //  System.out.println("Src 1:"+src1+"   Src 2 :"+src2);
            if(src1.contains(src2))
            {

                ret.add(O.get(i));
            }
        }
        return ret;
    }

}
