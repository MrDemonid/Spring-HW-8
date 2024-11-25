package mr.demonid.spring.hw8.repository;

import mr.demonid.spring.hw8.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findAccountByName(String name);

    Boolean existsAccountByName(String name);
}
