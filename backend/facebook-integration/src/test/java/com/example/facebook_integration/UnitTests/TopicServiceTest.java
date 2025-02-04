package com.example.facebook_integration.UnitTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.example.facebook_integration.model.Topic;
import com.example.facebook_integration.repository.TopicRepository;
import com.example.facebook_integration.service.Implementations.TopicServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicServiceImpl topicService;

    private Topic topic1;
    private Topic topic2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        topic1 = new Topic();
        topic1.setId(1l);
        topic1.setTopic("First Topic");

        topic2 = new Topic();
        topic2.setId(2l);
        topic2.setTopic("Second Topic");
    }

    @Test
    public void testSaveTopic() {
        when(topicRepository.save(topic1)).thenReturn(topic1);

        Topic result = topicService.saveTopic(topic1);

        assertEquals(topic1, result);
        verify(topicRepository, times(1)).save(topic1);
    }

    @Test
    public void testGetAllTopics() {
        List<Topic> topics = Arrays.asList(topic1, topic2);
        when(topicRepository.findAll()).thenReturn(topics);

        List<Topic> result = topicService.getAllTopics();

        assertEquals(topics, result);
        verify(topicRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllTopicsEmpty() {
        when(topicRepository.findAll()).thenReturn(Collections.emptyList());

        List<Topic> result = topicService.getAllTopics();

        assertEquals(Collections.emptyList(), result);
        verify(topicRepository, times(1)).findAll();
    }

    @Test
    public void testGetLatestTopic() {
        when(topicRepository.findTopByOrderByIdDesc()).thenReturn(topic2);

        Topic result = topicService.getLatestTopic();

        assertEquals(topic2, result);
        verify(topicRepository, times(1)).findTopByOrderByIdDesc();
    }

    @Test
    public void testGetLatestTopicNoTopics() {
        when(topicRepository.findTopByOrderByIdDesc()).thenReturn(null);

        Topic result = topicService.getLatestTopic();

        assertEquals(null, result);
        verify(topicRepository, times(1)).findTopByOrderByIdDesc();
    }
}

