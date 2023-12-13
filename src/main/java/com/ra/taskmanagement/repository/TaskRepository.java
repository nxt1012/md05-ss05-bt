package com.ra.taskmanagement.repository;

import com.ra.taskmanagement.entity.Status;
import com.ra.taskmanagement.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByTitleContainingIgnoreCase(String title);
    @Query("SELECT t FROM Task t WHERE t.status != :status ORDER BY t.createdAt DESC")
    Page<Task> findAllByStatusNotOrderByCreatedAtDesc(Status status, Pageable pageable);

}
