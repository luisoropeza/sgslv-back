package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.request.RequestDto;
import com.example.demo.dtos.response.RequestResponse;

public interface RequestService {
    public RequestResponse createRequest(RequestDto dto, MultipartFile multipartFile, String token) throws IOException;

    public RequestResponse updateRequestById(Long id, RequestDto dto, MultipartFile multipartFile) throws Exception;

    public RequestResponse approvedRequest(String token, Long userId, Long requestId, String status);

    public RequestResponse getRequestByEmployee(Long userId, Long requestId);

    public RequestResponse getRequestById(Long id, String token);

    public List<RequestResponse> getRequests(String token);

    public List<RequestResponse> getRequestsByPersonal(String token);

    public List<RequestResponse> getRequestsByEmployee(Long id);
}
