package mr.demonid.spring.hw8.repository;

import mr.demonid.spring.hw8.dto.TransactionOperation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<TransactionOperation, Long> {


}
