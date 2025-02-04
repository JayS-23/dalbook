package com.example.facebook_integration.controller;

import com.example.facebook_integration.model.Topic;
import com.example.facebook_integration.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/topics")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @PostMapping
    public Topic createTopic(@RequestBody Topic topic) {
        return topicService.saveTopic(topic);
    }

    @GetMapping
    public List<Topic> getAllTopics() {
        return topicService.getAllTopics();
    }

    @GetMapping("/latest")
    public Topic getLatestTopic() {
        return topicService.getLatestTopic();
    }
}
