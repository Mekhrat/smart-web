package kz.kaznu.smart.controllers;

import kz.kaznu.smart.models.entities.Photo;
import kz.kaznu.smart.services.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class FileController {

    private final PhotoService photoService;

    @Value("${photos.directory}")
    private String PHOTO_PATH;


    @GetMapping(value = "/photos/{photoName}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody byte[] getPhoto(@PathVariable(name = "photoName", required = true) String photoName) throws IOException {
        if (photoName != null) {
            String path = PHOTO_PATH + photoName + ".jpg";
            File file;
            try {
                file = new File(path);
                return Files.readAllBytes(file.toPath());
            }
            catch (Exception e) {
                file = new File(PHOTO_PATH + "default.png");
                return Files.readAllBytes(file.toPath());
            }
        }
        return null;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/admin/photos")
    public String uploadFile(@RequestParam(name = "photo") MultipartFile photo) {

        if (photo.getContentType().equals("image/jpeg") || photo.getContentType().equals("image/png")) {
            try {
                byte[] bytes = photo.getBytes();
                String fileName = photo.getOriginalFilename().replace(".jpg","").replace(".png","");
                if (new File(PHOTO_PATH + fileName + ".jpg").exists()) {
                    fileName = fileName + new Random().nextInt(1000);
                }
                Path path = Paths.get(PHOTO_PATH + fileName + ".jpg");
                Files.write(path, bytes);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/admin/photos";
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/admin/photos")
    public String photos(Model model) {
        String path = PHOTO_PATH.substring(0, PHOTO_PATH.length() - 1);
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                List<String> photos = Arrays.stream(files).map(File::getName)
                        .map(f -> f.substring(0, f.indexOf(".")))
                        .collect(Collectors.toList());
                model.addAttribute("photos", photos);
            }
        }
        return "admin/photos";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/admin/photos")
    public String delete(@RequestParam(name = "photo") String photo, Model model) {
        String path = PHOTO_PATH + photo + ".jpg";
        File file = new File(path);
        if (file.exists()) {
            List<Photo> photos = photoService.getPhotoByName(photo);
            if (photos.size() > 0)
                photos.forEach(photoService::delete);
            file.delete();
        }
        return "redirect:/admin/photos";
    }
}
