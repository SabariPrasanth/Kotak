package com.kotakNeo.kotakNeo.repositories;

import com.kotakNeo.kotakNeo.entities.DataPackOne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;


@Repository
public interface DataPackOneRepository extends JpaRepository<DataPackOne, Long> {
    @Query("SELECT AVG(d.differences) FROM DataPackOne d WHERE d.stockName = :stockName AND d.createdDate = :date")
    public Float findBycreatedDateAvg(@Param("stockName") String stockName, @Param("date") Date date);
}
