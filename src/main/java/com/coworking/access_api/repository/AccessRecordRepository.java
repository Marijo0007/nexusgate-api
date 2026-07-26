package com.coworking.access_api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coworking.access_api.model.AccessRecord;

@Repository
public interface AccessRecordRepository extends JpaRepository<AccessRecord, Long> {

    List<AccessRecord> findByUserId(Long userId);
    List<AccessRecord> findByUserIdOrderByEntryTimeDesc(Long userId);
    List<AccessRecord> findByExitTimeIsNull();
    List<AccessRecord> findByEntryTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT ar FROM AccessRecord ar WHERE ar.user.id = :userId " + 
        "AND ar.accessType = 'ENTRY' AND ar.exitTime IS NULL " + 
        "ORDER BY ar.entryTime DESC")
    Optional<AccessRecord> findOpenSessionByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(ar) FROM AccessRecord ar WHERE ar.user.id = :userId " +
        "AND ar.entryTime BETWEEN :start AND :end")
        Long countVisitsByUSerIdAndDateRange(
            @Param("userId") Long userId, 
            @Param("start") LocalDateTime start, 
            @Param("end") LocalDateTime end
        );
}
