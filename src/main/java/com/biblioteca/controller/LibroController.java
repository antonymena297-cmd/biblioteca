package com.biblioteca.controller;

import com.biblioteca.model.Libro;
import com.biblioteca.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/libros")
public class LibroController {
    
    @Autowired
    private LibroService libroService;
    
    @GetMapping
    public String listarLibros(Model model) {
        model.addAttribute("libros", libroService.listarTodos());
        return "libros/lista";
    }
    
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("libro", new Libro());
        return "libros/agregar";
    }
    
    @PostMapping("/guardar")
    public String guardarLibro(@ModelAttribute Libro libro, RedirectAttributes redirectAttributes) {
        libroService.guardar(libro);
        redirectAttributes.addFlashAttribute("success", "Libro guardado exitosamente");
        return "redirect:/libros";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        var libroOpt = libroService.buscarPorId(id);
        if (libroOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Libro no encontrado");
            return "redirect:/libros";
        }
        model.addAttribute("libro", libroOpt.get());
        return "libros/editar";
    }
    
    @PostMapping("/actualizar/{id}")
    public String actualizarLibro(@PathVariable Long id, @ModelAttribute Libro libro, RedirectAttributes redirectAttributes) {
        if (!libroService.existePorId(id)) {
            redirectAttributes.addFlashAttribute("error", "Libro no encontrado");
            return "redirect:/libros";
        }
        libro.setId(id);
        libroService.guardar(libro);
        redirectAttributes.addFlashAttribute("success", "Libro actualizado exitosamente");
        return "redirect:/libros";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarLibro(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (libroService.existePorId(id)) {
            libroService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Libro eliminado exitosamente");
        } else {
            redirectAttributes.addFlashAttribute("error", "Libro no encontrado");
        }
        return "redirect:/libros";
    }
    
    @GetMapping("/buscar")
    public String buscarLibros(@RequestParam(required = false) String titulo, 
                                @RequestParam(required = false) String autor,
                                Model model) {
        if (titulo != null && !titulo.isEmpty()) {
            model.addAttribute("libros", libroService.buscarPorTitulo(titulo));
            model.addAttribute("busqueda", "Título: " + titulo);
        } else if (autor != null && !autor.isEmpty()) {
            model.addAttribute("libros", libroService.buscarPorAutor(autor));
            model.addAttribute("busqueda", "Autor: " + autor);
        } else {
            model.addAttribute("libros", libroService.listarTodos());
        }
        return "libros/lista";
    }
}
