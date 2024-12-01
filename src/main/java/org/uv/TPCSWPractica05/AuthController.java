package org.uv.TPCSWPractica05;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // Ruta para registrar un usuario
    @PostMapping("/auth/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            // Verificar si el nombre de usuario ya existe
            if (userRepository.findByUsername(user.getUsername()) != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El usuario ya existe");
            }

            // Guardar al usuario en la base de datos
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuario registrado con éxito");

        } catch (Exception e) {
            // Capturar cualquier excepción que ocurra y devolver un error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar usuario: " + e.getMessage());
        }
    }

    // Ruta para hacer login
    @PostMapping("/auth/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        try {
            // Buscar el usuario por nombre de usuario
            User foundUser = userRepository.findByUsername(user.getUsername());

            if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
                // Generar el token JWT
                String token = jwtUtil.generateToken(user.getUsername());
                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Credenciales incorrectas");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al iniciar sesión: " + e.getMessage());
        }
    }
}
