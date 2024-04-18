package com.restaurantdelivery.service;

import com.restaurantdelivery.entity.Feedback;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.repository.FeedbackRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FeedbackService {

    private FeedbackRepository feedbackRepository;

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Feedback getFeedbackByIdOrThrow(Long id) {
        return feedbackRepository.findById(id).orElseThrow(() ->
                new ServerException(HttpStatus.NOT_FOUND,
                        "Feedback with id " + id + " does not exist")
        );
    }

    public void nonExistOrThrow(Feedback feedback) {
        feedbackRepository.findById(feedback.getId()).ifPresent(usr -> {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "Feedback with id " + usr.getId() + " already exists");
        });
    }

    public Feedback getFeedbackById(Long id) {
        return getFeedbackByIdOrThrow(id);
    }

    public Feedback addFeedback(Feedback feedback) {
        nonExistOrThrow(feedback);
        return feedbackRepository.save(feedback);
    }

    public Feedback updateFeedback(Long id, Feedback feedback) {
        nonExistOrThrow(feedback);
        return feedbackRepository.save(feedback);
    }

    public void deleteFeedbackById(Long id) {
        feedbackRepository.deleteById(id);
    }
}
