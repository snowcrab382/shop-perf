package perf.shop.domain.outbox.dao;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BatchOutboxRepository {

    private final JdbcTemplate jdbcTemplate;

    public void bulkUpdateStatusToDone(List<String> orderIds) {
        String sql = "UPDATE outbox SET status = 'DONE' WHERE order_id IN (?)";
        jdbcTemplate.batchUpdate(sql, orderIds, orderIds.size(), (ps, orderId) -> {
            ps.setString(1, orderId);
        });
    }
}
