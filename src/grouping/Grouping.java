package grouping;

import java.util.*;
import java.sql.*;

import static grouping.MainAlgo3.*;

public class Grouping {

  public void mainGrouping(String querylog[],int n) throws SQLException
  {
    HashMap<String,Integer> Qt=new HashMap<String,Integer>();
    Qt=calculateQt(querylog);
    System.out.println(Qt);
    double sum1=0,sum2=0;
    List<Double> l1=new ArrayList<Double>();
    int i;
    for(i=0;i<querylog.length;i++)
    {
        l1.add(Qtfrequency(querylog[i],Qt));
    }
    System.out.println(l1);
    HashMap<String,Integer> D=new HashMap<String,Integer>();
    D=calculateD(querylog);
    int Dint=0;
    List<String > sl=getUniqueString(querylog);
    for(i=0;i<sl.size();i++)
    {
        Dint=Dint+D.get(sl.get(i));
    }
    List<Double> l2=new ArrayList<Double>();
    for(i=0;i<querylog.length;i++)
    {
        l2.add(Dfrequency(querylog[i],D,Dint));
    }
    List<Double >textual=new ArrayList<Double>();
    for(i=0;i<l1.size();i++)
        textual.add(l1.get(i)+l2.get(i));

    System.out.println(textual);

  }
    
}
