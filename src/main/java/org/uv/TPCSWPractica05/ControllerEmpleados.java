package org.uv.TPCSWPractica05;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
@PreAuthorize("isAuthenticated()") 
public class ControllerEmpleados {

    @Autowired
    private RepositoryEmpleados repositoryEmpleados;

    @Autowired
    private RepositoryDepartamentos repositoryDepartamentos;

    @PostMapping
    public ResponseEntity<String> createEmpleado(@RequestBody Empleado empleado) {
        if (empleado.getDepto() != null) {
            // Verificar si el departamento especificado existe
            if (empleado.getDepto().getClave() > 0) {
                Optional<Departamentos> deptoOptional = repositoryDepartamentos.findById(empleado.getDepto().getClave());
                if (deptoOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El departamento especificado no existe");
                }
                // Asociamos el empleado al departamento
                empleado.setDepto(deptoOptional.get());
            } else {
                // Si no se ha especificado un departamento, lo creamos
                Departamentos nuevoDepto = repositoryDepartamentos.save(empleado.getDepto());
                empleado.setDepto(nuevoDepto);
            }
        }

        // Guardamos el empleado
        repositoryEmpleados.save(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body("Empleado creado con éxito");
    }
}
