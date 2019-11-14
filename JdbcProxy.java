package student.utils;

import com.beust.jcommander.internal.Lists;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by cxr1205628673 on 2019/11/7.
 */
public class JdbcProxy {
    private static JdbcConnectionFactory factory = JdbcConnectionFactoryBuilder.build();
    private Connection conn = factory.getConnection();//每个templete使用一个connection,这样不出现瓶颈
    public JdbcProxy(JdbcConnectionFactory factory) {
        this.factory = factory;
    }

    private static void close(PreparedStatement pstmt) throws SQLException{
        pstmt.close();
    }
    private static void close(Connection conn) throws SQLException{
        conn.close();
    }
    public Map queryForMap(String sql,Object[] args) throws SQLException{
        Map row = new HashMap();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pstmt.setObject(i + 1, args[i]);
            }
            ResultSet resultSet = pstmt.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            row = new HashMap();
            resultSet.next();
            for (int j = 0; j < resultSetMetaData.getColumnCount(); j++) {
                String colName = resultSetMetaData.getColumnName(j + 1);
                String colVal = String.valueOf(resultSet.getObject(colName));
                row.put(colName, colVal);
            }
        close(pstmt);
        return row;
    }

    public List queryForList(String sql,Object[] args) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i=0;i<args.length;i++) {
            pstmt.setObject(i+1,args[i]);
        }
        List colNames = new ArrayList();
        List rowList = new ArrayList();
        ResultSet resultSet = pstmt.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for (int j=0;j<resultSetMetaData.getColumnCount();j++) {
            colNames.add(resultSetMetaData.getColumnName(j+1));
        }
        while (resultSet.next()) {
            Map row = new HashMap();
            for (int z = 0; z < colNames.size(); z++) {
                String colName = (String)colNames.get(z);
                row.put(colName,String.valueOf(resultSet.getObject(colName)));
            }
            rowList.add(row);
        }
        close(pstmt);
        return rowList;
    }
    public List queryForList(String sql) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql);
        List colNames = new ArrayList();
        List rowList = new ArrayList();
        ResultSet resultSet = pstmt.executeQuery();
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        for (int j=0;j<resultSetMetaData.getColumnCount();j++) {
            colNames.add(resultSetMetaData.getColumnName(j+1));
        }
        while (resultSet.next()) {
            Map row = new HashMap();
            for (int z = 0; z < colNames.size(); z++) {
                String colName = (String)colNames.get(z);
                row.put(colName,String.valueOf(resultSet.getObject(colName)));
            }
            rowList.add(row);
        }
        close(pstmt);
        return rowList;
    }
    public String queryForString(String sql,Object... args) throws SQLException{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i=0;i<args.length;i++) {
            pstmt.setObject(i+1,args[i]);
        }
        ResultSet resultSet = pstmt.executeQuery();
        String colVal = null;
        while(resultSet.next()){
            colVal = resultSet.getString(1);
        }
        close(pstmt);
        return colVal;
    }
    public String queryForString(String sql) throws SQLException{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        String colVal = null;
        while(resultSet.next()){
            colVal = resultSet.getString(1);
        }
        close(pstmt);
        return colVal;
    }
    public int queryforInt(String sql,Object... args) throws SQLException{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i=0;i<args.length;i++) {
            pstmt.setObject(i+1,args[i]);
        }
        ResultSet resultSet = pstmt.executeQuery();
        Integer colVal = null;
        while(resultSet.next()){
            colVal = resultSet.getInt(1);
        }
        close(pstmt);
        return colVal;
    }
    public int queryforInt(String sql) throws SQLException{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        Integer colVal = null;
        while(resultSet.next()){
            colVal = resultSet.getInt(1);
        }
        close(pstmt);
        return colVal;
    }
    public long queryforLong(String sql,Object... args) throws SQLException{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i=0;i<args.length;i++) {
            pstmt.setObject(i+1,args[i]);
        }
        ResultSet resultSet = pstmt.executeQuery();
        Long colVal = null;
        while(resultSet.next()){
            colVal = resultSet.getLong(1);
        }
        close(pstmt);
        return colVal;
    }
    public long queryforLong(String sql) throws SQLException{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet resultSet = pstmt.executeQuery();
        Long colVal = null;
        while(resultSet.next()){
            colVal = resultSet.getLong(1);
        }
        close(pstmt);
        return colVal;
    }
    public <T> T queryforObject(String sql,Class<T> requireType,Object... args) throws SQLException,NoSuchMethodException,
            IllegalAccessException,InstantiationException,InvocationTargetException{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for (int i=0;i<args.length;i++) {
            pstmt.setObject(i+1,args[i]);
        }
        ResultSet resultSet = pstmt.executeQuery();
        T obj = null;
        if(requireType.equals(java.lang.Integer.class) ||
                requireType.equals(java.lang.Byte.class) ||
                requireType.equals(java.lang.Long.class) ||
                requireType.equals(java.lang.Double.class) ||
                requireType.equals(java.lang.Float.class) ||
                requireType.equals(java.lang.Character.class) ||
                requireType.equals(java.lang.Short.class) ||
                requireType.equals(java.lang.Boolean.class)){
            while(resultSet.next()){
                obj = (T)resultSet.getObject(1);
            }
        }else{
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            resultSet.next();
            for (int i=0;i<resultSetMetaData.getColumnCount();i++){
                String colName = resultSetMetaData.getColumnName(i+1);
                Object colVal = resultSet.getObject(0);
                int colType = resultSetMetaData.getColumnType(i+1);
                Class dataType = null;
                switch(colType){
                    case Types.LONGVARCHAR: //-1
                        dataType=Long.class;
                        break;
                    case Types.CHAR:    //1
                        dataType=Character.class;
                        break;
                    case Types.NUMERIC: //2
                        dataType=Float.class;
                        break;
                    case Types.VARCHAR:  //12
                        dataType=String.class;
                        break;
                    case Types.DATE:  //91
                        dataType= java.util.Date.class;
                        break;
                    case Types.TIMESTAMP: //93
                        dataType= Date.class;
                        break;
                    case Types.BLOB :
                        dataType=Blob.class;
                        break;
                    default:
                        dataType=String.class;
                }
                Method method = requireType.getMethod("set"+colName.substring(0,0).toUpperCase()+colName.substring(1));
                Object instance = requireType.newInstance();
                method.invoke(instance,colVal);
            }
        }
        close(pstmt);
        return obj;
    }
    public int update(String sql,Object[] args) throws SQLException{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(int i=0;i<args.length;i++) {
            pstmt.setObject(i+1,args[i]);
        }
        int effect  = pstmt.executeUpdate();
        return effect;
    }
    public void execute(TransactionCallback transactionCallBack) throws SQLException{
        conn.setAutoCommit(false);
        transactionCallBack.setJdbcTemplete(this);
        TransactionStatus transactionStatus = new TransactionStatus();
        transactionStatus.setConn(conn);
        transactionCallBack.doTransaction(transactionStatus);
        conn.commit();
        close(conn);
    }
    public int insert(String sql,Object[] args) throws SQLException{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(int i=0;i<args.length;i++) {
            pstmt.setObject(i+1,args[i]);
        }
        int effect  = pstmt.executeUpdate();
        return effect;
    }
    public int delete(String sql,Object[] args) throws SQLException{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        for(int i=0;i<args.length;i++) {
            pstmt.setObject(i+1,args[i]);
        }
        int effect  = pstmt.executeUpdate();
        return effect;
    }
    public int delete(String sql) throws SQLException{
        PreparedStatement pstmt = conn.prepareStatement(sql);
        int effect  = pstmt.executeUpdate();
        return effect;
    }

}

