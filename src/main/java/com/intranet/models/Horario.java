package com.intranet.models;

import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Entity
@Table(name = "tb_horarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class Horario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_horario")
	private Integer idHorario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_curso")
	private Curso curso;
	
	@Column(name = "dia_semana")
	private String Dia;
	
	@Column(name = "hora_inicio")
	private LocalTime inicio;
	
	@Column(name = "hora_fin")
	private LocalTime fin;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="id_aula")
	private Aula aula;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="modalidad")
	private Modalidad modalidad;
	
	@Column(name = "estado", columnDefinition = "BIT NOT NULL DEFAULT 1")
	private Boolean estado = true;

}