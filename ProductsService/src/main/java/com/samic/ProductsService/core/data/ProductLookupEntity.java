package com.samic.ProductsService.core.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products-lookup")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductLookupEntity {
    @Id
    @Column(unique = true)
    private String productId;
    @Column(unique = true)
    private String title;
}
