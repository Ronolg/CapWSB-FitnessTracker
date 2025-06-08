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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/trainings")
@RequiredArgsConstructor
public class TrainingController {
    private final TrainingServiceImpl trainingService;
    private final TrainingMapper trainingMapper;

    /**
     * Retrieves all training sessions.
     * <p>
     * This endpoint returns all training sessions stored in the system.
     * The response includes training details along with associated user identifiers.
     * </p>
     *
     * @return {@link ResponseEntity} containing a list of {@link TrainingDto} objects
     */
    @GetMapping
    public ResponseEntity<List<TrainingDto>> findAllTrainings() {
        List<TrainingDto> result = trainingService.findAllTrainings()
                .stream()
                .map(trainingMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }



    /**
     * Retrieves all training sessions associated with a specific user.
     * <p>
     * This endpoint returns a list of {@link TrainingDto} objects that represent all
     * training records assigned to the user with the given ID.
     * Each {@code TrainingDto} contains training metadata along with the {@code userId}.
     * </p>
     *
     * @param userId the ID of the user whose trainings are to be retrieved
     * @return a {@link ResponseEntity} containing the list of {@link TrainingDto} objects
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<TrainingDto>> findByUserId(@PathVariable Long userId) {
        List<TrainingDto> result = trainingService.findTrainingsByUserId(userId)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves all training sessions that finished after the specified date.
     * <p>
     * This endpoint returns a list of {@link TrainingDto} representing trainings
     * whose {@code endTime} is after the provided date.
     * The date must be in ISO format (yyyy-MM-dd).
     * </p>
     *
     * @param date the date after which finished trainings should be retrieved (format: yyyy-MM-dd)
     * @return a {@link ResponseEntity} containing a list of {@link TrainingDto} objects
     */
    @GetMapping("/finished/{date}")
    public ResponseEntity<List<TrainingDto>> findAfterDate(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        List<TrainingDto> result = trainingService.findTrainingsAfterDate(date)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves all training sessions filtered by the specified activity type.
     * <p>
     * This endpoint allows clients to fetch trainings that match a specific {@link ActivityType},
     * such as RUNNING, CYCLING, TENNIS, etc. The activity type must be provided as a query parameter.
     * </p>
     *
     * Example request:
     * <pre>
     * GET /v1/trainings/activityType?activityType=RUNNING
     * </pre>
     *
     * @param activityType the {@link ActivityType} used to filter training records (e.g. RUNNING, TENNIS)
     * @return a {@link ResponseEntity} containing a list of {@link TrainingDto} objects
     *         that match the specified activity type
     */
    @GetMapping("/activityType")
    public ResponseEntity<List<TrainingDto>> findByActivity(@RequestParam("activityType") ActivityType activityType) {
        List<TrainingDto> result = trainingService.findTrainingsByActivity(activityType)
                .stream()
                .map(trainingMapper::toDto)
                .toList();
        return ResponseEntity.ok(result);
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
