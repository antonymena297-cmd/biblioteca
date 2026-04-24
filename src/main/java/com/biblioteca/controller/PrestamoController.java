package com.biblioteca.controller;

import com.biblioteca.service.LibroService;
import com.biblioteca.service.PrestamoService;
import com.biblioteca.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {
    
    @Autowired
    private PrestamoService prestamoService;
    
    @Autowired
    private LibroService libroService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping
    public String listarPrestamos(Model model) {
        model.addAttribute("prestamos", prestamoService.listarTodos());
        return "prestamos/lista";
    }
    
    @GetMapping("/activos")
    public String listarPrestamosActivos(Model model) {
        model.addAttribute("prestamos", prestamoService.listarPrestamosActivos());
        model.addAttribute("titulo", "Préstamos Activos");
        return "prestamos/lista";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("libros", libroService.buscarDisponibles());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "prestamos/nuevo";
    }
    
    @PostMapping("/realizar")
    public String realizarPrestamo(@RequestParam Long libroId, 
                                    @RequestParam Long usuarioId,
                                    RedirectAttributes redirectAttributes) {
        try {
            prestamoService.realizarPrestamo(libroId, usuarioId);
            redirectAttributes.addFlashAttribute("success", "Préstamo realizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/prestamos";
    }
    
    @GetMapping("/devolver/{id}")
    public String registrarDevolucion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            prestamoService.registrarDevolucion(id);
            redirectAttributes.addFlashAttribute("success", "Devolución registrada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/prestamos";
    }
}
