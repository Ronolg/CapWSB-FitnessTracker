package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.internal.ActivityType;
import pl.wsb.fitnesstracker.training.api.TrainingNotFoundException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingServiceImpl trainingService;

    @GetMapping
    public List<Training> findAll() {
        return trainingService.findAllTrainings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Training> findById(@PathVariable Long id) {
        return trainingService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TrainingNotFoundException(id));
    }

    @GetMapping("/user/{userId}")
    public List<Training> findByUserId(@PathVariable Long userId) {
        return trainingService.findTrainingsByUserId(userId);
    }

    @GetMapping("/after")
    public List<Training> findAfterDate(@RequestParam("date") Date date) {
        return trainingService.findTrainingsAfterDate(date);
    }

    @GetMapping("/activity")
    public List<Training> findByActivity(@RequestParam("type") ActivityType activityType) {
        return trainingService.findTrainingsByActivity(activityType);
    }
}
