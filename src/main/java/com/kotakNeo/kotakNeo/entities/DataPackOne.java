package com.kotakNeo.kotakNeo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.sql.Date;
import java.sql.Time;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataPackOne {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String stockName;
    private Date createdDate;
    private Time createdTime;
    @NonNull
    private String totalBuyQuantity;
    @NonNull
    private String totalSellQuantity;
    @NonNull
    private double differences;
    @NonNull
    private String price;

}
