package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
    
    @Autowired
    private LibroRepository libroRepository;
    
    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }
    
    public Optional<Libro> buscarPorId(Long id) {
        return libroRepository.findById(id);
    }
    
    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }
    
    public void eliminar(Long id) {
        libroRepository.deleteById(id);
    }
    
    public List<Libro> buscarPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }
    
    public List<Libro> buscarPorAutor(String autor) {
        return libroRepository.findByAutorContainingIgnoreCase(autor);
    }
    
    public List<Libro> buscarDisponibles() {
        return libroRepository.findByDisponibleTrue();
    }
    
    public boolean existePorId(Long id) {
        return libroRepository.existsById(id);
    }
}
