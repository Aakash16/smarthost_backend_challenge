package com.smarthost.booking.service;

import com.smarthost.booking.exception.InvalidBookingRequestException;
import com.smarthost.booking.model.Guest;
import com.smarthost.booking.repository.GuestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuestServiceImpl implements GuestService {

    private final GuestRepository guestRepository;

    public GuestServiceImpl(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @Override
    public Guest createGuest(Guest guest) {
        return guestRepository.save(guest);
    }

    @Override
    public List<Guest> getAllGuests() {
        return guestRepository.findAll();
    }

    @Override
    public Optional<Guest> getGuestById(Long id) {
        return guestRepository.findById(id);
    }

    @Override
    public Guest updateGuest(Long id, Guest guestDetails) {
        Optional<Guest> optionalGuest = guestRepository.findById(id);
        if (optionalGuest.isPresent()) {
            Guest guest = optionalGuest.get();
            guest.setName(guestDetails.getName());
            guest.setContact(guestDetails.getContact());
            guest.setMaxBudget(guestDetails.getMaxBudget());
            return guestRepository.save(guest);
        } else {
            throw new InvalidBookingRequestException("Guest not found with id: " + id);
        }
    }

    @Override
    public void deleteGuest(Long id) {
        if (guestRepository.existsById(id)) {
            guestRepository.deleteById(id);
        } else {
            throw new InvalidBookingRequestException("Guest not found with id: " + id);
        }
    }
}