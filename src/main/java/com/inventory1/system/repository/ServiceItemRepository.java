package com.inventory1.system.repository;

import com.inventory1.system.entity.ServiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceItemRepository extends JpaRepository<ServiceItem, Long> {
    // We don't need any custom searches here yet, the basic save(), findAll(), and findById()
    // provided by JpaRepository are enough for the repair services.
}