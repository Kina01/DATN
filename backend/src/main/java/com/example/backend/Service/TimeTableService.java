package com.example.backend.Service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.Model.TimeTableEntity;
import com.example.backend.Repository.TimeTableRepository;

@Service
public class TimeTableService {
    
    @Autowired 
    private TimeTableRepository timeTableRepository;

    public Optional<TimeTableEntity> getTimeTableById(Integer idTimeTable) {
        return timeTableRepository.findByIdTimeTable(idTimeTable);
    }

    @Transactional
    public TimeTableEntity addTimeTable(TimeTableEntity timeTableEntity) {
        return timeTableRepository.save(timeTableEntity);
    }

    @Transactional
    public TimeTableEntity updateTimeTable(TimeTableEntity oldTimeTable, TimeTableEntity newTimeTable) {
        oldTimeTable.setSubjectName(newTimeTable.getSubjectName());
        oldTimeTable.setClassName(newTimeTable.getClassName());
        oldTimeTable.setRoom(newTimeTable.getRoom());
        oldTimeTable.setDayOfWeek(newTimeTable.getDayOfWeek());
        oldTimeTable.setPeriodStart(newTimeTable.getPeriodStart());
        oldTimeTable.setPeriodEnd(newTimeTable.getPeriodEnd());
        return timeTableRepository.save(oldTimeTable);
    }

    @Transactional
    public void deleteTimeTable(Integer idTimeTable) {
        timeTableRepository.deleteByIdTimeTable(idTimeTable);
    }
}