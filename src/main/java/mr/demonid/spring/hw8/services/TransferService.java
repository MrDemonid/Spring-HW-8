package mr.demonid.spring.hw8.services;

import lombok.AllArgsConstructor;
import mr.demonid.spring.hw8.aspect.TrackUserAction;
import mr.demonid.spring.hw8.domain.Account;
import mr.demonid.spring.hw8.domain.User;
import mr.demonid.spring.hw8.dto.TransactionOperation;
import mr.demonid.spring.hw8.dto.TransferRequest;
import mr.demonid.spring.hw8.repository.HistoryRepository;
import mr.demonid.spring.hw8.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TransferService {

    private UserRepository userRepository;
    private HistoryRepository historyRepository;


    /**
     * Скорее всего добровольная передача денег от одного клиента к другому.
     */
    @TrackUserAction
    @Transactional
    public void transfer(TransferRequest request) throws Exception {
        Optional<User> optionalFrom = userRepository.findById(request.getFromUserId());
        Optional<User> optionalTo = userRepository.findById(request.getRecipientId());
        if (optionalFrom.isEmpty() || optionalTo.isEmpty()) {
            throw new Exception("Пользователь не найден!");
        }
        User userFrom = optionalFrom.get();
        User userTo = optionalTo.get();
        // проверяем возможность операции
        BigDecimal amount = request.getTransferAmount();
        Account accountFrom = userFrom.getAccounts().stream().findFirst().get();
        Account accountTo = userTo.getAccounts().stream().findFirst().get();
        if (amount.compareTo(BigDecimal.ZERO) <= 0 || amount.compareTo(accountFrom.getAmount()) > 0) {
            throw new Exception("Невозможно перевести указанную сумму.");
        }
        // обновляем и сохраняем данные
        accountFrom.setAmount(accountFrom.getAmount().subtract(amount));
        accountTo.setAmount(accountTo.getAmount().add(amount));
        userRepository.save(userTo);
        userRepository.save(userFrom);
        storeHistory(userFrom, userTo, amount);
    }

    /**
     * Сохраняет успешную транзакцию в историю.
     */
    private void storeHistory(User from, User to, BigDecimal amount) {
        TransactionOperation op = new TransactionOperation();
        op.setFromUserId(from.getId());
        op.setRecipientId(to.getId());
        op.setTransferAmount(amount);
        historyRepository.save(op);
    }
}
