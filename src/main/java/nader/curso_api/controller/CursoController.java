package nader.curso_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import nader.curso_api.dto.CursoRequestDTO;
import nader.curso_api.model.Curso;
import nader.curso_api.service.CursoService;

@RestController
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping("/cursos")
    public List<Curso> listar(@RequestParam(required = false) String nome) {
        return cursoService.listar(nome);
    }

    @PostMapping("/cursos")
    public ResponseEntity<Curso> criar(@Valid @RequestBody CursoRequestDTO dto) {
        Curso criado = cursoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }
}
