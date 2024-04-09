package ems.example.ems.controllers;

import ems.example.ems.models.Employee;
import ems.example.ems.models.Leave;
import ems.example.ems.models.LeaveDTO;
import ems.example.ems.repositories.EmployeeRepository;
import ems.example.ems.repositories.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/leave")
public class LeaveController {
    private final LeaveRepository leaveRepository;
    private EmployeeRepository employeeRepository;


    @Autowired
    public LeaveController(EmployeeRepository employeeRepository, LeaveRepository leaveRepository) {
        this.employeeRepository = employeeRepository;
        this.leaveRepository = leaveRepository;
    }

    /*---------------------------------Admin----------------------------------------------*/

    @GetMapping("/list")
    public ResponseEntity<List<Object[]>> getAllLeaveRecords() {
        List<Object[]> allLeave = leaveRepository.getAllLeaveRecords();
        return ResponseEntity.ok(allLeave);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<Object[]>> getPendingLeave() {
        List<Object[]> pendingLeave = leaveRepository.findPendingLeaveWithEmployeeName();
        return ResponseEntity.ok(pendingLeave);
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approveLeave(@PathVariable Long id) {
        try {
            Optional<Leave> optionalLeave = leaveRepository.findById(id);
            if (optionalLeave.isPresent()) {
                Leave leave = optionalLeave.get();
                leave.setApprovedStatus(true);

                // Fetch the corresponding employee
                Optional<Employee> optionalEmployee = employeeRepository.findById(leave.getEmpId());
                if (optionalEmployee.isPresent()) {
                    Employee employee = optionalEmployee.get();
                    int leaveQuota = employee.getLeavequota();
                    // Check if leave quota is zero
                    if (leaveQuota == 0) {
                        return new ResponseEntity<>("Can't approve, leave quota is zero", HttpStatus.BAD_REQUEST);
                    }
                    // Update leave quota
                    employee.setLeavequota(leaveQuota - 1);
                    employeeRepository.save(employee);
                } else {
                    return new ResponseEntity<>("Employee not found.", HttpStatus.NOT_FOUND);
                }

                leaveRepository.save(leave);
                return new ResponseEntity<>("Leave approved successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Leave not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to approve leave: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("/approve/{id}")
//    public ResponseEntity<String> approveLeave(@PathVariable Long id) {
//        try {
//            Optional<Leave> optionalLeave = leaveRepository.findById(id);
//            if (optionalLeave.isPresent()) {
//                Leave leave = optionalLeave.get();
//                leave.setApprovedStatus(true);
//
//                leaveRepository.save(leave);
//                return new ResponseEntity<>("Leave approved successfully", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("Leave not found.", HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>("Failed to approve leave: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<String> rejectLeave(@PathVariable Long id) {
        try {
            Optional<Leave> optionalLeave = leaveRepository.findById(id);
            if (optionalLeave.isPresent()) {
                Leave leave = optionalLeave.get();
                leave.setApprovedStatus(false);

                leaveRepository.save(leave);
                return new ResponseEntity<>("Leave rejected successfully.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Leave not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to approve leave: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /*----------------------------------------------Employee---------------------------------------------------------*/
    @PostMapping("/save")
    public ResponseEntity<String> saveLeave(@RequestBody Leave leave) {
        if (leave.getLeaveType() == null || leave.getReason() == null || leave.getStartDate() == null) {
            return ResponseEntity.badRequest().body("Mandatory fields (leaveType, reason, startDate) are required.");
        }

//        if (leave.getId() != null && leave.getId() > 0) {
//
//            if (!leaveRepository.existsById(leave.getId())) {
//                return ResponseEntity.badRequest().body("Leave application with the provided ID does not exist.");
//            }
//            leaveRepository.save(leave);
//            return ResponseEntity.status(HttpStatus.OK).body("Leave application with ID " + leave.getId() + " updated successfully.");
//        }
        else {
            leaveRepository.save(leave);
            return ResponseEntity.status(HttpStatus.CREATED).body("New leave application added successfully.");
        }
    }

    //End point to get leave record using empid
    @GetMapping("/employee/{empId}")
    public List<Object[]> getLeaveRecordsByEmployeeId(@PathVariable Long empId) {
        return leaveRepository.getLeaveRecordsByEmployeeId(empId);
    }

//    @GetMapping("/employee/{empId}")
//    public ResponseEntity<List<Leave>> getLeaveByEmployeeId(@PathVariable Long empId) {
//        List<Leave> leaveRecords = leaveRepository.findByEmpId(empId);
//        return new ResponseEntity<>(leaveRecords, HttpStatus.OK);
//    }


    // Endpoint to delete a leave record
    @DeleteMapping("/delete/{leaveId}")
    public ResponseEntity<String> deleteLeave(@PathVariable Long leaveId) {
        try {

            Optional<Leave> optionalLeave = leaveRepository.findById(leaveId);
            if (optionalLeave.isEmpty()) {
                return new ResponseEntity<>("Leave with ID " + leaveId + " not found.", HttpStatus.NOT_FOUND);
            }

            leaveRepository.deleteById(leaveId);

            return new ResponseEntity<>("Leave with " + leaveId + " deleted.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete leave with " + leaveId + ": " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
//
//    @GetMapping("/list_pending_leave")
//    public ResponseEntity<List<Leave>> getAllPendingLeaveRecords() {
//        List<Leave> pendingLeaveList = leaveRepository.findByApprovedStatus();
//
//        if(pendingLeaveList.isEmpty()) {
//            return ResponseEntity.noContent().build(); // No pending leave found
//        }
//
//        return ResponseEntity.ok(pendingLeaveList);
//    }

//    @GetMapping("/pending")
//    public ResponseEntity<List<LeaveDTO>> getPendingLeave() {
//        List<Leave> pendingLeave = leaveRepository.findByApprovedStatus(false);
//        List<LeaveDTO> pendingLeaveDTOs = new ArrayList<>();
//        for (Leave leave : pendingLeave) {
//            LeaveDTO leaveDTO = new LeaveDTO(
//                    leave.getId(), // Include id here
//                    leave.getEmpId(),
//                    leave.getLeaveType(),
//                    leave.getReason(),
//                    leave.getStartDate(),
//                    leave.getEndDate()
//            );
//            pendingLeaveDTOs.add(leaveDTO);
//        }
//        return ResponseEntity.ok(pendingLeaveDTOs);

 /*   @PostMapping("/approve/{leaveId}")
    public ResponseEntity<String> approveLeave(@PathVariable Long leaveId, @RequestBody String remarks) {
        try {
            Optional<Leave> optionalLeave = leaveRepository.findById(leaveId);
            if (optionalLeave.isPresent()) {
                Leave leave = optionalLeave.get();
                leave.setApprovedStatus(true); // Change to boolean

                if (remarks != null) {
                    leave.setRemarks(remarks);
                }

                leaveRepository.save(leave);
                return new ResponseEntity<>("Leave with ID " + leaveId + " approved.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Leave with ID " + leaveId + " not found.", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to approve leave: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
*/


//    @PostMapping("/approve/{id}")
//    public ResponseEntity<String> approveLeave(@PathVariable Long id) {
//        try {
//            Optional<Leave> optionalLeave = leaveRepository.findById(id);
//            if (optionalLeave.isPresent()) {
//                Leave leave = optionalLeave.get();
//                leave.setApprovedStatus(true); // Change to boolean
//
//                leaveRepository.save(leave);
//                return new ResponseEntity<>("Leave with ID " + id + " approved.", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("Leave with ID " + id + " not found.", HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>("Failed to approve leave: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @PostMapping("/reject/{leaveId}")
//    public ResponseEntity<String> rejectLeave(@PathVariable Long leaveId) {
//        try {
//            SimpleJpaRepository<T, Leave> leaveRepository;
//            Optional<Leave> optionalLeave = leaveRepository.findById(leaveId);
//            if (optionalLeave.isPresent()) {
//                Leave leave = optionalLeave.get();
//                // You may want to set additional properties or perform other actions before rejecting the leave
//                leaveRepository.delete(leave);
//                return new ResponseEntity<>("Leave with ID " + leaveId + " rejected.", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>("Leave with ID " + leaveId + " not found.", HttpStatus.NOT_FOUND);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>("Failed to reject leave: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

