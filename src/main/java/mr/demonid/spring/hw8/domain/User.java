package mr.demonid.spring.hw8.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "userParen", cascade = CascadeType.ALL)
    private Set<Account> accounts = new HashSet<>();     // счета


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Добавления счёта пользователю, с учётом связей в БД.
     * @param account Новый счет.
     */
    public void addAccount(Account account) {
        account.setUserParen(this);
        accounts.add(account);
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
        account.setUserParen(this);
    }

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }


    public String getRolesList() {
        return roles.stream().map(Role::getName).toList().toString();
    }

    public String getAccountsList() {
        return accounts.stream().map(e -> "(" + e.getName() + ": " + e.getAmount() + " уе)").toList().toString();
    }

    public String getAccountsNameList() {
        return accounts.stream().map(Account::getName).toList().toString();

    }
    public String getAccountsAmountList() {
        return accounts.stream().map(Account::getAmount).toList().toString();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", accounts=" + getAccountsList() +
                ", roles=" + getRolesList() +
                '}';
    }
}
