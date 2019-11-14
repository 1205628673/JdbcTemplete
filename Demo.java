package student.daoimpl;

import com.alibaba.druid.pool.DruidDataSource;
import student.dao.StudentDao;
import student.model.StudentOff;
import student.utils.*;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by cxr1205628673 on 2019/11/7.
 */
public class StudentDaoImpl implements StudentDao{
    private static JdbcConnectionFactory jdbcFactory = new JdbcConnectionFactory(new DruidDataSource());
    //new JdbcConnectionFactoryBuilder().build();//static保持线程安全,只有一个datasource
    private JdbcProxy jdbcProxy = jdbcFactory.getJdbcProxy();//每次生成的jdbcproxy都不同

    public Map<String, String> getStudentByStudentIdAndPassword(String studentId, String password) throws SQLException,NullPointerException{
        String sql = "SELECT s.id,s.student_id,s.student_name,s.major_id,s.last_time,m.name,a.name FROM student AS s LEFT JOIN major AS m ON " +
                "s.major_id=m.id LEFT JOIN academy AS a ON m.academy_id=a.id WHERE s.student_id=? AND s.password=?";
        Map<String,String> result  = jdbcProxy.queryForMap(sql,new Object[]{studentId,password});
        return result;
    }
    public int modifyPasswordByStudentId(String studentId,String oldPassword,String newPassword) throws SQLException,NullPointerException{
        String sql = "UPDATE student SET password=? WHERE student_id=? AND password=?";
        int effect = jdbcProxy.update(sql,new Object[]{newPassword,studentId,oldPassword});
        return effect;
    }
    public Map<String, String> getStudentByStudentId(String studentId) throws SQLException,NullPointerException{
        String sql = "SELECT s.id,s.student_id,s.student_name,s.major_id,s.last_time,m.name major_name,a.name academy_name FROM " +
                "student AS s LEFT JOIN major AS m ON s.major_id=m.id LEFT JOIN academy AS a ON m.academy_id=a.id WHERE s.student_id=?";
        Map<String,String> result = jdbcProxy.queryForMap(sql,new Object[]{studentId});

        return result;
    }
    public List<Map<String,String>> getTodayAllStudentoffByStudentid(String studentId,String now)throws SQLException,NullPointerException{
        String sql = "SELECT s.id,s.student_name,s.student_id,s.major_id,so.reason,so.start_time,so.end_time," +
                "so.day,so.hour,so.signature_time,so.signature,so.result,t.teacher_name,m.name major_name,a.name academy_name FROM " +
                "student AS s LEFT JOIN student_off AS so ON s.student_id=so.student_id LEFT JOIN teacher AS t ON so.teacher_id=t.teacher_id" +
                "LEFT JOIN major AS m ON s.major_id=m.id LEFT JOIN academy AS a ON m.academy_id=a.id WHERE s.student_id=? AND unix_timestamp(so.start_time)<unix_timestamp(?) AND " +
                "unix_timestamp(?)<unix_timestamp(so.end_time)";

        List<Map<String,String>> result = jdbcProxy.queryForList(sql,new Object[]{studentId,now,now});
        return result;
    }
    public List<Map<String, String>> getAllStudentOffByStudentId(String studentId)  throws SQLException,NullPointerException{
        String sql = "SELECT s.id,s.student_name,s.student_id,s.major,s.academy,so.reason,so.start_time,so.end_time," +
                "so.day,so.hour,so.signature_time,so.signature,so.result,t.teacher_name,m.name major_name,a.name academy_name FROM " +
                "student AS s LEFT JOIN student_off AS so ON s.student_id=so.student_id LEFT JOIN teacher AS t ON so.teacher_id=t.teacher_id" +
                "LEFT JOIN major AS m ON s.major_id=m.id LEFT JOIN academy AS a ON m.academy_id=a.id WHERE s.student_id=?";

        List<Map<String,String>> result = jdbcProxy.queryForList(sql,new Object[]{studentId});
        return result;
    }

    public Map<String, String> getStudentOffByOffId(String studentOffId) throws SQLException,NullPointerException{
        return null;
    }
    public Object test(String studentId,String password) throws SQLException{
        /*String sql = "SELECT s.id,s.student_id,s.student_name,s.major_id,s.last_time,m.name,a.name FROM student AS s LEFT JOIN major AS m ON " +
                "s.major_id=m.id LEFT JOIN academy AS a ON m.academy_id=a.id WHERE s.student_id=? AND s.password=?";
        List<Map<String,String>> list = jdbcProxy.queryForList(sql,new Object[]{studentId,password});
        */
        jdbcProxy.execute(new TransactionCallback(){
            @Override
            public Object doTransaction(TransactionStatus transactionStatus) {
                String sql = "insert into student(student_id,student_name,major_id,password) values(?,?,?,?)";
                Object savePoint = transactionStatus.createSavePoint();
                try {
                    jdbcTemplete.update(sql, new Object[]{"12172115", "bobo","1","1"});
                    int w = 1/0;
                    jdbcTemplete.update(sql, new Object[]{"12172130", "zongrui","1","1"});
                }catch (SQLException sqle){
                    transactionStatus.rollback();
                    sqle.printStackTrace();
                }catch (Exception e){
                    transactionStatus.rollback();
                    e.printStackTrace();
                }
                return savePoint;
            }
        });
        return null;
    }

    public int addStudentOff(StudentOff studentOff)  throws SQLException,NullPointerException{
        String sql = "INSERT INTO student_off(student_id,teacher_id,reason,start_time,end_time,day,hour) VALUES(?,?,?,?,?,?)";
        int effect = jdbcProxy.update(sql,new Object[]{studentOff.getStudentId(),studentOff.getTeacherId(),studentOff.getReason()
        ,studentOff.getStartTime(), studentOff.getEndTime(),studentOff.getDay(),studentOff.getHour()});
        return effect;
    }
}
