package com.holding.pestcontrol.repository;

import com.holding.pestcontrol.entity.Chemical;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChemicalRepository extends JpaRepository<Chemical, String> {
}
