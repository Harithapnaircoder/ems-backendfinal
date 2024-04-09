package ems.example.ems.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class LeaveDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("empId")
    private Long empId;

    @JsonProperty("leaveType")
    private String leaveType;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("startDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonProperty("endDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;


    // Constructor
    public LeaveDTO(Long id,Long empId, String leaveType, String reason, Date startDate, Date endDate) {
        this.id = id;
        this.empId = empId;
        this.leaveType = leaveType;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}