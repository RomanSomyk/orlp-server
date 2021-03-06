package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.exceptions.EmptyFileException;
import com.softserve.academy.spaced.repetition.exceptions.NotAuthorisedUserException;
import com.softserve.academy.spaced.repetition.exceptions.NotOwnerOperationException;
import com.softserve.academy.spaced.repetition.exceptions.WrongFormatException;
import com.softserve.academy.spaced.repetition.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class CardsFileController {

    @Autowired
    private CardService cardService;

    @PostMapping("api/private/upload/deck/{deckId}/cards")
    public ResponseEntity uploadFile(@RequestParam("file") MultipartFile cardsFile, @PathVariable Long deckId) throws WrongFormatException, NotOwnerOperationException, NotAuthorisedUserException, EmptyFileException, IOException {
        cardService.uploadCards(cardsFile, deckId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("download/deck/{deckId}/cards")
    public void downloadFile(HttpServletResponse response, @PathVariable Long deckId) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=Cards.yml");
        cardService.downloadCards(deckId, response.getOutputStream());
    }

    @GetMapping("api/private/download/template/cards")
    public void downloadFileTemplate(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=CardsTemplate.yml");
        cardService.downloadCardsTemplate(response.getOutputStream());
    }
}
