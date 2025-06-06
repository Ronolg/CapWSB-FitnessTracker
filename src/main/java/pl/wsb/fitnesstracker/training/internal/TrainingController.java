package pl.wsb.fitnesstracker.training.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.wsb.fitnesstracker.training.api.Training;
import pl.wsb.fitnesstracker.training.api.TrainingDto;
import pl.wsb.fitnesstracker.training.internal.ActivityType;
import pl.wsb.fitnesstracker.training.api.TrainingNotFoundException;
import pl.wsb.fitnesstracker.user.api.User;
import pl.wsb.fitnesstracker.user.api.UserDto;
import pl.wsb.fitnesstracker.user.api.UserNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public ResponseEntity<Training> findById(@PathVariable Long id) {
        return trainingService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new TrainingNotFoundException(id));
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

    /**
     * Creates a new training entry for a specified user.
     * <p>
     * This endpoint accepts a {@link TrainingDto} in the request body and delegates the creation logic
     * to the service layer. If the training is successfully created, it returns the created training data
     * along with a 201 Created HTTP status.
     * </p>
     * <p>
     * If the user associated with the training does not exist, a {@link ResponseStatusException} is thrown
     * with a 404 Not Found status.
     * </p>
     *
     * @param trainingDto the data of the training to be created, including a reference to the user ID
     * @return a {@link ResponseEntity} containing the created {@link TrainingDto} with HTTP status 201 Created
     * @throws ResponseStatusException if the specified user is not found (404 Not Found)
     */
    @PostMapping()
    public ResponseEntity<TrainingDto> createNewTraining(@RequestBody TrainingDto trainingDto)
    {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(trainingMapper.toDto(trainingService.createTrainingForUser(trainingDto)));
        } catch (UserNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    /**
     * Updates an existing training identified by the given {@code trainingId}, or creates a new one if it does not exist.
     * <p>
     * The method delegates to the service layer to perform the update or creation logic. If the operation is successful,
     * the updated or newly created {@link TrainingDto} is returned with a 200 OK status. If any error occurs, the method
     * throws a {@link ResponseStatusException} with an appropriate HTTP status and message.
     * </p>
     *
     * @param trainingId      the ID of the training to update or create
     * @param newTrainingDto  the training data to use for the update or creation
     * @return a {@link ResponseEntity} containing the updated or newly created {@link TrainingDto}
     * @throws ResponseStatusException if the input is invalid (400 Bad Request) or the user/training is not found (404 Not Found)
     */
    @PutMapping("/{trainingId}")
    public ResponseEntity<TrainingDto> updateOrCreateTrainingForUser(
            @PathVariable Long trainingId,
            @RequestBody TrainingDto newTrainingDto) {

        try {
            return ResponseEntity.status(HttpStatus.OK)
                .body(trainingMapper.toDto(trainingService.updateOrCreateTrainingForUser(trainingId, newTrainingDto)));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        } catch (UserNotFoundException | TrainingNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }
}
