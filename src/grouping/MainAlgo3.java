package grouping;

import util.DataBase.DBConstants;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class MainAlgo3 {

    private String querylog[];
    private int n;

    public static HashMap<String, Integer> calculateQt(String arr[]) {
        HashMap<String, Integer> hm = new HashMap<String, Integer>();
        int len = arr.length;
        int i;
        for (i = 0; i < len; i++) {
            String sp[] = arr[i].split(",");
            for (int j = 0; j < sp.length; j++) {

                if (!hm.containsKey(sp[j])) {
                    hm.put(sp[j], 1);
                } else {
                    int val = hm.get(sp[j]);
                    val++;
                    hm.put(sp[j], val);
                }
            }
        }
        return hm;
    }

    public static double Qtfrequency(String query, HashMap<String, Integer> hm) {
        double sum = 0;
        int i, j;

        String sp[] = query.split(",");
        double sum1 = 0;
        for (j = 0; j < sp.length; j++) {
            int get = hm.get(sp[j]);
            sum1 = sum1 + 0.66 * ((double) get / hm.size());
        }
        sum = sum + sum1;

        return sum;
    }

    public static double Dfrequency(String query, HashMap<String, Integer> hm, int Dint) {
        double sum = 0;
        int i, j;

        String sp[] = query.split(",");
        double sum1 = 0;
        for (j = 0; j < sp.length; j++) {
            int get = hm.get(sp[j]);
            sum1 = sum1 + 0.34 * ((double) get / Dint);
        }
        sum = sum + sum1;

        return sum;
    }

    public static HashMap<String, Integer> calculateD(String query[]) throws SQLException {
        HashMap<String, Integer> hm = new HashMap<String, Integer>();
        Statement con = DriverManager.getConnection(DBConstants.DB_DRIVER_TYPE.getConstValue() + ":" + DBConstants.DB_LOCATION.getConstValue()).createStatement();
        int i, j;
        for (i = 0; i < query.length; i++) {
            String sp[] = query[i].split(",");
            for (j = 0; j < sp.length; j++) {
                if (!hm.containsKey(sp[j])) {
                    ResultSet rs = con.executeQuery("SELECT count(name) AS Cname FROM mortoncode WHERE name LIKE '%" + sp[j] + "%'");
                    int res = rs.getInt("Cname");
                    hm.put(sp[j], res);
                } else {
                    int val = hm.get(sp[j]);
                    val = val + val;
                    hm.put(sp[j], val);
                }
            }
        }
        return hm;
    }

    public static List<String> getUniqueString(String[] querylog) {
        int i, j;
        List<String> l1 = new ArrayList<String>();
        for (i = 0; i < querylog.length; i++) {
            String sp[] = querylog[i].split(",");
            for (j = 0; j < sp.length; j++) {
                if (!l1.contains(sp[j])) {
                    l1.add(sp[j]);
                }
            }
        }
        return l1;
    }

    public void getData() throws SQLException {
        System.out.println("Enter number of Queries ");
        Scanner manu = new Scanner(System.in);
        n = manu.nextInt();
        querylog = new String[n];
        int i;
        String temp = manu.nextLine();
        for (i = 0; i < n; i++) {
            System.out.println("Please Enter Query " + (i + 1));
            querylog[i] = manu.nextLine();
        }
        Grouping gr = new Grouping();
        gr.mainGrouping(querylog, n);
    }
}
