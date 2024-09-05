package perf.shop.domain.outbox.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import perf.shop.domain.outbox.dao.OutboxRepository;
import perf.shop.domain.outbox.domain.Outbox;
import perf.shop.global.error.exception.EntityNotFoundException;
import perf.shop.global.error.exception.GlobalErrorCode;

@Transactional
@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;

    public void createOutbox(String orderId) {
        outboxRepository.save(Outbox.from(orderId));
    }

    public void updateStatusToDone(String orderId) {
        Outbox outbox = getOutbox(orderId);
        outbox.Done();
    }

    private Outbox getOutbox(String orderId) {
        return outboxRepository.findByOrderId(orderId).orElseThrow(
                () -> new EntityNotFoundException(GlobalErrorCode.OUTBOX_NOT_FOUND)
        );
    }
}
