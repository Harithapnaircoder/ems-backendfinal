package ems.example.ems.controllers;

import ems.example.ems.models.Events;
import ems.example.ems.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Event")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {

@Autowired
    private EventRepository eventRepository;
@GetMapping("/eventlist")
public List<Events> getAllEvent(){
    return eventRepository.findAll();
}
@PostMapping("/eventsave")
    public String save(@RequestBody Events events){
    eventRepository.save(events);
    return "Event successfull";
}


}
