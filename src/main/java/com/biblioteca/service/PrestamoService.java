package com.biblioteca.service;

import com.biblioteca.model.Libro;
import com.biblioteca.model.Prestamo;
import com.biblioteca.model.Usuario;
import com.biblioteca.repository.PrestamoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrestamoService {
    
    @Autowired
    private PrestamoRepository prestamoRepository;
    
    @Autowired
    private LibroService libroService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    public List<Prestamo> listarTodos() {
        return prestamoRepository.findAll();
    }
    
    public List<Prestamo> listarPrestamosActivos() {
        return prestamoRepository.findByDevueltoFalse();
    }
    
    public Optional<Prestamo> buscarPorId(Long id) {
        return prestamoRepository.findById(id);
    }
    
    @Transactional
    public Prestamo realizarPrestamo(Long libroId, Long usuarioId) {
        Optional<Libro> libroOpt = libroService.buscarPorId(libroId);
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorId(usuarioId);
        
        if (libroOpt.isEmpty() || usuarioOpt.isEmpty()) {
            throw new RuntimeException("Libro o usuario no encontrado");
        }
        
        Libro libro = libroOpt.get();
        if (!libro.getDisponible()) {
            throw new RuntimeException("El libro no está disponible para préstamo");
        }
        
        Usuario usuario = usuarioOpt.get();
        
        Prestamo prestamo = new Prestamo(libro, usuario, LocalDate.now());
        libro.setDisponible(false);
        libroService.guardar(libro);
        
        return prestamoRepository.save(prestamo);
    }
    
    @Transactional
    public Prestamo registrarDevolucion(Long prestamoId) {
        Optional<Prestamo> prestamoOpt = prestamoRepository.findById(prestamoId);
        
        if (prestamoOpt.isEmpty()) {
            throw new RuntimeException("Préstamo no encontrado");
        }
        
        Prestamo prestamo = prestamoOpt.get();
        if (prestamo.getDevuelto()) {
            throw new RuntimeException("El libro ya ha sido devuelto");
        }
        
        prestamo.setDevuelto(true);
        prestamo.setFechaDevolucion(LocalDate.now());
        
        Libro libro = prestamo.getLibro();
        libro.setDisponible(true);
        libroService.guardar(libro);
        
        return prestamoRepository.save(prestamo);
    }
    
    public List<Prestamo> buscarPorUsuario(Long usuarioId) {
        return prestamoRepository.findByUsuarioId(usuarioId);
    }
    
    public List<Prestamo> buscarPorLibro(Long libroId) {
        return prestamoRepository.findByLibroId(libroId);
    }
}
