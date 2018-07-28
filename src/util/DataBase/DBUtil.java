package util.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {

    private static DBUtil _util;

    private static Connection con;

    private DBUtil() {
    }

    public static DBUtil getUtil() {
        if (_util == null) {
            _util = new DBUtil();
            try {
                con=DriverManager.getConnection(DBConstants.DB_DRIVER_TYPE.getConstValue() + ":" + DBConstants.DB_LOCATION.getConstValue());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return _util;
    }

    public Connection getConnection() {
        if (con == null) {
            con = _util.getConnection();
        }
        return con;
    }


}
