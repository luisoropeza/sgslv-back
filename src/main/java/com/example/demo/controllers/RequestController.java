package com.example.demo.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.request.RequestDto;
import com.example.demo.dtos.response.RequestResponse;
import com.example.demo.services.RequestService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class RequestController {

    private final RequestService requestService;

    // employee
    @PostMapping("/requests")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<RequestResponse> createRequest(@ModelAttribute @Valid RequestDto dto,
            @RequestPart("file") @Nullable MultipartFile multipartFile,
            @RequestHeader("Authorization") String authorizationHeader) throws IOException {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(requestService.createRequest(dto, multipartFile, token));
    }

    // employee
    @PutMapping("/request/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<RequestResponse> updateRequestById(@PathVariable("id") Long id,
            @ModelAttribute RequestDto dto, @RequestPart("file") @Nullable MultipartFile multipartFile)
            throws Exception {
        return ResponseEntity.ok(requestService.updateRequestById(id, dto, multipartFile));
    }

    // employee
    @GetMapping("/request/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<RequestResponse> getRequestById(@PathVariable("id") Long id,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(requestService.getRequestById(id, token));
    }

    // employee
    @GetMapping("/requests")
    @PreAuthorize("hasAnyRole('EMPLOYEE')")
    public ResponseEntity<List<RequestResponse>> getRequests(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(requestService.getRequests(token));
    }

    // personal
    @PatchMapping("/user/{userId}/request/{requestId}")
    @PreAuthorize("hasAnyRole('PERSONAL')")
    public ResponseEntity<RequestResponse> approvedRequest(@PathVariable("userId") Long userId,
            @PathVariable("requestId") Long requestId,
            @RequestBody Map<String, String> status,
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok().body(requestService.approvedRequest(token, userId, requestId, status.get("status")));
    }

    // personal
    @GetMapping("/user/{userId}/request/{requestId}")
    @PreAuthorize("hasAnyRole('PERSONAL',)")
    public ResponseEntity<RequestResponse> getRequestByEmployee(@PathVariable("userId") Long userId,
            @PathVariable("requestId") Long requestId) {
        return ResponseEntity.ok().body(requestService.getRequestByEmployee(userId, requestId));
    }

    // personal
    @GetMapping("/user/requests")
    @PreAuthorize("hasAnyRole('PERSONAL')")
    public ResponseEntity<List<RequestResponse>> getRequestsByPersonal(
            @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return ResponseEntity.ok(requestService.getRequestsByPersonal(token));
    }

    // personal
    @GetMapping("/user/{id}/requests")
    @PreAuthorize("hasAnyRole('PERSONAL')")
    public ResponseEntity<List<RequestResponse>> getRequestsByEmployee(@PathVariable("id") Long id) {
        return ResponseEntity.ok(requestService.getRequestsByEmployee(id));
    }
}
