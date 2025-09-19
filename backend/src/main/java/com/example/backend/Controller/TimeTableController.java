package com.example.backend.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.Model.TimeTableEntity;
import com.example.backend.Service.TimeTableService;

@RestController
@RequestMapping("/time-table")
@CrossOrigin(origins = { "http://localhost:4200",
        "http://192.168.0.107:4200" }, allowedHeaders = "*", allowCredentials = "true", methods = { RequestMethod.GET,
                RequestMethod.POST,
                RequestMethod.PUT,
                RequestMethod.DELETE })
public class TimeTableController {
    
    @Autowired
    private TimeTableService timeTableService;

    @PostMapping("/add-time-table")
    public ResponseEntity<TimeTableEntity> addTimeTable(@RequestBody TimeTableEntity timeTable) {
        TimeTableEntity newTimeTable = timeTableService.addTimeTable(timeTable);
        return ResponseEntity.ok(newTimeTable);
    }

    @PutMapping("/update-time-table/{id}")
    public ResponseEntity<TimeTableEntity> updateTimeTable(@PathVariable Integer id,
            @RequestBody TimeTableEntity timeTableDetails) {

        Optional<TimeTableEntity> timeTable = timeTableService.getTimeTableById(id);
        if (timeTable.isPresent()) {
            TimeTableEntity newTimeTable = timeTableService.updateTimeTable(timeTable.get(), timeTableDetails);
            return ResponseEntity.ok(newTimeTable);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-time-table/{id}")
    public ResponseEntity<Void> deleteTimeTable(@PathVariable Integer id) {
        Optional<TimeTableEntity> timeTable = timeTableService.getTimeTableById(id);
        if (timeTable.isPresent()) {
            timeTableService.deleteTimeTable(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
