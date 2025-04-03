package bap.jp.mvcbap.batch.bai3;


import org.springframework.batch.item.database.JdbcCursorItemReader;

import javax.sql.DataSource;

public class CleanupUserItemReader extends JdbcCursorItemReader<Integer> {

    public CleanupUserItemReader(DataSource dataSource, String cutoffDate) {
	setDataSource(dataSource);
	setSql("SELECT u.user_id FROM users u " +
		"WHERE NOT EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.user_id AND o.order_date >= ?)");
	setRowMapper((rs, rowNum) -> rs.getInt("user_id"));
	setPreparedStatementSetter(ps -> ps.setString(1, cutoffDate));
    }
}