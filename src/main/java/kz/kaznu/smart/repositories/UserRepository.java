package kz.kaznu.smart.repositories;

import kz.kaznu.smart.models.entities.Role;
import kz.kaznu.smart.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.email = ?1")
    Optional<User> getUserByEmail(String email);

    List<User> findAllByRolesContaining(Role role);

    List<User> findAllByRolesNotContaining(Role role);
}
