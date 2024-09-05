package perf.shop.domain.payment.dao;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import perf.shop.domain.payment.domain.Payment;

@Repository
@RequiredArgsConstructor
public class BatchPaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public void bulkInsert(List<Payment> payments) {
        String sql =
                "INSERT INTO payment (order_id, order_name, total_amount, payment_key, type, status, requested_at, approved_at, created_at, modified_at) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, payments, payments.size(), (ps, payment) -> {
            ps.setString(1, payment.getOrderId());
            ps.setString(2, payment.getOrderName());
            ps.setLong(3, payment.getTotalAmount());
            ps.setString(4, payment.getPaymentKey());
            ps.setString(5, payment.getType().name());
            ps.setString(6, payment.getStatus().name());
            ps.setTimestamp(7, Timestamp.from(payment.getRequestedAt().toInstant()));
            ps.setTimestamp(8, Timestamp.from(payment.getApprovedAt().toInstant()));
            ps.setTimestamp(9, Timestamp.from(Instant.now()));
            ps.setTimestamp(10, Timestamp.from(Instant.now()));
        });
    }
}
