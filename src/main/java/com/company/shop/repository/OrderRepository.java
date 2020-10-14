package com.company.shop.repository;

import com.company.shop.entity.OrderPlacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderPlacement, Long> {

    @Query(value = "SELECT o FROM OrderPlacement o WHERE createdAt BETWEEN :fromDate AND :toDate")
    List<OrderPlacement> getByCreatedAtBetween(@Param("fromDate") LocalDateTime from, @Param("toDate") LocalDateTime to);

}
