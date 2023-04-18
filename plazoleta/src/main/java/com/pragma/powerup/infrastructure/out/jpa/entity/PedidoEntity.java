package com.pragma.powerup.infrastructure.out.jpa.entity;

import com.pragma.powerup.domain.model.Estados;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "pedidos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PedidoEntity implements Serializable {



    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private LocalDateTime fecha;
    @Enumerated(value = EnumType.STRING)
    private Estados estado;

    @ManyToOne(fetch = FetchType.LAZY)
    private RestauranteEntity idRestaurante;
    
    private Long idCliente;
    @Column(nullable = true)
    private Long idChef;



    @PrePersist
    public void prePersist() {
        fecha = LocalDateTime.now();
    }


}
