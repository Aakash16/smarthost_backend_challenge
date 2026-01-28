package com.smarthost.booking.repository;

import com.smarthost.booking.model.OccupancyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OccupancyRepository extends JpaRepository<OccupancyRequest, Long> {
}