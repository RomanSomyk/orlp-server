package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.DTO.DTOBuilder;
import com.softserve.academy.spaced.repetition.DTO.impl.UserCardQueuePublicDTO;
import com.softserve.academy.spaced.repetition.domain.UserCardQueue;
import com.softserve.academy.spaced.repetition.domain.UserCardQueueStatus;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.service.UserCardQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class UserCardQueueController {
    private final UserCardQueueService userCardQueueService;

    @Autowired
    public UserCardQueueController(UserCardQueueService userCardQueueService) {
        this.userCardQueueService = userCardQueueService;
    }

    @PutMapping("/api/private/decks/{deckId}/cards/{cardId}/queue")
    @PreAuthorize(value = "@accessToUrlService.hasAccessToCard(#deckId, #cardId)")
    public ResponseEntity updateUserCardQueue(
            @PathVariable Long deckId, @PathVariable Long cardId, @RequestBody String status)
            throws NotAuthorisedUserException {
        for (UserCardQueueStatus userCardQueueStatus : UserCardQueueStatus.values()) {
            if (userCardQueueStatus.status.equals(status)) {
                userCardQueueService.updateUserCardQueue(deckId, cardId, userCardQueueStatus);
                return new ResponseEntity(HttpStatus.OK);
            }
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/api/private/decks/{deckId}/cards-that-need-repeating/count")
    public ResponseEntity<Long> countCardsThatNeedRepeating(@PathVariable Long deckId) throws
            NotAuthorisedUserException {
        return new ResponseEntity<>(userCardQueueService.countCardsThatNeedRepeating(deckId), HttpStatus.OK);
    }

    @GetMapping("api/user/card/queue/{user_card_queue_id}")
    public ResponseEntity<UserCardQueuePublicDTO> getUserCardQueueById(@PathVariable long user_card_queue_id) {
        UserCardQueue userCardQueue = userCardQueueService.getUserCardQueueById(user_card_queue_id);
        Link selfLink = linkTo(methodOn(UserCardQueueController.class).getUserCardQueueById(user_card_queue_id)).withSelfRel();
        UserCardQueuePublicDTO userCardQueuePublicDTO = DTOBuilder.buildDtoForEntity(userCardQueue, UserCardQueuePublicDTO.class, selfLink);
        return new ResponseEntity<>(userCardQueuePublicDTO, HttpStatus.OK);
    }
}
