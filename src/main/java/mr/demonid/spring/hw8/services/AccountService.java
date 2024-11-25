package mr.demonid.spring.hw8.services;

import lombok.AllArgsConstructor;
import mr.demonid.spring.hw8.aspect.TrackUserAction;
import mr.demonid.spring.hw8.domain.Account;
import mr.demonid.spring.hw8.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountService {

    private static Random random = new Random();

    private AccountRepository accountRepository;

    /**
     * Создаёт новый счёт, не привязанный ни к кому.
     */
    @TrackUserAction
    public Account createNewAccount() {
        Account account = new Account();
        // добавляем имя счета (карты, тут это не важно)
        do {
            Long nameStr = random.nextLong(100000000, 999999999 + 1);
            // создаем красивый номер счета, вида xxx-xxx-xxx
            account.setName(String.format(
                    "%03d-%03d-%03d", nameStr / 1000000L, (nameStr % 1000000L) / 1000, nameStr % 1000));
        } while (accountRepository.existsAccountByName((account.getName())));

        // для примера закидываем денежек
        account.setAmount(BigDecimal.valueOf(1000));
        // и дату создания
        account.setCreation(LocalDate.now());
        return account;
    }


}
