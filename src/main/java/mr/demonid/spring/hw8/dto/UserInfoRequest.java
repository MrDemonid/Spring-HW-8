package mr.demonid.spring.hw8.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoRequest {
    private Long id;
    private String username;
    private String email;

    private String payId;
    private String amount;

    private String roles;
}
