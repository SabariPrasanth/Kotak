package com.kotakNeo.kotakNeo.repositories;

import com.kotakNeo.kotakNeo.entities.DataPackOne;
import com.kotakNeo.kotakNeo.entities.GapWithPredictionAndActualPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GapWithPredictionAndActualPriceRepository
        extends JpaRepository<GapWithPredictionAndActualPrice, Long> {
}
