package com.gilsontsc.clinica.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gilsontsc.clinica.api.controller.exception.ObjectNotFoundException;
import com.gilsontsc.clinica.api.dto.ProcedimentoDTO;
import com.gilsontsc.clinica.api.entity.Procedimento;
import com.gilsontsc.clinica.api.services.ProcedimentoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Procedimento Endpoint", description = "Controler da entidade procedimento", tags = {"ProcedimentoEndpoint"})
@RestController
@RequestMapping(value="/api/procedimentos")
public class ProcedimentoController {

	@Autowired
	private ProcedimentoService procedimentoService;
	
	@ApiOperation(value = "Salvar o Procedimento")
	@PostMapping(produces = { "application/json", "application/xml", "application/x-yaml" }, 
	 			 consumes = { "application/json", "application/xml", "application/x-yaml" })
	@ResponseStatus(HttpStatus.CREATED)
	public ProcedimentoDTO salvar(@RequestBody @Valid ProcedimentoDTO procedimento) {
		Procedimento p = this.procedimentoService.convertDtoToEntity(procedimento);
		p = this.procedimentoService.salvar(p);
		return this.procedimentoService.convertEntityToDto(p);
	}
	
	@ApiOperation(value = "Atualizar o Procedimento")
	@PutMapping(value = "{id}", produces = { "application/json", "application/xml", "application/x-yaml" }, 
								consumes = { "application/json", "application/xml", "application/x-yaml" })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void atualizar(@PathVariable Long id, @RequestBody @Valid ProcedimentoDTO procedimentoAtualizado) {
		this.procedimentoService.buscarPorId(id)
			.map( procedimento -> {
				this.procedimentoService.salvar(this.procedimentoService.convertDtoToEntity(procedimentoAtualizado));
				return Void.TYPE;
			})
			.orElseThrow(() -> new ObjectNotFoundException(
					"Procedimento não encontrado! Id: " + id + ", Tipo: " + Procedimento.class.getName()));
	}
	
	@ApiOperation(value = "Lista todos os procedimentos")
	@GetMapping(produces = { "application/json", "application/xml", "application/x-yaml" })
	public List<ProcedimentoDTO> obterTodos(){
		List<ProcedimentoDTO> list = new ArrayList<>();
		this.procedimentoService.buscarTodos()
			.forEach(procedimento -> {
				list.add(this.procedimentoService.convertEntityToDto(procedimento));
			});
		return list;
	}
	
	@ApiOperation(value = "Lista todos os procedimentos Paginado")
	@GetMapping(value="/page", produces = { "application/json", "application/xml", "application/x-yaml" })
	public ResponseEntity<Page<ProcedimentoDTO>> obterTodos(@RequestParam(value="page", defaultValue = "0") int page,
														    @RequestParam(value="limit", defaultValue = "10") int limit,
														    @RequestParam(value="direction", defaultValue = "asc") String direction){
		Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
		Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "nome"));
		
		Page<ProcedimentoDTO> procedimentoDTO = this.procedimentoService
										.buscarTodos(pageable)
										.map(procedimento -> this.procedimentoService.convertEntityToDto(procedimento));
		return ResponseEntity.ok().body(procedimentoDTO);
	}
	
	@ApiOperation(value = "Buscar Procedimento por Id")
	@GetMapping(value = "/{id}", produces = { "application/json", "application/xml", "application/x-yaml" })
	public ProcedimentoDTO buscarPorId(@PathVariable Long id) {
		return this.procedimentoService.buscarPorId(id)
				   .map(procedimento -> {
					   return this.procedimentoService.convertEntityToDto(procedimento);
				   })
				   .orElseThrow(() -> new ObjectNotFoundException(
							"Procedimento não encontrado! Id: " + id + ", Tipo: " + Procedimento.class.getName()));
	}
	
	@ApiOperation(value = "Deletar o Procedimento")
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long id) {
		this.procedimentoService.buscarPorId(id)
			.map( procedimento -> {
				this.procedimentoService.deletar(procedimento.getId());
				return Void.TYPE;
			})
			.orElseThrow(() -> new ObjectNotFoundException(
					"Procedimento não encontrado! Id: " + id + ", Tipo: " + Procedimento.class.getName()));
	}
	
}