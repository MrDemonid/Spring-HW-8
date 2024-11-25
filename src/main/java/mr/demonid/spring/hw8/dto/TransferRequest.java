package mr.demonid.spring.hw8.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {

    private Long fromUserId;
    private Long recipientId;
    private BigDecimal transferAmount;

}
