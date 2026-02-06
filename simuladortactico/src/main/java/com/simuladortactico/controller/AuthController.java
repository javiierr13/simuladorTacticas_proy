package com.simuladortactico.controller;

import com.simuladortactico.model.Entrenador;
import com.simuladortactico.service.AuthService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody RegistroRequest request) {
        try {
            Entrenador entrenador = new Entrenador();
            entrenador.setNombre(request.getNombre());
            entrenador.setCorreo(request.getCorreo());
            entrenador.setContrasena(request.getContrasena());
            
            Entrenador nuevoEntrenador = authService.registrar(entrenador);
            return ResponseEntity.ok(new AuthResponse(nuevoEntrenador.getId(), nuevoEntrenador.getNombre(), nuevoEntrenador.getCorreo()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Entrenador> entrenador = authService.login(request.getCorreo(), request.getContrasena());
        if (entrenador.isPresent()) {
            Entrenador e = entrenador.get();
            return ResponseEntity.ok(new AuthResponse(e.getId(), e.getNombre(), e.getCorreo()));
        }
        return ResponseEntity.status(401).body(new ErrorResponse("Credenciales inválidas"));
    }

    @Data
    static class RegistroRequest {
        private String nombre;
        private String correo;
        private String contrasena;
    }

    @Data
    static class LoginRequest {
        private String correo;
        private String contrasena;
    }

    @Data
    static class AuthResponse {
        private Long id;
        private String nombre;
        private String correo;

        public AuthResponse(Long id, String nombre, String correo) {
            this.id = id;
            this.nombre = nombre;
            this.correo = correo;
        }
    }

    @Data
    static class ErrorResponse {
        private String mensaje;

        public ErrorResponse(String mensaje) {
            this.mensaje = mensaje;
        }
    }
}
