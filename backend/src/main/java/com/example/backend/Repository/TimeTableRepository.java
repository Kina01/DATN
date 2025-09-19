package com.example.backend.Repository;

import com.example.backend.Model.TimeTableEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeTableRepository extends JpaRepository<TimeTableEntity, Integer> {

    Optional<TimeTableEntity> findByIdTimeTable(Integer idTimeTable);
    Optional<TimeTableEntity> deleteByIdTimeTable(Integer idTimeTable);
}