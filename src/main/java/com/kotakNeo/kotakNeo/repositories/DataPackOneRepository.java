package com.kotakNeo.kotakNeo.repositories;

import com.kotakNeo.kotakNeo.entities.DataPackOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataPackOneRepository extends JpaRepository<DataPackOne, Long> {
}
