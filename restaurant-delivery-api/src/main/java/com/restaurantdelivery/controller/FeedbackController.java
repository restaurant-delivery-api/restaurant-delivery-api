package com.restaurantdelivery.controller;

import com.restaurantdelivery.dto.FeedbackDto;
import com.restaurantdelivery.entity.Feedback;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor

@RestController
@RequestMapping("/api/v1/feedback")
public class FeedbackController {

    private FeedbackService feedbackService;
    private ModelMapper feedbackMapper;

    @GetMapping("/all")
    @ResponseBody
    public List<FeedbackDto> getFeedbacks() {
        return feedbackService.getAllFeedbacks().stream().map(this::convertToDto).toList();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public FeedbackDto getFeedback(@PathVariable("id") Long id) {
        return convertToDto(feedbackService.getFeedbackById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public FeedbackDto addFeedback(@RequestBody FeedbackDto feedbackDto) {
        return convertToDto(feedbackService.addFeedback(convertToEntity(feedbackDto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public FeedbackDto updateFeedback(@PathVariable("id") Long id,
                              @RequestBody FeedbackDto feedbackDto) {
        if (!Objects.equals(id, feedbackDto.getId()))
        {
            throw new ServerException(HttpStatus.BAD_REQUEST, "IDs don't match");
        }
        return convertToDto(feedbackService.updateFeedback(id, convertToEntity(feedbackDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeedback(@PathVariable("id") Long id) {
        feedbackService.deleteFeedbackById(id);
    }

    private FeedbackDto convertToDto(Feedback feedback) {
        return feedbackMapper.map(feedback, FeedbackDto.class);
    }

    private Feedback convertToEntity(FeedbackDto feedbackDto) {
        return feedbackMapper.map(feedbackDto, Feedback.class);
    }
}
