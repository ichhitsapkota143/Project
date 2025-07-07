package controller;

import model.Event;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import repository.EventRepository;
import java.time.LocalDate;


import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepo;

    public EventController(EventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAll() {
        return ResponseEntity.ok(eventRepo.findAll());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> create(@RequestBody Event event) {
        return ResponseEntity.ok(eventRepo.save(event));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Event> update(@PathVariable Long id, @RequestBody Event event) {
        return eventRepo.findById(id)
                .map(existing -> {
                    existing.setTitle(event.getTitle());
                    existing.setDescription(event.getDescription());
                    existing.setEventDate(event.getEventDate());
                    eventRepo.save(existing);
                    return ResponseEntity.ok(existing);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/range")
    public ResponseEntity<List<Event>> getEventsByDateRange(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        // Parse dates
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<Event> events = eventRepo.findByEventDateBetween(startDate, endDate);
        return ResponseEntity.ok(events);
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!eventRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
