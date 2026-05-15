package com.example.laptopshop.repository;

import com.example.laptopshop.dto.RevenueDTO;
import com.example.laptopshop.dto.TopProductDTO;
import com.example.laptopshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser_Id(Integer userId);
    Order findTopByOrderByIdDesc(); // ✅ thêm dòng này
    @Query("SELECT o FROM Order o JOIN FETCH o.user")
    List<Order> findAllWithUser(); // ✅ thêm dòng này

    @Query("""
        SELECT new com.example.laptopshop.dto.RevenueDTO(
            COALESCE(SUM(o.totalPrice),0),
            COUNT(o),
            COALESCE(SUM(oi.quantity),0)
        )
        FROM Order o
        LEFT JOIN o.items oi
        WHERE o.status = 'DELIVERED'
        AND o.orderDate BETWEEN :startDate AND :endDate
    """)
    RevenueDTO getRevenueStatistics(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("""
    SELECT new com.example.laptopshop.dto.TopProductDTO(
        oi.laptop.name,
        SUM(oi.quantity)
    )
    FROM OrderItem oi
    WHERE oi.order.status = 'DELIVERED'
    GROUP BY oi.laptop.name
    ORDER BY SUM(oi.quantity) DESC
""")
List<TopProductDTO> getTopProducts();

@Query("""
    SELECT DATE(o.orderDate), SUM(o.totalPrice)
    FROM Order o
    WHERE o.status = 'DELIVERED'
    AND o.orderDate BETWEEN :startDate AND :endDate
    GROUP BY DATE(o.orderDate)
    ORDER BY DATE(o.orderDate)
""")
List<Object[]> getRevenueByDate(
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
);

}

