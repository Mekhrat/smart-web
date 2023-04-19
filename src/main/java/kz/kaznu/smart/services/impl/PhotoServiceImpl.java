package kz.kaznu.smart.services.impl;

import kz.kaznu.smart.models.entities.Photo;
import kz.kaznu.smart.repositories.PhotoRepository;
import kz.kaznu.smart.services.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    @Override
    public List<Photo> getPhotoByName(String name) {
        return photoRepository.findAllByPhotoName(name);
    }

    @Override
    public void delete(Photo photo) {
        photoRepository.delete(photo);
    }
}
