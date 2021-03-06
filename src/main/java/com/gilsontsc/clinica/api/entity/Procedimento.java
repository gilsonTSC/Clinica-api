package com.gilsontsc.clinica.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_procedimento")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Procedimento {

	@Id
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column(name = "medico_id")
	private Long medico;
	
	@Column(name = "consulta_id")
	private Long consulta;
	
	@Column(nullable = false)
	private String nome;
	
	private String descricao;
	
	@Column(nullable = false)
	private Double preco;
	
}