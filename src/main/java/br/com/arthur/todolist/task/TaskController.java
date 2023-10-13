package br.com.arthur.todolist.task;

import br.com.arthur.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/task")

public class TaskController {
    @Autowired
    private ITaskRepository taskRepository;
    @PostMapping("")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);
        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início / data de término deve ser maior que a data atual");
        }

        if(taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A data de início deve ser menor do que a data de término");
        }

        var createdTask = this.taskRepository.save(taskModel);
        System.out.println("Cadastrou com êxito");
        return ResponseEntity.status(HttpStatus.OK).body(createdTask);
    }

    @GetMapping("")
    public List<TaskModel> List(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        return this.taskRepository.findByIdUser((UUID) idUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity Update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {
            var task = this.taskRepository.findById(id).orElse(null);
            if(task == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada.");
            }
            var idUser = request.getAttribute("idUser");
            if(!taskModel.getIdUser().equals(idUser)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não tem permissão para alterar essa tarefa");
            }
            Utils.copyNonNullProperties(taskModel, task);
            var taskUpdated = this.taskRepository.save(task);
            return ResponseEntity.ok().body(taskUpdated);
    }
}
