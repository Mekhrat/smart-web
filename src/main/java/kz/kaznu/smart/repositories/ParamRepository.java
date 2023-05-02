package kz.kaznu.smart.repositories;

import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.Param;
import kz.kaznu.smart.models.enums.Params;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface ParamRepository extends JpaRepository<Param, Long> {

    Optional<Param> getFirstByName(Params name);
}
