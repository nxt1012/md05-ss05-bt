package com.ra.taskmanagement.service;

import com.ra.taskmanagement.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    List<Task> getAllTasks();

    Page<Task> getAllTasksPaginated(Pageable pageable);

    Task getTaskById(Long id);

    Task createTask(Task task);

    Task updateTask(Long id, Task updatedTask);

    void deleteTask(Long id);

    List<Task> searchTasksByTitle(String title);

    Task markTaskAsCompleted(Long id);
}
