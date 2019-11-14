package student.utils;

import java.util.List;

/**
 * Created by cxr1205628673 on 2019/11/13.
 */
public abstract class TransactionCallback {
    protected JdbcProxy jdbcTemplete;
    public abstract Object doTransaction(TransactionStatus transactionStatus);

    public JdbcProxy getJdbcTemplete() {
        return jdbcTemplete;
    }

    public void setJdbcTemplete(JdbcProxy jdbcTemplete) {
        this.jdbcTemplete = jdbcTemplete;
    }
}

