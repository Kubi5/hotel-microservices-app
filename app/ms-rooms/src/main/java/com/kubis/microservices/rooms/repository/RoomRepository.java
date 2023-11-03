package com.kubis.microservices.rooms.repository;

import com.kubis.microservices.rooms.model.RoomModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository <RoomModel, Long> {
    List<RoomModel> findAll();
}
