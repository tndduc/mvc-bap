package bap.jp.mvcbap.entity;

import bap.jp.mvcbap.entity.type.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Integer id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "role",nullable = false)
    private Role role;

    @Column(name = "password", nullable = false)
    private String password;

    @ColumnDefault("0")
    @Column(name = "delete_flg")
    private Boolean deleteFlg;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;
    public User() {}

    public User(String userName, String password, Role role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.deleteFlg = false;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDeleteFlg() {
        return deleteFlg;
    }

    public void setDeleteFlg(Boolean deleteFlg) {
        this.deleteFlg = deleteFlg;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}