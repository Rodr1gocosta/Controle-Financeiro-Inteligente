package br.com.financeiro.financeiro.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "TB_PLANNING")
public class Planning implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private BigDecimal totalPlanned;

    @OneToMany(mappedBy = "planning", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Categories> categories;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_PLANNINGS_USERS",
            joinColumns = @JoinColumn(name = "planning_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> userList;

}
