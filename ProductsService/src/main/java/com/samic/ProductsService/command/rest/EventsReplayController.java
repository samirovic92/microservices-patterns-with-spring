package com.samic.ProductsService.command.rest;

import lombok.AllArgsConstructor;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/replay")
@AllArgsConstructor
public class EventsReplayController {
    private EventProcessingConfiguration eventProcessingConfiguration;

    @PostMapping("/{eventProcessorName}")
    public ResponseEntity<String> replayEvents(@PathVariable String eventProcessorName) {

        var eventProcessorOptional = eventProcessingConfiguration.eventProcessor(eventProcessorName, TrackingEventProcessor.class);
        if (eventProcessorOptional.isPresent()) {
            var eventProcessor = eventProcessorOptional.get();
            eventProcessor.shutDown();
            eventProcessor.resetTokens();
            eventProcessor.start();
            return ResponseEntity
                    .ok(String.format("The event processor with name [%s] has been reset", eventProcessor));
        }

        return ResponseEntity.badRequest()
                .body(String.format("The event processor with name [%s] is not tracking event", eventProcessorName));
    }
}
