package util;

import btopskQuery.VirtualQuadtree;
import util.preprocessing.Object;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Input {
    public int no_of_users;
    public double latitude = 64;
    public double longitude = -145;
    public ArrayList<String> Query = new ArrayList<>();
    public ArrayList<List<String>> terms = new ArrayList<>();
    public int minDepth = 32;
    public List<String> Q_star = new ArrayList<>();
    public VirtualQuadtree e;
    public Map<String, HashMap<Double, Object>> R;
    private List<String> locations = new ArrayList<>();
    public Map<String, List<String>> clusMap[];
    public HashMap<String, List<Object>> clusters[];
    public static long start;

    public void getInput(File file) throws FileNotFoundException {
        //Scanner scan=new Scanner(System.in);
        BufferedReader inputfile = new BufferedReader(new FileReader(file));
        Scanner scan = new Scanner(inputfile);
        //System.out.println("Enter number of Queries?");
        System.out.println("Input taken from file : '" + file + "'");
        no_of_users = Integer.parseInt(scan.nextLine());
        for (int i = 0; i < no_of_users; i++) {
            String query = scan.nextLine();
            Query.add(query);
            locations.add(scan.nextLine());
            List<String> list = new ArrayList<>(Arrays.asList(query.split(" ")));
            terms.add(list);
        }
        System.out.println("Queries recorded : " + Query + "\nTerms recorded : " + terms);

    }

    public List<String> getUserTerms(int i) {
        return terms.get(i);
    }

    public Double getLongitude(int i) {
        String t[] = locations.get(i).split(",");
        return Double.parseDouble(t[1]);
    }

    public Double getLatitude(int i) {
        String t[] = locations.get(i).split(",");
        return Double.parseDouble(t[0]);
    }

    public int getIndex(String subQuery) {
        int index = 0, i = 0;
        for (String query : Query) {
            if (query.equals(subQuery)) {
                index = i;
                break;
            } else
                i++;
        }
        return index;
    }


}
