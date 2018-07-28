package util.DataBase;
public enum DBConstants {

    DB_DRIVER_TYPE("jdbc:sqlite"),
    DB_LOCATION("G:"+System.getProperty("file.separator")+"euro"+System.getProperty("file.separator")+"quadtree.db.enc");
    //DB_LOCATION("/run/media/rajnvn/TECHNIE/quadtree.db.enc");
    //DB_LOCATION("F:\\util.preprocessing.db.enc");

    private String constValue = null;

    DBConstants(String constValue){
        this.setConstValue(constValue);
    }






    public String getConstValue() {
        return constValue;
    }

    private void setConstValue(String constValue) {
        this.constValue = constValue;
    }
}
