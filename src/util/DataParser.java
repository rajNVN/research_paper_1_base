package util;

import org.apache.lucene.spatial.util.MortonEncoder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.DataBase.DBUtil;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;


public class DataParser {
    public static void parseGNData() throws Exception {
        Connection con = DBUtil.getUtil().getConnection();
        Statement stat = con.createStatement();
        //BufferedWriter file=new BufferedWriter(new FileWriter("F:\\Mortons.txt"));
        ResultSet resultSet = stat.executeQuery("select * from GNquery;");
        long start = System.nanoTime();
        while (resultSet.next()) {
            double lat = resultSet.getDouble(2);
            double lon = resultSet.getDouble(3);
            int i = resultSet.getInt(1);
            String string = Long.toBinaryString(MortonEncoder.encode(lat, lon));
            System.out.println("ID->" + i + "\tLatitude--->" + lat + "\tLongitude--->" + lon + "\t" + string);

            ///editing row using result set
            //resultSet.updateString(4, "" + string);
            //resultSet.updateRow();

            ///to write into txt file
            //file.write(string);
            //file.newLine();
            long end = System.nanoTime();
            System.out.println((end - start) / 1000000000);
        }
        long end = System.nanoTime();
        System.out.println((end - start) / 1000000000);
    }

    public static void convertDataset() throws Exception {
        Connection con = DBUtil.getUtil().getConnection();
        Statement stat = con.createStatement();
        File myFile = new File("G:\\reaserch_workspace\\TopskSearchQuery\\extras\\SamS.xlsx");
        FileInputStream fs = new FileInputStream(myFile);
        XSSFWorkbook wb = new XSSFWorkbook(fs);//workbook to read
        XSSFSheet mySheet = wb.getSheetAt(0);
        int index = 0;
        long start = System.nanoTime();
        for (Row row : mySheet) {
            double latitude = 0, longitude = 0;
            String name = "", desc = "";
            for (Cell cell : row) {
                switch (cell.getColumnIndex()) {
                    case 0:
                        latitude = cell.getNumericCellValue();
                        break;
                    case 1:
                        longitude = cell.getNumericCellValue();
                        break;
                    case 2:
                        name = cell.getStringCellValue();
                    default: {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                if (!desc.matches("")) desc += ", " + cell.getNumericCellValue();
                                else desc += cell.getNumericCellValue();
                                break;
                            case Cell.CELL_TYPE_STRING:
                                if (!desc.matches("")) desc += ", " + cell.getStringCellValue();
                                else desc += cell.getStringCellValue();
                                break;

                        }
                    }
                }
            }
            long mortonCode = MortonEncoder.encode(latitude, longitude);
            System.out.println(mortonCode);
            ++index;
            stat.executeUpdate("insert into EuroDataset(id,latitude,longitude,mcode,name)values(" + index + "," + latitude + "," + longitude + ",\"" + Long.toBinaryString(mortonCode) + "\",\"" + name + "\");");
        }
        System.out.println("total time (seconds): " + (System.nanoTime() - start) / 1000000000);
    }

    public static void parseEuroData() throws Exception {
        Statement stat = DBUtil.getUtil().getConnection().createStatement();
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File("G:\\reaserch_workspace\\TopskSearchQuery\\extras\\SamS.xlsx")));//workbook to read
        XSSFSheet mySheet = wb.getSheetAt(0);
        for (Row row : mySheet) {
            double latitude = 0, longitude = 0;
            String name = "", description = "";
            for (Cell cell : row) {
                switch (cell.getColumnIndex()) {
                    case 0:
                        // System.out.println("case 0 and value: " + cell.getStringCellValue());
                        latitude = cell.getNumericCellValue();
                        break;
                    case 1:
                        longitude = cell.getNumericCellValue();
                        break;
                    case 2:
                        name = cell.getStringCellValue();
                        break;
                    default: {
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                if (!description.matches(""))
                                    description += ", " + cell.getNumericCellValue();
                                else
                                    description += cell.getNumericCellValue();
                                break;
                            case Cell.CELL_TYPE_STRING:
                                if (!description.matches(""))
                                    description += ", " + cell.getStringCellValue();
                                else
                                    description += cell.getStringCellValue();
                        }
                    }
                }
            }
            stat.executeUpdate("insert into SpatialData(Latitude,Longitude,Name,Details) values(" + latitude + "," + longitude + ",\"" + name + "\",\"" + description + "\");");
        }
    }
    public static void copyEuro() throws FileNotFoundException, SQLException {
        Statement statement= DBUtil.getUtil().getConnection().createStatement();
        long start= System.nanoTime();
        File file = new File("/run/media/rajnvn/SHARED/research/euro&uk/");
        int no_of_files=0;
        double latitude,longitude;
        String name,description,mCode;
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.isFile()) {
                String type=f.getName();
                if(type.endsWith(".csv2"))
                    type=type.substring(0,type.length()-11);
                else
                    type=type.substring(0,type.length()-10);
                System.out.println("------------------------->File name : " + type);
                Scanner scanner = new Scanner(f);
                int index=0;
                while (scanner.hasNextLine()) {
                    String line=scanner.nextLine();
                    if(!line.startsWith(";")) {
                        try {
                            int in = line.indexOf(",");
                            longitude = Double.parseDouble(line.substring(0, in));
                            line=line.substring(in+1);

                            in = line.indexOf(",");
                            latitude = Double.parseDouble(line.substring(0, in));
                            line=line.substring(in+2);

                            in=line.indexOf("\"");
                            name=line.substring(0,in);
                            line=line.substring(in+3);

                            in=line.indexOf("\"");
                            description=line.substring(0,in);
                            if(description.equals(""))
                                description=" ";
                            mCode=Long.toBinaryString(MortonEncoder.encode(latitude,longitude));
                            System.out.println("insert into Euro values(" + index + "," + latitude + "," + longitude + ",'" + mCode + "','" + name + "','" + description + "','" + type + "');");
                            statement.executeUpdate("insert into Euro values(" + index + "," + latitude + "," + longitude + ",'" + mCode + "',\"" + name + "\",\"" + description + "\",\"" + type + "\");");
                            index++;
                            System.out.println((double)(System.nanoTime()-start)/1000000000);
                        }catch (StringIndexOutOfBoundsException e)
                        {
                            continue;
                        }

                    }
                }
                no_of_files++;
            }
        }
        System.out.println(no_of_files + " lines recorded");
        System.out.println((double)(System.nanoTime()-start)/1000000000);
    }

    public static void getTerms(int no_of_terms,int no_of_occurences,String table_name) throws SQLException {
        Map<String, Integer> stringEntries = new HashMap<>();
        Statement statement = DBUtil.getUtil().getConnection().createStatement();
        ResultSet rs = statement.executeQuery("select * from "+table_name+";");
        while (rs.next()) {
            if (rs.getString("name").split(" ").length ==no_of_terms) {
                String string = rs.getString("name");
                Integer count = stringEntries.get(string);
                if (count == null)
                    stringEntries.put(string, 1);
                else
                    stringEntries.put(string, count + 1);
                //System.out.println(rs.getInt("id") + "---->" + rs.getString("name"));
            }
        }
        int count=0;
        for (HashMap.Entry<String, Integer> entry : stringEntries.entrySet())
            if (entry.getValue() >= no_of_occurences) {
                double latitude=0,longitude=0;
                boolean flag= true;
                while(flag) {
                    if(latitude<180 && latitude>-180 && longitude<90 && longitude>-90 && latitude != 0.0 && longitude != 0.0) {
                        System.out.println(entry.getKey());
                        System.out.println(latitude + "," + longitude);
                        flag= false;
                    }
                    latitude=Math.random();
                    longitude=Math.random();
                }
                count++;
            }
        System.out.println("count - "+count);
    }
    //created at 5-5-18
    //used : 8-7-18
    public static void setMorton() throws SQLException, FileNotFoundException {
        PrintWriter printWriter=new PrintWriter("setMorton-2.txt");
        Connection connection = DBUtil.getUtil().getConnection();
        Statement stat = connection.createStatement();
        ResultSet rs= stat.executeQuery("select *from queries;");
        Set<String> queries = new HashSet<>();
        long start = System.nanoTime();
        while (rs.next())
        {
            double longitude=rs.getDouble("longitude");
            double latitude=rs.getDouble("latitude");
            long morton = MortonEncoder.encode(latitude,longitude);
            String mCode=Long.toBinaryString(morton);
            queries.add("update queries set mCode='"+mCode+"' where longitude="+longitude+" and latitude="+latitude+";");
            System.out.println("update queries set mCode='"+mCode+"' where Long="+longitude+" and Lat="+latitude+";");
            stat.executeUpdate("update queries set mCode='"+mCode+"' where longitude="+longitude+" and latitude="+latitude+";");
        }
        int i=0;
        for (String sql:queries) {
            System.out.println(sql + "\n" + (double) (System.nanoTime() - start) / 1000000000+" | "+i+" - "+queries.size());
            printWriter.write(sql + " - " + (double) (System.nanoTime() - start) / 1000000000 + "\n");
            stat.executeUpdate(sql);
            i++;
        }
        printWriter.close();
    }

    public static void generateInset(int terms,int queries,int k,String table) throws SQLException, FileNotFoundException {
        PrintWriter writer = new PrintWriter("G:\\euro\\"+terms+"_"+queries+"_"+k+".txt");
        writer.write(queries+"");
        Map<String, Integer> stringEntries = new HashMap<>();
        Statement statement = DBUtil.getUtil().getConnection().createStatement();
        ResultSet rs = statement.executeQuery("select * from "+table+";");
        while (rs.next()) {
            if (rs.getString("name").split(" ").length ==terms) {
                String string = rs.getString("name");
                Integer count = stringEntries.get(string);
                if (count == null)
                    stringEntries.put(string, 1);
                else
                    stringEntries.put(string, count + 1);
                //System.out.println(rs.getInt("id") + "---->" + rs.getString("name"));
            }
        }
        Set<String> queris=new HashSet<>();
        boolean yet=true;
        for (HashMap.Entry<String, Integer> entry : stringEntries.entrySet()) {
            if (yet) {
                if (entry.getValue() >= k) {
                    queris.add(entry.getKey());
                    if (queris.size() == queries)
                        yet = false;
                }
            }
        }
        Scanner scanner = new Scanner(new File("_30.txt"));
        while (queris.size() < queries) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (terms == 1)
                    queris.add(line.split(" ")[1]);
                else if (terms == 2)
                    queris.add(line.split(" ")[1] + " " + line.split(" ")[2]);
                else
                    queris.add(line);
            }
        }
        for(String query:queris)
        {
            double latitude=0,longitude=0;
            boolean flag= true;
            while(flag) {
                if(latitude<60 && latitude>50 && longitude<-150 && longitude>-160 && latitude != 0.0 && longitude != 0.0) {
                    System.out.println(query);
                    writer.write("\n"+query);
                    System.out.println(latitude + "," + longitude);
                    writer.write("\n"+latitude + "," + longitude);
                    flag= false;
                }
                latitude=Math.random()*100;
                longitude=-Math.random()*1000;
            }
        }
        writer.close();
        System.out.println("count - "+queris.size());
    }

    public static void generaterRawTersms(int k,String table) throws FileNotFoundException, SQLException {
        PrintWriter writer = new PrintWriter("_"+30+".txt");
        Map<String, Integer> stringEntries = new HashMap<>();
        Statement statement = DBUtil.getUtil().getConnection().createStatement();
        ResultSet rs = statement.executeQuery("select * from "+table+";");
        while (rs.next()) {
            String string = rs.getString("name");
            if(string.split(" ").length==3) {
                Integer count = stringEntries.get(string);
                if (count == null)
                    stringEntries.put(string, 1);
                else
                    stringEntries.put(string, count + 1);
                //System.out.println(rs.getInt("id") + "---->" + rs.getString("name"));
            }
        }
        for (HashMap.Entry<String, Integer> entry : stringEntries.entrySet())
            if (entry.getValue() >= k)
                writer.write("\n" + entry.getKey());
        writer.close();
    }

}
