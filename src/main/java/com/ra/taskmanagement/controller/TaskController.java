package com.ra.taskmanagement.controller;
import com.ra.taskmanagement.entity.Category;
import com.ra.taskmanagement.entity.Status;
import com.ra.taskmanagement.entity.Task;
import com.ra.taskmanagement.service.CategoryService;
import com.ra.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@CrossOrigin("*")
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final CategoryService categoryService;

    @Autowired
    public TaskController(TaskService taskService, CategoryService categoryService) {
        this.taskService = taskService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Task> tasksPage = taskService.getAllTasksPaginated(PageRequest.of(page, size));
        List<Task> tasks = tasksPage.getContent();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasksByTitle(@RequestParam String title) {
        List<Task> tasks = taskService.searchTasksByTitle(title);
        return tasks.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<Task> createTaskWithCategory(@PathVariable Long categoryId, @RequestBody Task task) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        task.setCategory(category);

        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}/{categoryId}")
    public ResponseEntity<Task> updateTaskWithCategory(@PathVariable Long id, @PathVariable Long categoryId, @RequestBody Task updatedTask) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category == null) {
            return ResponseEntity.notFound().build();
        }

        updatedTask.setCategory(category); // Set the category for the updated task

        Task task = taskService.updateTask(id, updatedTask);
        return task != null ? ResponseEntity.ok(task) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long id) {
        Task completedTask = taskService.markTaskAsCompleted(id);
        return completedTask != null ? ResponseEntity.ok(completedTask) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long id,
            @PathVariable String status
    ) {
        Task task = taskService.getTaskById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        switch (status.toUpperCase()) {
            case "TODO":
                task.setStatus(Status.TODO);
                break;
            case "IN_PROGRESS":
                task.setStatus(Status.IN_PROGRESS);
                break;
            case "DONE":
                task.setStatus(Status.DONE);
                break;
            default:
                return ResponseEntity.badRequest().build();
        }

        Task updatedTask = taskService.updateTask(id, task);
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

}
