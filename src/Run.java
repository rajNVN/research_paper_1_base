import btopskQuery.BtopskQuery;
import grouping.Group;
import grouping.GroupAlgo;
import org.apache.lucene.spatial.util.MortonEncoder;
import org.apache.lucene.util.RamUsageEstimator;
import topskQuery.TopskQuery;
import util.DataBase.DBUtil;
import util.DataParser;
import util.Input;
import util.preprocessing.Object;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


public class Run {
    public static void main(String nvn[]) throws Exception {
        /*PrintWriter printWriter=new PrintWriter("setMorton-2.txt");
        Connection connection = DBUtil.getUtil().getConnection();
        Statement stat = connection.createStatement();
        Set<String> queries = new HashSet<>();
        ResultSet rs= stat.executeQuery("select * from queries;");
        while (rs.next()) {
            if(rs.getString("mCode")==null) {
                double longitude = rs.getDouble("longitude");
                double latitude = rs.getDouble("latitude");
                long morton = MortonEncoder.encode(latitude, longitude);
                String mCode = Long.toBinaryString(morton);
                queries.add("update queries set mCode='" + mCode + "' where longitude=" + longitude + " and latitude=" + latitude + ";");
                //System.out.println("update queries set mCode='"+mCode+"' where longitude="+longitude+" and latitude="+latitude+";");
                //stat.executeUpdate("update queries set mCode='"+mCode+"' where longitude="+longitude+" and latitude="+latitude+";");
            }
        }
        long starrt = System.nanoTime();
        int i=0;
        for (String sql:queries) {
            System.out.println(sql + "\n" + (double) (System.nanoTime() - starrt) / 1000000000+" | "+i+" - "+queries.size());
            printWriter.write(sql + " - " + (double) (System.nanoTime() - starrt) / 1000000000 + "\n");
            stat.executeUpdate(sql);
            i++;
        }
        printWriter.close();
        System.exit(0);*/
        //System.setOut(new PrintStream("1euro20 .txt"));
        //DataParser.getTerms(3,20,"euro");
        //System.exit(0);
      //  DataParser.generaterRawTersms(5,"euro");
        //System.exit(0);
        /*for(int terms=1;terms<=3;terms++)
            for(int k=1;k<=4;k++)
                for(int q=1;q<=3;q++) {
                    System.out.println(terms+"_"+q*20+"_"+k*5);
                    if(terms==3&&q*20==60&&k*5==10)
                        DataParser.generateInset(terms, q * 20, k * 5, "euro");
                }
        System.exit(0);*/
        for (File in : new File("G:" + System.getProperty("file.separator") + "euro").listFiles()) {
            if(in.getName().contains("quadtree"))
                continue;
            System.out.println(in.getName());
            HashMap<Integer, List<Object>> map1 = new HashMap<>();
            Input input = new Input();
            //  FileOutputStream fos = new FileOutputStream("in&out/OutputEuro2(1).txt");
            // System.setOut(new PrintStream(fos));
            System.out.println("TOPSK QUERY PROCESSING");
            //input.getInput("/run/media/rajnvn/SHARED/reaserch_workspace/in&out/InputEuro2.txt");
            input.getInput(in);
            long start = System.nanoTime();

            for (int i = 0; i < input.no_of_users; i++) {
                System.out.println("\n<===============First Algorithm Call(Query " + (i + 1) + ")===================================>");
                map1.put(i, TopskQuery.topSk(i, input));
            }
            System.out.println("\nTime taken for all first algorithm calls : " + (double) (System.nanoTime() - start) / 1000000000);

//        fos = new FileOutputStream("in&out/OutputEuro2(3).txt");
            //      System.setOut(new PrintStream(fos));
            System.out.println("\n<=====================================Third Algorithm =======================================>");
            long start3 = System.nanoTime();
            input.clusMap = Group.getData(input, map1);
            input.clusters = GroupAlgo.getClustersObject();
            System.out.println("\n<^><^><^><^><^><^><^><^> TOTAL TIME FOR Algorithm 3 : " + (double) (System.nanoTime() - start3) / 1000000000 + " <^><^><^><^><^><^><^><^>");

            //   fos = new FileOutputStream("in&out/OutputEuro2(4).txt");
            // System.setOut(new PrintStream(fos));

            System.out.println("\n<=====================================Fourth Algorithm =======================================>");
            Map<String, HashMap<Double, Object>> result = BtopskQuery.btopSk(input);

            //FileOutputStream fos = new FileOutputStream("/run/media/rajnvn/SHARED/reaserch_workspace/New Folder/OutputEuro2Results.txt");
            PrintWriter writer = new PrintWriter("G:" + System.getProperty("file.separator") + "euro" + System.getProperty("file.separator") + "base("+in.getName().split("\\.")[0]+").txt");
            writer.write("\n<=====================================================Final Results==========================================================================>:\nDistance -----> latitude,longitude,Mcode,Name,Type");
            for (String subQuery : input.Query) {
                writer.write("\n<-------------- For Query : " + subQuery + "----------------->");
                int count = 0;
                TreeMap<Double, Object> tree = new TreeMap<>(result.get(subQuery));
                int top_k = Integer.parseInt(in.getName().split("_")[2].split("\\.")[0]);
                for (Double dist : tree.keySet()) {
                    Object object = tree.get(dist);
                                                   /* boolean flag=true;
                                                    int index=0;
                                                    for(String term:subQuery.split(" ")){
                                                        if(index>object.getName().indexOf(term)) {
                                                            flag = false;
                                                            break;
                                                        }
                                                        else
                                                            index=object.getName().indexOf(term);
                                                    }

                                                    */
                                                    /*
                                                    String sp[]=subQuery.split(" ");
                                                    int refindex=subQuery.indexOf(sp[0]);
                                                    for(int y=1;y<sp.length;y++)
                                                    {
                                                        if(subQuery.indexOf(sp[y])<refindex)
                                                        {
                                                            flag=false;
                                                            break;
                                                        }
                                                    }*/
                    if (count < top_k) {
                        writer.write(dist + " --> ");
                        writer.write(object.getLatitude()+ " , " + object.getLongitude() + " , " + object.getName());
                        count++;
                    }
                }
            }
            long end = System.nanoTime();
            double res = (double) (end - start) / 1000000000;
            writer.write("\n\n\n<^><^><^><^><^><^><^><^> TOTAL TIME for BASE PAPER : " + res + " <^><^><^><^><^><^><^><^>");
            writer.close();
        }
    }
}
