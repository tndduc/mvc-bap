package bap.jp.mvcbap.batch.bai3;


import org.springframework.batch.item.database.JdbcCursorItemReader;
import javax.sql.DataSource;


public class CleanupProductItemReader extends JdbcCursorItemReader<Integer> {

    public CleanupProductItemReader(DataSource dataSource, String cutoffDate) {
	setDataSource(dataSource);
	setSql("SELECT p.product_id FROM products p " +
		"WHERE NOT EXISTS (SELECT 1 FROM order_items oi " +
		"JOIN orders o ON oi.order_id = o.order_id " +
		"WHERE oi.product_id = p.product_id AND o.order_date >= ?)");
	setRowMapper((rs, rowNum) -> rs.getInt("product_id"));
	setPreparedStatementSetter(ps -> ps.setString(1, cutoffDate));
    }
}