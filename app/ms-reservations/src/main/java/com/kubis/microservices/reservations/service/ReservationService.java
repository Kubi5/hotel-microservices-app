package com.kubis.microservices.reservations.service;

import com.kubis.microservices.reservations.clients.CustomerClient;
import com.kubis.microservices.reservations.clients.RoomClient;
import com.kubis.microservices.reservations.model.ReservationModel;
import com.kubis.microservices.reservations.repository.ReservationRepository;
import com.kubis.microservices.reservations.request.ReservationRequest;
import com.kubis.microservices.reservations.response.ReservationResponse;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomerClient customerClient;
    private final RoomClient roomCLient;
    private LocalDate startOfReservation;
    private LocalDate endOfReservation;
    @Value("${jwt.secret}")
    private String jwtSecret;

    public ReservationResponse addReservation(String token, ReservationRequest request){

        startOfReservation = dateFormationAndParsing(request.getReservationStartDate());
        endOfReservation = dateFormationAndParsing(request.getReservationEndDate());

        if(reservationDateIsNotTaken(request.getRoomId())) {
            long reservationId = reservationRepository.save(reservationDataPreparation(token, request)).getId();

            return ReservationResponse.builder()
                    .id(reservationId)
                    .customerId(customerClient.getCustomerByEmail(getEmailFromToken(token)).getId())
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

    private ReservationModel reservationDataPreparation(String token, ReservationRequest request){
        return ReservationModel.builder()
                .customerId(customerClient.getCustomerByEmail(getEmailFromToken(token)).getId())
                .roomId(request.getRoomId())
                .reservationStartDate(startOfReservation)
                .reservationEndDate(endOfReservation)
                .reservationConfirmedDate(new Date())
                .numberOfPeople(request.getNumberOfPeople())
                .priceOfReservation(calculatePriceOfReservation(request.getRoomId(), startOfReservation, endOfReservation))
                .build();
    }

    private boolean reservationDateIsNotTaken(long roomId){
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

    private double calculatePriceOfReservation(Long roomId, LocalDate reservationStart, LocalDate reservationEnd){
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
