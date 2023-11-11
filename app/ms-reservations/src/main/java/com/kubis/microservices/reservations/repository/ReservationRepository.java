package com.kubis.microservices.reservations.repository;

import com.kubis.microservices.reservations.model.ReservationModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends CrudRepository <ReservationModel, Long> {
    List<ReservationModel> findAll();
    List<ReservationModel> findByRoomId(long roomId);
}
