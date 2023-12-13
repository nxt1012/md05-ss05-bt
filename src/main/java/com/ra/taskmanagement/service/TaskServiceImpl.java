package com.ra.taskmanagement.service;

import com.ra.taskmanagement.entity.Status;
import com.ra.taskmanagement.entity.Task;
import com.ra.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Page<Task> getAllTasksPaginated(Pageable pageable) {
        Status doneStatus = Status.DONE;
        return taskRepository.findAllByStatusNotOrderByCreatedAtDesc(doneStatus, pageable);
    }

    @Override
    public List<Task> searchTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public Task createTask(Task task) {
        LocalDateTime now = LocalDateTime.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task updatedTask) {
        Optional<Task> taskToUpdate = taskRepository.findById(id);

        if (taskToUpdate.isPresent()) {
            Task existingTask = taskToUpdate.get();
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setStatus(updatedTask.getStatus());
            existingTask.setCategory(updatedTask.getCategory());
            existingTask.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(existingTask);
        }

        return null;
    }

    @Override
    public Task markTaskAsCompleted(Long id) {
        Optional<Task> taskOptional = taskRepository.findById(id);

        if (taskOptional.isPresent()) {
            Task existingTask = taskOptional.get();
            existingTask.setStatus(Status.DONE);
            existingTask.setUpdatedAt(LocalDateTime.now());
            return taskRepository.save(existingTask);
        }

        return null;
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
