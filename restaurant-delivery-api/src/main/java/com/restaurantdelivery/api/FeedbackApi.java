package com.restaurantdelivery.api;

import com.restaurantdelivery.configuration.Constants;
import com.restaurantdelivery.dto.FeedbackDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RequestMapping(Constants.BASE_API_PATH + "/feedback")
public interface FeedbackApi {

    @GetMapping("/all")
    @ResponseBody
    List<FeedbackDto> getFeedbacks();

    @GetMapping("/{id}")
    @ResponseBody
    FeedbackDto getFeedback(@PathVariable("id") @PositiveOrZero(message = "id must be not negative") Long id);

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    FeedbackDto addFeedback(@RequestBody FeedbackDto feedbackDto);

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    FeedbackDto updateFeedback(@PathVariable("id") @PositiveOrZero(message = "id must be not negative") Long id,
                               @RequestBody FeedbackDto feedbackDto);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteFeedback(@PathVariable("id") @PositiveOrZero(message = "id must be not negative") Long id);

}
