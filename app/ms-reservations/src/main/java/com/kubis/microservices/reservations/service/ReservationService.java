package com.kubis.microservices.reservations.service;

import com.kubis.microservices.reservations.clients.CustomerClient;
import com.kubis.microservices.reservations.clients.RoomClient;
import com.kubis.microservices.reservations.model.ReservationModel;
import com.kubis.microservices.reservations.repository.ReservationRepository;
import com.kubis.microservices.reservations.request.ReservationRequest;
import com.kubis.microservices.reservations.request.SearchRequest;
import com.kubis.microservices.reservations.response.ReservationResponse;
import com.kubis.microservices.reservations.response.RoomResponse;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerClient customerClient;
    private final RoomClient roomCLient;
    @Value("${jwt.secret}")
    private String jwtSecret;

    public ReservationResponse addReservation(String token, ReservationRequest request){

        LocalDate startOfReservation = dateFormationAndParsing(request.getReservationStartDate());
        LocalDate endOfReservation = dateFormationAndParsing(request.getReservationEndDate());

        if(reservationDateIsNotTaken(request.getRoomId(), startOfReservation, endOfReservation)) {
            long reservationId = reservationRepository.save(reservationDataPreparation(token, request)).getReservationId();

            return ReservationResponse.builder()
                    .reservationId(reservationId)
                    .customerId(customerClient.getCustomerByEmail(getEmailFromToken(token)).getCustomerId())
                    .roomId(request.getRoomId())
                    .reservationStartDate(startOfReservation)
                    .reservationEndDate(endOfReservation)
                    .reservationConfirmedDate(new Date())
                    .numberOfPeople(request.getNumberOfPeople())
                    .priceOfReservation(calculatePriceOfReservation(request.getRoomId(), startOfReservation, endOfReservation))
                    .build();
        }
        return null;
    }
    public List<ReservationModel> getReservations(){
        return reservationRepository.findAll();
    }

    public Optional<ReservationModel> getReservationById(long id){
        return reservationRepository.findById(id);
    }

    public List<RoomResponse> searchForRoomsWithCriteria(SearchRequest request){

        LocalDate startOfReservation = dateFormationAndParsing(request.getReservationStartDate());
        LocalDate endOfReservation = dateFormationAndParsing(request.getReservationEndDate());

        List<RoomResponse> rooms = roomCLient.getRooms();

        List<RoomResponse> roomsWhichMeetRoomCriteria = filterRoomsByRoomsProperties(request, rooms);

        return filterRoomsWithNoDateConflict(roomsWhichMeetRoomCriteria,startOfReservation,endOfReservation);

    }

    private List<RoomResponse> filterRoomsWithNoDateConflict(List<RoomResponse> rooms, LocalDate startOfReservation, LocalDate endOfReservation){
        return rooms.stream().filter(x -> {
            List<ReservationModel> reservationModels = reservationRepository.findByRoomId(x.getRoomId());

            return reservationModels.stream()
                    .allMatch(reservation ->
                            (endOfReservation.isBefore(reservation.getReservationStartDate()) ||
                                    endOfReservation.isEqual(reservation.getReservationStartDate()))
                                    ||
                                    (startOfReservation.isAfter(reservation.getReservationEndDate())
                                            || startOfReservation.isEqual(reservation.getReservationEndDate()))
                    );
        }).collect(Collectors.toList());
    }

    private List<RoomResponse> filterRoomsByRoomsProperties(SearchRequest request, List<RoomResponse> rooms){
        return rooms.stream()
                .filter(x -> x.getMaxPeopleNumber() >= request.getNumberOfPeople()
                        && x.getPrice() >= request.getMinPossiblePricePerDay()
                        && x.getPrice() <= request.getMaxPossiblePricePerDay()
                        && x.getIsBathroomPrivate() == request.getIsBathroomPrivate()
                        && new HashSet<>(x.getAvailableBeds()).containsAll(request.getSelectedBeds())
                        && new HashSet<>(x.getAdditionalAmenities()).containsAll(request.getSelectedAdditionalAmenities())).toList();
    }

    private ReservationModel reservationDataPreparation(String token, ReservationRequest request){

        LocalDate startOfReservation = dateFormationAndParsing(request.getReservationStartDate());
        LocalDate endOfReservation = dateFormationAndParsing(request.getReservationEndDate());

        return ReservationModel.builder()
                .customerId(customerClient.getCustomerByEmail(getEmailFromToken(token)).getCustomerId())
                .roomId(request.getRoomId())
                .reservationStartDate(startOfReservation)
                .reservationEndDate(endOfReservation)
                .reservationConfirmedDate(new Date())
                .numberOfPeople(request.getNumberOfPeople())
                .priceOfReservation(calculatePriceOfReservation(request.getRoomId(), startOfReservation, endOfReservation))
                .build();
    }

    private boolean reservationDateIsNotTaken(long roomId, LocalDate startOfReservation, LocalDate endOfReservation){
        List<ReservationModel> currentReservations = reservationRepository.findByRoomId(roomId);

        long numberOfCurrentReservationOnChosenDate = currentReservations.stream()
                .filter(x -> {
                    return (startOfReservation.isAfter(x.getReservationStartDate()) || startOfReservation.isEqual(x.getReservationStartDate())) && startOfReservation.isBefore(x.getReservationEndDate())
                            || endOfReservation.isAfter(x.getReservationStartDate()) && (endOfReservation.isBefore(x.getReservationEndDate()) || endOfReservation.isEqual(x.getReservationEndDate()))
                            || (startOfReservation.isBefore(x.getReservationStartDate()) || startOfReservation.isEqual(x.getReservationStartDate())) && (endOfReservation.isAfter(x.getReservationEndDate()) || endOfReservation.isEqual(x.getReservationEndDate()));
                })
                .count();

        return numberOfCurrentReservationOnChosenDate == 0;
    }

    private LocalDate dateFormationAndParsing(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return LocalDate.parse(date, formatter);
    }

    private double calculatePriceOfReservation(long roomId, LocalDate reservationStart, LocalDate reservationEnd){
        long numberOfDaysSpent = ChronoUnit.DAYS.between(reservationStart, reservationEnd);

        return roomCLient.getRoomById(roomId).getPrice() * (int)numberOfDaysSpent;
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
