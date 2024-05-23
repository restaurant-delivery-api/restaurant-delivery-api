package com.restaurantdelivery.controller;

import com.restaurantdelivery.api.FeedbackApi;
import com.restaurantdelivery.dto.FeedbackDto;
import com.restaurantdelivery.entity.Feedback;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor

@RestController
public class FeedbackController implements FeedbackApi {

    private FeedbackService feedbackService;
    private ModelMapper feedbackMapper;

    public List<FeedbackDto> getFeedbacks() {
        return feedbackService.getAllFeedbacks().stream().map(this::convertToDto).toList();
    }

    public FeedbackDto getFeedback(Long id) {
        return convertToDto(feedbackService.getFeedbackById(id));
    }

    public FeedbackDto addFeedback(FeedbackDto feedbackDto) {
        return convertToDto(feedbackService.addFeedback(convertToEntity(feedbackDto)));
    }


    public FeedbackDto updateFeedback(Long id,
                                      FeedbackDto feedbackDto) {
        if (!Objects.equals(id, feedbackDto.getId()))
        {
            throw new ServerException(HttpStatus.BAD_REQUEST, "IDs don't match");
        }
        return convertToDto(feedbackService.updateFeedback(id, convertToEntity(feedbackDto)));
    }


    public void deleteFeedback(Long id) {
        feedbackService.deleteFeedbackById(id);
    }

    private FeedbackDto convertToDto(Feedback feedback) {
        return feedbackMapper.map(feedback, FeedbackDto.class);
    }

    private Feedback convertToEntity(FeedbackDto feedbackDto) {
        return feedbackMapper.map(feedbackDto, Feedback.class);
    }
}
