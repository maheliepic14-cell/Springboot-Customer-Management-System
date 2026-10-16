package com.customer.customer_management_system.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CustomerImageService {

    //@Value assigned default value from properties files
    //So basically its inject the values into the fields
    //And also its convert the objected value to the required data type
    //Basically using to inject configuration values into spring beans
    @Value("${customer.upload-dir}")
    private String uploadDir;

    //Receives the uploaded file through multipart support
    public String saveImage(MultipartFile image) throws IOException {


        //Image Validation

        //Check that an image was uploaded
        if (image == null || image.isEmpty()){
            throw new IllegalArgumentException("Please select an image");
        }

        //Allow only specific image types
        String contentType = image.getContentType();

        if (contentType == null ||
                !(contentType.equals("image/jpeg") ||
                        contentType.equals("image/png") ||
                        contentType.equals("image/gif") ||
                        contentType.equals("image/bmp")))
        {
            throw new IllegalArgumentException(
                    "Only JPG, PNG, GIF or BMP files are supported");
        }

        //Maximum file size: 2MB
        if (image.getSize() > 2 * 1024 * 1024){
            throw new IllegalArgumentException("Size of image is too large");
        }

        //Just the path if the uploaded directory
        //Here uploadDir is a string
        //So Paths.get method convert the string into a path object
        Path uploadPath = Paths.get(uploadDir);

        //Create the folder if it doesn't exist
        Files.createDirectories(uploadPath);

        String originalFilename = image.getOriginalFilename();

        //Start with an empty extension
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")){

            //Taking the extension
            //lastIndexOf - finds the last dot
            //substring = takes the string from that last dot to the end

            extension =  originalFilename.substring(originalFilename.lastIndexOf("."));

        }

        String filename = UUID.randomUUID() + extension;

        //Save the image
        try {
            //Build the full file path
            //resolve combines uploadPath with the filename

            Path filepath = uploadPath.resolve(filename);

            //Copy the file into the destination file
            //getInputStream provides a stream for reading files.byte
            //So that's mean this is basically convert the image into bytes
            //And this files.copy writes(copies) all the bytes into the destination file
            Files.copy(image.getInputStream(), filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return filename;

    }

    public byte[] getImage(String filename) throws IOException {

        //Creates the file path
        Path filepath = Paths.get(uploadDir).resolve(filename).normalize();

        if (!Files.exists(filepath) || !Files.isRegularFile(filepath)){
            throw new FileNotFoundException("Image not found");
        }

        return Files.readAllBytes(filepath);

    }


    public String getImageContentType(String filename) {

        String lowerFileName = filename.toLowerCase();

        if (lowerFileName.endsWith(".png")) {
            return "image/png";
        } else if (lowerFileName.endsWith(".gif")) {
            return "image/gif";
        } else if (lowerFileName.endsWith(".bmp")) {
            return "image/bmp";
        } else if (lowerFileName.endsWith(".jpg") || lowerFileName.endsWith(".jpeg")) {
            return "image/jpeg";
        }

        throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Unsupported image type"
        );
    }

    //Delete the image after user updates the existing image
    public void deleteImage(String filename) throws IOException {

        // Do nothing if there is no filename
        if (filename == null || filename.isBlank()) {
            return;
        }

        //Creates only the file path
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        //Build the full path to the image
        Path filePath = uploadPath.resolve(filename).normalize();

        //Check if the file path starts with upload directory path
        if (!filePath.startsWith(uploadPath)) {
            throw new IllegalArgumentException("Invalid Image Filename");
        }

        //Deletes the image file if it exists
        Files.deleteIfExists(filePath);

    }


}


