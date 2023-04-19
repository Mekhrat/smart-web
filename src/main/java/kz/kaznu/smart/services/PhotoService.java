package kz.kaznu.smart.services;

import kz.kaznu.smart.models.entities.Photo;

import java.util.List;
import java.util.Optional;

public interface PhotoService {

    List<Photo> getPhotoByName(String name);

    void delete(Photo photo);
}
