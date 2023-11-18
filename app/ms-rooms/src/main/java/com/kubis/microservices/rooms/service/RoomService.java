package com.kubis.microservices.rooms.service;

import com.kubis.microservices.rooms.clients.CustomerClient;
import com.kubis.microservices.rooms.model.RoomModel;
import com.kubis.microservices.rooms.repository.RoomRepository;
import com.kubis.microservices.rooms.request.RoomRequest;
import com.kubis.microservices.rooms.response.CustomerResponse;
import feign.FeignException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final CustomerClient customerClient;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public Long addRoom(RoomRequest request){
        RoomModel roomModel = RoomModel.builder()
                .maxPeopleNumber(request.getMaxPeopleNumber())
                .price(request.getPrice())
                .availableBeds(request.getAvailableBeds())
                .isBathroomPrivate(request.getIsBathroomPrivate())
                .additionalAmenities(request.getAdditionalAmenities())
                .build();
        return roomRepository.save(roomModel).getRoomId();
    }

    public List<RoomModel> getRooms(){
        return roomRepository.findAll();
    }

    public Optional<RoomModel> getRoomById(long id){
        return roomRepository.findById(id);
    }

    public void deleteRoom(long id){
        roomRepository.deleteById(id);
    }

    public Optional<RoomModel> updateRoom(long id, RoomRequest request){
        Optional<RoomModel> roomToUpdate = roomRepository.findById(id);

        if(roomToUpdate.isPresent()){
            if(request.getMaxPeopleNumber() != 0){
                roomToUpdate.get().setMaxPeopleNumber(request.getMaxPeopleNumber());
            }
            if(request.getPrice() != 0.0){
                roomToUpdate.get().setPrice(request.getPrice());
            }
            if(request.getAvailableBeds() != null){
                roomToUpdate.get().setAvailableBeds(request.getAvailableBeds());
            }
            if(request.getIsBathroomPrivate() != null) {
                roomToUpdate.get().setIsBathroomPrivate(request.getIsBathroomPrivate());
            }
            if(request.getAdditionalAmenities() != null){
                roomToUpdate.get().setAdditionalAmenities(request.getAdditionalAmenities());
            }

            roomRepository.save(roomToUpdate.get());
        }
        return roomToUpdate;
    }

    public boolean senderIsAdmin(String token) {
        String email = getEmailFromToken(token);

        if (email != null) {
            try {
                CustomerResponse customer = customerClient.getCustomerByEmail(email, token);

                return "admin".equals(customer.getRole());
            } catch (FeignException.Forbidden e) {
                return false;
            }
        }

        return false;
    }


    private String getEmailFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            System.out.println(e.getMessage() + " => " + e);
        }
        return null;
    }
}
