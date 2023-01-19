package com.woowahan.recipe.repository;

import com.woowahan.recipe.domain.entity.OrderEntity;
import com.woowahan.recipe.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findMyOrderByUser(UserEntity user, Pageable pageable);
}