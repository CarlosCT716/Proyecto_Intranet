package com.intranet.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_cab_boleta")
@Getter
@Setter
public class Boleta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_boleta")
	private Integer idBoleta;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_alumno")
	private Usuario usuario;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "fecha")
	private LocalDateTime fecha;
    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleBoleta> detalles = new ArrayList<>();

    public double getTotal() {
        return detalles.stream().mapToDouble(DetalleBoleta::getMonto).sum();
    }
}
