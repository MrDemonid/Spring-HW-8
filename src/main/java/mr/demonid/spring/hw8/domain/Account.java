package mr.demonid.spring.hw8.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Счет.
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate creation;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User userParen;


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Account account = (Account) obj;
        return id.equals(account.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", creation=" + creation +
                ", user id=" + userParen.getId() +
                '}';
    }
}
