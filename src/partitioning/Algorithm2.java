package partitioning;

import java.util.*;
import java.sql.*;

import static partitioning.Partion.*;

public class Algorithm2 {


    int start=0;
    double finalmin=999999999;
    HashMap<Double,List<List<String>>> hp=new HashMap<Double,List<List<String>>>();
    List<List<String>> finalList=new ArrayList<List<String>>();

    public void Partition(String []querylog,List<String> obj,int thr,int D,int nt) throws SQLException
    {
        Algorithm2 al=new Algorithm2();
        List<String>  hfset = new ArrayList<String>();
        List<String>  hpset=new ArrayList<String>();
        List<String>  fpset=new ArrayList<String>();
        List<String>  fplist=new ArrayList<String>();
        List<String>  querylist=new ArrayList<String>();
        List<String>  Q=new ArrayList<String>();

        hfset=getMaximumObj(obj,querylog,thr);
        for(String H:hfset)
            System.out.println(H);
        fpset=pairTheTerms(hfset);
        List<String> qlog=getListHere(querylog);
        System.out.println("Hello");
        hpset=pairObjTerms(obj,qlog);
        hpset=pairObjAgain(hpset);

        System.out.println("size:"+hpset.size());
        fplist=removeTrueHits(hpset,fpset);

        List<String> hfset2=new ArrayList<String>();
        hfset2=getReversePair();
        fplist=AgainRemove(fplist,fpset);
        fplist=AgainRemove(fplist,hfset2);


        System.out.println("Len 1:"+hpset.size()+"   Len 2:"+fplist.size());
        Q=getQlist();
        for(String H:Q)
            System.out.println(H);
        int Qsize=Q.size();
        removeQlist();
        List<String> Clist1=new ArrayList<String>();
        List<String> Clist2=new ArrayList<String>();
        double min=999999999;
        String psel="";
        int i;
        double  pro=(double)Qsize/querylog.length;
        System.out.println("Q="+Qsize+" Demo="+querylog.length+" Pro:"+pro);

        //for(String f:fplist)
        //  System.out.println(f);


        for(i=0;i<fplist.size();i++)
        {
            double sum=0;
            List<String> tClist1=new ArrayList<String>();
            List<String> tClist2=new ArrayList<String>();
            String temp[]=fplist.get(i).split("-");
            tClist1=getFalseCount(obj,temp[0]);
            tClist2=getFalseCount(obj,temp[1]);
            sum=(tClist1.size()*pro)+(tClist2.size()*pro);
            if(sum<min)
            {
                psel=fplist.get(i);
                Clist1=tClist1;
                Clist2=tClist2;
                min=sum;
            }


        }
        System.out.println("For Pair :"+psel);
        System.out.println("Pro :"+pro+"      Min :"+min);
        for(String H:Clist1)
            System.out.println(H);
        System.out.println("Cluster Break....");
        for(String H:Clist2)
            System.out.println(H);
        if(Clist1.size()==0||Clist2.size()==0)
            System.out.println("End");
        if(min<finalmin)
            finalmin=min;


        if(fplist.remove(psel))
        {
            System.out.println("For a Pair :"+psel);
            D=D+1;
            if(nt>(int)Math.pow(2,D)&&Clist1.size()>0&&Clist2.size()>0&&psel.length()>0)
            {

                hp=getFinal(hp,Clist1,min);
                finalList.add(Clist1);
                System.out.println("Displaying Cluster...");
                for(String H:Clist1)
                {
                    System.out.println(H);
                }
                System.out.println(min);
                al.Partition(fplist.toArray(new String[fplist.size()]),Clist1,thr,D,(int)Math.pow(2,Clist1.size()));
                finalList.add(Clist2);
                hp=getFinal(hp,Clist2,min);
                al.Partition(fplist.toArray(new String[fplist.size()]),Clist2,thr,D,(int)Math.pow(2,Clist2.size()));
            }

        }
        else{

            String x=hp.toString();
            System.out.println(x);
        }
        for(List<String> H1:finalList)
        {
            for (String sr:H1)
            {
                System.out.println(sr);
            }
        }
        System.out.println("Minimum false Hit:"+finalmin);

    }

}
