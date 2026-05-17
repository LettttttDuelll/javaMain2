package com.example.laptopshop.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.laptopshop.entity.Laptop;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface LaptopRepository extends JpaRepository<Laptop,Long> {
    List<Laptop> findAllByDeletedTrueAndRestoredFalseAndDeletedAtBefore(LocalDateTime time);
    List<Laptop> findAllByDeletedTrue();
    List<Laptop> findByDeletedFalse();
    @Query("SELECT l.model, SUM(l.stock) FROM Laptop l WHERE l.deleted = false GROUP BY l.model")
    List<Object[]> countByModel();
    @Query("SELECT l.discount, SUM(l.stock) FROM Laptop l WHERE l.deleted = false GROUP BY l.discount ORDER BY l.discount ASC")
    List<Object[]> stockByDiscount();
    Optional<Laptop> findById(Integer id);

    @Query("""
    SELECT l FROM Laptop l
    WHERE l.deleted = false AND l.current_price > 0
    AND (lower(l.name) LIKE lower(concat('%', :keyword, '%'))
        OR lower(l.model) LIKE lower(concat('%', :keyword, '%'))
        OR lower(l.category.name) LIKE lower(concat('%', :keyword, '%')))
    """)
    List<Laptop> searchByKeyword(@Param("keyword") String keyword);

    List<Laptop> findTop5ByStockLessThanAndDeletedFalse(Integer stock); 

    @Query("""
SELECT l FROM Laptop l
WHERE l.deleted = false
AND (
    :keyword IS NULL
    OR lower(l.name) LIKE lower(concat('%', :keyword, '%'))
)
AND (
    :brand IS NULL
    OR lower(l.model) = lower(:brand)
)
AND (
    :minPrice IS NULL
    OR l.current_price >= :minPrice
)
AND (
    :maxPrice IS NULL
    OR l.current_price <= :maxPrice
)
""")
List<Laptop> searchAndFilter(
        @Param("keyword") String keyword,
        @Param("brand") String brand,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice
);


}