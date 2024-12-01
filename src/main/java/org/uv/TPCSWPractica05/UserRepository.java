package org.uv.TPCSWPractica05;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Puedes agregar métodos personalizados si es necesario
    User findByUsername(String username);
}
