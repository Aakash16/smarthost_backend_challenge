package com.smarthost.booking.service;

import com.smarthost.booking.model.Guest;
import java.util.List;
import java.util.Optional;

public interface GuestService {
    Guest createGuest(Guest guest);
    List<Guest> createGuests(List<Guest> guests);
    List<Guest> getAllGuests();
    Optional<Guest> getGuestById(Long id);
    Guest updateGuest(Long id, Guest guestDetails);
    void deleteGuest(Long id);
}