package nader.avaliacao_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import nader.avaliacao_api.dto.AvaliacaoRequestDTO;
import nader.avaliacao_api.model.Avaliacao;
import nader.avaliacao_api.service.AvaliacaoService;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {
    
    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @GetMapping
    public List<Avaliacao> listar() {
        return avaliacaoService.listar();
    }

    @GetMapping("/{id}")
    public Avaliacao buscarPorId(@PathVariable Long id) {
        return avaliacaoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Avaliacao> criar(@Valid @RequestBody AvaliacaoRequestDTO dto) {
        Avaliacao criada = avaliacaoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        avaliacaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
