package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.training.internal.ActivityType;
import pl.wsb.fitnesstracker.training.api.TrainingNotFoundException;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {

    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;


    /**
     * @return
     */
    @GetMapping
    public List<Training> findAll() {
        return trainingService.findAllTrainings();
    }


    /**
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public List<Training> findByUserId(@PathVariable Long userId) {
        return trainingService.findTrainingsByUserId(userId);
    }

    /**
     * @param date
     * @return
     */
    @GetMapping("/finished/{date}")
    public List<Training> findAfterDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return trainingService.findTrainingsAfterDate(date);
    }

    /**
     * @param activityType
     * @return
     */
    @GetMapping("/activityType")
    public List<Training> findByActivity(@RequestParam("activityType") ActivityType activityType) {
        return trainingService.findTrainingsByActivity(activityType);
    }
}
