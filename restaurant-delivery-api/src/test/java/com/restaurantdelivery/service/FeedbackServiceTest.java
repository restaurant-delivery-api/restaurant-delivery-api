package com.restaurantdelivery.service;

import com.restaurantdelivery.entity.Feedback;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.repository.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFeedbacks() {
        Feedback feedback1 = new Feedback();
        Feedback feedback2 = new Feedback();
        List<Feedback> feedbackList = Arrays.asList(feedback1, feedback2);

        when(feedbackRepository.findAll()).thenReturn(feedbackList);

        List<Feedback> result = feedbackService.getAllFeedbacks();

        assertEquals(2, result.size());
        verify(feedbackRepository, times(1)).findAll();
    }

    @Test
    void testGetFeedbackByIdOrThrow() {
        Feedback feedback = new Feedback();
        feedback.setId(1L);

        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

        Feedback result = feedbackService.getFeedbackByIdOrThrow(1L);

        assertEquals(feedback, result);
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void testGetFeedbackByIdOrThrow_NotFound() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        ServerException exception = assertThrows(ServerException.class, () -> {
            feedbackService.getFeedbackByIdOrThrow(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Feedback with id 1 does not exist", exception.getMessage());
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void testNonExistOrThrow() {
        Feedback feedback = new Feedback();
        feedback.setId(1L);

        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> feedbackService.nonExistOrThrow(feedback));
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void testNonExistOrThrow_AlreadyExists() {
        Feedback feedback = new Feedback();
        feedback.setId(1L);

        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(feedback));

        ServerException exception = assertThrows(ServerException.class, () -> {
            feedbackService.nonExistOrThrow(feedback);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Feedback with id 1 already exists", exception.getMessage());
        verify(feedbackRepository, times(1)).findById(1L);
    }

    @Test
    void testAddFeedback() {
        Feedback feedback = new Feedback();
        feedback.setId(1L);

        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());
        when(feedbackRepository.save(feedback)).thenReturn(feedback);

        Feedback result = feedbackService.addFeedback(feedback);

        assertEquals(feedback, result);
        verify(feedbackRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(feedback);
    }

    @Test
    void testUpdateFeedback() {
        Feedback feedback = new Feedback();
        feedback.setId(1L);

        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());
        when(feedbackRepository.save(feedback)).thenReturn(feedback);

        Feedback result = feedbackService.updateFeedback(1L, feedback);

        assertEquals(feedback, result);
        verify(feedbackRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(feedback);
    }

    @Test
    void testDeleteFeedbackById() {
        doNothing().when(feedbackRepository).deleteById(1L);

        feedbackService.deleteFeedbackById(1L);

        verify(feedbackRepository, times(1)).deleteById(1L);
    }
}
