package com.example.chapter06.job_stop.dao;

import com.example.chapter06.job_stop.domain.Transaction;
import com.example.chapter06.job_stop.domain.TransactionDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class TransactionDaoSupport extends JdbcTemplate implements TransactionDao {

    public TransactionDaoSupport(DataSource dataSource) {
        super(dataSource);
    }

//    @SuppressWarnings("unchecked")
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        String query = "select t.id, t.timestamp, t.amount " +
                "from TRANSACTION t " +
                "   inner join ACCOUNT_SUMMARY a on a.id = t.account_summary_id " +
                "where a.account_number = ?";

        return query(query, getTransactionRowMapper(), accountNumber);
    }

    private RowMapper<Transaction> getTransactionRowMapper() {
        return (rs, rowNum) -> {
            Transaction trans = new Transaction();
            trans.setAmount(rs.getDouble("amount"));
            trans.setTimestamp(rs.getDate("timestamp"));
            return trans;
        };
    }
}
