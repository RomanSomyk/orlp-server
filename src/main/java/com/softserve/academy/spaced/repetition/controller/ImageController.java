package com.softserve.academy.spaced.repetition.controller;

import com.softserve.academy.spaced.repetition.exceptions.ImageContextDublicationException;
import com.softserve.academy.spaced.repetition.exceptions.ImageNameDublicationException;
import com.softserve.academy.spaced.repetition.service.ImageService;
import com.softserve.academy.spaced.repetition.service.ImageService.OperationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
public class ImageController  {


    @Autowired
    private ImageService imageService;


    @PostMapping("/api/service/image/")
    public ResponseEntity<?> addImageToDB(@RequestParam("fileName") String fileName, @RequestParam("file") MultipartFile file) throws ImageContextDublicationException, ImageNameDublicationException {

        HttpStatus httpStatus = HttpStatus.CONFLICT;;
        OperationStatus operationStatus = imageService.addImageToDB(file, fileName);

            switch (operationStatus) {
                case OK:
                    httpStatus = HttpStatus.OK;
                    break;
                case IMAGE_EXISTS:
                    throw new ImageContextDublicationException();
                case WRONG_FILE_SIZE:
                    throw new MultipartException("File upload error: file is too large.");
                case NAME_EXISTS:
                    throw new ImageNameDublicationException();
            }

        return new ResponseEntity<>(httpStatus);
    }

    @GetMapping("/api/service/images/")
    public ResponseEntity<List<MultipartFile>> getAllImages (){

        List<MultipartFile> imagesList = imageService.getAllImages();
        return new ResponseEntity<List<MultipartFile>>(imagesList, HttpStatus.OK);
    }
}
