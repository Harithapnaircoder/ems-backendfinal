package ems.example.ems.controllers;


import ems.example.ems.models.Feedback;
import ems.example.ems.repositories.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
@RestController
@RequestMapping("/feedbacks")
@CrossOrigin(origins = "http://localhost:3000")
public class FeedbackController {
    @Autowired
    private FeedbackRepository feedbackRepository;
    private final ObjectMapper objectMapper;

    // Inject ObjectMapper into your controller's constructor
    public FeedbackController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Endpoint to retrieve all feedbacks
    @GetMapping("/list")
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @GetMapping("/feedback_emp")
    public ResponseEntity<List<Object[]>> getFeedbackWithEmployee() {
        List<Object[]> feedbackWithEmployee = feedbackRepository.findFeedbackWithEmployeeName();
        return ResponseEntity.ok(feedbackWithEmployee);
    }

//    @GetMapping("/listreply/{empId}")
//    public ResponseEntity<List<Object[]>> getFeedbackReply(@PathVariable Long empId) {
//        List<Object[]> feedbackWithEmployee = feedbackRepository.findFeedbackWithReply(empId);
//        return ResponseEntity.ok(feedbackWithEmployee);
//    }

    /*@PostMapping("/save")
    public ResponseEntity<String> createFeedback(@RequestBody Feedback feedback) {
        try {

            Long id = feedback.getId();
            String feedbackText = feedback.getFeedback();

            return new ResponseEntity<>("Feedback added successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add feedback", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    @PostMapping("/save")
    public ResponseEntity<String> saveFeedback(@RequestBody Feedback feedback) {
        try {
            // Save the feedback
            feedbackRepository.save(feedback);
            return new ResponseEntity<>("Feedback saved successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to save feedback", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @GetMapping("/list_reply_null")
//    public ResponseEntity<List<Object[]>> getFeedbackWithEmployeeAndNullReply() {
//        List<Object[]> feedbackWithEmployeeAndNullReply = feedbackRepository.findFeedbackWithEmployeeNameAndNullReply();
//        return ResponseEntity.ok(feedbackWithEmployeeAndNullReply);
//    }



    // Endpoint to retrieve a feedback by ID
//    @GetMapping("/list/{id}")
//    public ResponseEntity<Feedback> getFeedbackById(@PathVariable int id) {
//        Feedback feedback = feedbackRepository.findById(id).orElse(null);
//        if (feedback == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(feedback, HttpStatus.OK);
//    }


    //List details of application by empid
    @GetMapping("list/{empId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByEmployeeId(@PathVariable Long empId) {
        List<Feedback> feedbacks = feedbackRepository.findByEmpId(empId);
        if (feedbacks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }





    // Endpoint to create a new feedback
//    @PostMapping("/save")
//    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
//        try {
//            Feedback savedFeedback = feedbackRepository.save(feedback);
//            return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }




//
//    // Endpoint to update an existing feedback
//    @PutMapping("/update/{id}")
//    public ResponseEntity<Feedback> updateFeedback(@PathVariable int id, @RequestBody Feedback feedback) {
//        Feedback existingFeedback = feedbackRepository.findById(id).orElse(null);
//        if (existingFeedback == null) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        existingFeedback.setFeedback(feedback.getFeedback());
//        //existingFeedback.setFeedbackDate(feedback.getFeedbackDate());
//        existingFeedback.setReply(feedback.getReply());
//        //existingFeedback.setReplyDate(feedback.getReplyDate());
//        existingFeedback.setEmpId(feedback.getEmpId());
//
//        Feedback updatedFeedback = feedbackRepository.save(existingFeedback);
//        return new ResponseEntity<>(updatedFeedback, HttpStatus.OK);
//    }
//
//    // Endpoint to delete a feedback by ID
//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<HttpStatus> deleteFeedback(@PathVariable int id) {
//        try {
//            feedbackRepository.deleteById(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//
    /*@PutMapping("/{id}/reply")
    public ResponseEntity<String> replyToFeedback(@PathVariable Long id, @RequestBody String reply) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
        if (optionalFeedback.isPresent()) {
            Feedback feedback = optionalFeedback.get();
            feedback.setReply(reply);
            // Set reply date if needed: feedback.setReplyDate(new Date());
            feedbackRepository.save(feedback);
            return ResponseEntity.ok("Reply sent successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback not found");
        }
    }*/

    @PutMapping("/{id}/reply")
    public ResponseEntity<String> replyToFeedback(@PathVariable Long id, @RequestBody String reply) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
        if (optionalFeedback.isPresent()) {
            Feedback feedback = optionalFeedback.get();
            feedback.setReply(reply);
            feedbackRepository.save(feedback);
            return ResponseEntity.ok("Reply sent successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback not found");
        }
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<String> replyFeedback(@PathVariable Long id, @RequestBody(required = false) String reply) {
        if (reply != null) {
            // This indicates a reply to existing feedback
            Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
            if (optionalFeedback.isPresent()) {
                Feedback feedback = optionalFeedback.get();
                feedback.setReply(reply); // Store the reply as plain text
                feedbackRepository.save(feedback);
                return ResponseEntity.ok("Reply sent successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback not found");
            }
        } else {
            // No reply parameter indicates a new feedback request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Reply parameter is missing");
        }
    }


    @PostMapping("/{feedbackId}/reply1")
    public ResponseEntity<String> sendReplyToFeedback(@PathVariable Long feedbackId, @RequestBody Map<String, String> replyMap) {
        // Check if the feedback with the given ID exists
        if (!feedbackRepository.existsById(feedbackId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback not found");
        }

        try {
            // Convert the replyMap to a JSON string
            String replyJson = objectMapper.writeValueAsString(replyMap);

            // Update the reply field using the converted JSON string
            feedbackRepository.updateReply(feedbackId, replyJson);

            return ResponseEntity.ok("Reply sent successfully");
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send reply");
        }
    }


}