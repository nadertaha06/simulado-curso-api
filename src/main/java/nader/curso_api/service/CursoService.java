package nader.curso_api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import nader.curso_api.dto.CursoRequestDTO;
import nader.curso_api.exception.CursoNotFoundException;
import nader.curso_api.model.Curso;
import nader.curso_api.repository.CursoRepository;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public List<Curso> listar(String nomeFiltro) {
        if (StringUtils.hasText(nomeFiltro)) {
            return cursoRepository.findByDeletadoFalseAndNomeStartingWith(nomeFiltro);
        }
        return cursoRepository.findByDeletadoFalse();
    }

    public Curso criar(CursoRequestDTO dto) {
        Curso curso = new Curso();
        curso.setNome(dto.getNome());
        curso.setDescricao(dto.getDescricao());
        curso.setCargaHoraria(dto.getCargaHoraria());
        return cursoRepository.save(curso);
    }

    public void deletar(Long id) {
        Curso curso = cursoRepository.findById(id)
                .filter(c -> !c.isDeletado())
                .orElseThrow(() -> new CursoNotFoundException(id));
        curso.setDeletado(true);
        cursoRepository.save(curso);
    }
}
