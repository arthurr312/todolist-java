package br.com.arthur.todolist.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")

public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;
    @PostMapping("")
    public TaskModel create(@RequestBody TaskModel taskModel) {
        var createdTask = this.taskRepository.save(taskModel);
        System.out.println("Cadastrou com êxito");
        return createdTask;
    }
}
