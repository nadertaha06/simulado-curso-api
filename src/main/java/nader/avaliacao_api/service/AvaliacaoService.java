package nader.avaliacao_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import nader.avaliacao_api.dto.AvaliacaoRequestDTO;
import nader.avaliacao_api.exception.AvaliacaoNotFoundException;
import nader.avaliacao_api.model.Avaliacao;
import nader.avaliacao_api.observer.AvaliacaoObserver;
import nader.avaliacao_api.repository.AvaliacaoRepository;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final List<AvaliacaoObserver> observers;

    public AvaliacaoService(AvaliacaoRepository avaliacaoRepository, List<AvaliacaoObserver> observers) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.observers = observers;
    }

    public List<Avaliacao> listar() {
        return avaliacaoRepository.findAll();
    }
    public Avaliacao buscarPorId(Long id) {
        return avaliacaoRepository.findById(id)
                .orElseThrow(() -> new AvaliacaoNotFoundException(id));
    }

    public Avaliacao criar(AvaliacaoRequestDTO dto) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setAutor(dto.getAutor());
        avaliacao.setConteudo(dto.getConteudo());
        avaliacao.setNota(dto.getNota());
        avaliacao.setDataAvaliacao(LocalDateTime.now());
        Avaliacao salva = avaliacaoRepository.save(avaliacao);
        observers.forEach(observer -> observer.aoCriar(salva));
        return salva;
    }

    public void excluir(Long id) {
        Avaliacao avaliacao = buscarPorId(id);
        avaliacaoRepository.deleteById(id);
        observers.forEach(observer -> observer.aoExcluir(avaliacao));
    }
}
