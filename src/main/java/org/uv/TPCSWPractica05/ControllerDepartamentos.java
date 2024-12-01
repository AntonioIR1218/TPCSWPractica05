package org.uv.TPCSWPractica05;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/departamentos")
@PreAuthorize("isAuthenticated()")
public class ControllerDepartamentos {

    @Autowired
    private RepositoryDepartamentos repositoryDepartamentos;

    @GetMapping
    public ResponseEntity<List<Departamentos>> list() {
        List<Departamentos> departamentos = repositoryDepartamentos.findAll();
        return ResponseEntity.ok(departamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departamentos> get(@PathVariable Long id) {
        Optional<Departamentos> res = repositoryDepartamentos.findById(id);
        if (res.isPresent()) {
            return ResponseEntity.ok(res.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Departamentos> post(@RequestBody Departamentos input) {
        Departamentos newDepartamento = repositoryDepartamentos.save(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDepartamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable Long id, @RequestBody Departamentos input) {
        Optional<Departamentos> existingDepartamento = repositoryDepartamentos.findById(id);
        if (existingDepartamento.isPresent()) {
            Departamentos updatedDepartamento = existingDepartamento.get();
            updatedDepartamento.setNombre(input.getNombre());
            repositoryDepartamentos.save(updatedDepartamento);
            return ResponseEntity.ok(updatedDepartamento);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Departamento no encontrado");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Departamentos> existingDepartamento = repositoryDepartamentos.findById(id);
        if (existingDepartamento.isPresent()) {
            repositoryDepartamentos.delete(existingDepartamento.get());
            return ResponseEntity.ok("Departamento borrado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Departamento no encontrado");
        }
    }
}
