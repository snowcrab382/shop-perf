package perf.shop.domain.order.dao;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
@RequiredArgsConstructor
public class BatchOrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public void bulkApproveOrders(List<String> orderIds) {
        String sql = "UPDATE orders SET state = 'PAYMENT_APPROVED' WHERE id IN (?)";
        jdbcTemplate.batchUpdate(sql, orderIds, orderIds.size(), (ps, orderId) -> {
            ps.setString(1, orderId);
        });
    }
}
