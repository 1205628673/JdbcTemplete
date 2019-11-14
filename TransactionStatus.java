package student.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * Created by cxr1205628673 on 2019/11/13.
 */
public class TransactionStatus {
    private Connection conn;
    public Object createSavePoint(){
        Savepoint savepoint = null;
        try {
            conn.setAutoCommit(false);
            savepoint = conn.setSavepoint();
        }catch(Exception e){
            e.printStackTrace();
        }
        return savepoint;
    }
    public void rollback(){
        try {
            conn.rollback();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void rollback(Object savepoint){
        try {
            conn.rollback((Savepoint) savepoint);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }
}
