package kz.kaznu.smart.repositories;

import kz.kaznu.smart.models.entities.Item;
import kz.kaznu.smart.models.entities.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ParamRepository extends JpaRepository<Param, Long> {

}
