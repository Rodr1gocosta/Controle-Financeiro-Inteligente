package br.com.financeiro.financeiro.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "TB_USER")
@Getter
@Setter
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean status;

    @Column(length = 20, nullable = false)
    private String cpf;

    @Column(length = 20, nullable = false)
    private String phoneNumber;

    @ManyToMany(mappedBy = "userList", fetch = FetchType.LAZY)
    private List<Planning> planningList;
}
