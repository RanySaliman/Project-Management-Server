package ProjectManagement.controllers;

import ProjectManagement.entities.Board;
import ProjectManagement.entities.Response;
import ProjectManagement.entities.Task;
import ProjectManagement.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * end point that's responsible for adding task to board
     * @header id
     * @return board
     */
    @RequestMapping(value = "addTask", method = RequestMethod.POST)
    public ResponseEntity<String> addTask(@RequestBody Task fields) {
        Response<Task> task = taskService.addTask(fields.getBoardId(),fields.getTaskParentId(), fields.getAssignedUserId(), fields.getImportance(), fields.getTitle(), fields.getDescription());
        if(task.isSucceed()){
            return ResponseEntity.ok("Task created successfully");
        }else {
            return ResponseEntity.badRequest().body(null);
        }

    }

}
