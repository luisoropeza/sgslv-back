package com.example.demo.services.implement;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.dtos.request.RequestDto;
import com.example.demo.dtos.response.RequestResponse;
import com.example.demo.exceptions.AppException;
import com.example.demo.models.Document;
import com.example.demo.models.Request;
import com.example.demo.models.Status;
import com.example.demo.models.User;
import com.example.demo.repositories.jpa.DocumentRepository;
import com.example.demo.repositories.jpa.RequestRepository;
import com.example.demo.repositories.jpa.UserRepository;
import com.example.demo.services.RequestService;
import com.example.demo.services.jwt.JwtUtils;
import com.example.demo.services.mapper.MainMapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
@SuppressWarnings({ "unchecked" })
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final Cloudinary cloudinary;
    private final MainMapper mainMapper;
    private final JwtUtils jwtUtils;

    @Override
    public RequestResponse createRequest(RequestDto dto, MultipartFile multipartFile, String token)
            throws IOException {
        if (requestRepository.existsByStatus(Status.Pending)) {
            throw new AppException("Exists a request pending", HttpStatus.BAD_REQUEST);
        }
        if (dto.getEndDate().isBefore(dto.getInitDate())) {
            throw new AppException("The end date must be after the start date.", HttpStatus.BAD_REQUEST);
        }
        User user = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token)).orElseThrow();
        Map<String, Object> file = null;
        if (multipartFile != null) {
            file = uploadFile(multipartFile);
        }
        Request request = mainMapper.toRequest(dto, user, file);
        requestRepository.save(request);
        return mainMapper.toRequestResponse(request);
    }

    @Override
    public RequestResponse updateRequestById(Long id, RequestDto dto, MultipartFile multipartFile)
            throws Exception {
        if (dto.getEndDate().isBefore(dto.getInitDate())) {
            throw new AppException("The end date must be after the start date.", HttpStatus.BAD_REQUEST);
        }
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new AppException("Request can't be found", HttpStatus.NOT_FOUND));
        ;
        Document document = documentRepository.findByRequest(request).orElse(null);
        Map<String, Object> file = null;
        if (multipartFile != null) {
            if (document != null) {
                deleteFile(document.getName());
                documentRepository.deleteByRequest(request);
            }
            file = uploadFile(multipartFile);
        }
        request = mainMapper.toRequest(request, dto, file);
        requestRepository.save(request);
        return mainMapper.toRequestResponse(request);
    }

    @Override
    public RequestResponse approvedRequest(String token, Long userId, Long requestId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User can't be found", HttpStatus.NOT_FOUND));
        User admin = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token))
                .orElseThrow(() -> new AppException("Admin can't be found", HttpStatus.NOT_FOUND));
        Request request = requestRepository.findByIdAndUser(requestId, user)
                .orElseThrow(() -> new AppException("Request can't be found", HttpStatus.NOT_FOUND));
        if (!user.getTeam().equals(admin.getTeam())) {
            throw new AppException("That admin can't approved that request", HttpStatus.FORBIDDEN);
        }
        request.setStatus(Status.valueOf(status));
        requestRepository.save(request);
        return mainMapper.toRequestResponse(request);
    }

    @Override
    public RequestResponse getRequestByEmployee(Long userId, Long requestId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User can't be found", HttpStatus.NOT_FOUND));
        Request request = requestRepository.findByIdAndUser(requestId, user)
                .orElseThrow(() -> new AppException("Request can't be found", HttpStatus.NOT_FOUND));
        return mainMapper.toRequestResponse(request);
    }

    @Override
    public RequestResponse getRequestById(Long id, String token) {
        User user = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token))
                .orElseThrow(() -> new AppException("User can't be found", HttpStatus.NOT_FOUND));
        Request request = requestRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException("Request can't be found", HttpStatus.NOT_FOUND));
        return mainMapper.toRequestResponse(request);
    }

    @Override
    public List<RequestResponse> getRequests(String token) {
        User user = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token))
                .orElseThrow(() -> new AppException("User can't be found", HttpStatus.NOT_FOUND));
        return requestRepository.findAll().stream().filter(request -> request.getUser().equals(user))
                .map(mainMapper::toRequestResponse).toList();
    }

    @Override
    public List<RequestResponse> getRequestsByPersonal(String token) {
        User user = userRepository.findByUsername(jwtUtils.getUsernameFromToken(token))
                .orElseThrow(() -> new AppException("User can't be found", HttpStatus.NOT_FOUND));
        return requestRepository.findAll().stream()
                .filter(request -> request.getUser().getTeam().equals(user.getTeam()))
                .map(mainMapper::toRequestResponse).toList();
    }

    @Override
    public List<RequestResponse> getRequestsByEmployee(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException("User can't be found", HttpStatus.NOT_FOUND));
        return requestRepository.findAll().stream().filter(request -> request.getUser().equals(user))
                .map(mainMapper::toRequestResponse).toList();
    }

    public Map<String, Object> uploadFile(MultipartFile file) throws IOException {
        Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "", "resource_type", "image"));
        return result;
    }

    public void deleteFile(String fileName) throws Exception {
        cloudinary.api().deleteResources(Arrays.asList(fileName),
                ObjectUtils.asMap("type", "upload", "resource_type", "image"));
    }
}
