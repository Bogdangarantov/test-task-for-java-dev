package org.example.testtaskforjavadev.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.testtaskforjavadev.dto.User;
import org.example.testtaskforjavadev.service.UserAggregationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users", description = "User aggregation API")
public class GetUsersController {

    private final UserAggregationService userAggregationService;

    @GetMapping("/users")
    @Operation(
        summary = "Get all users",
        description = "Aggregates users from all configured data sources with optional filtering"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved users")
    public List<User> getUsers(
            @Parameter(description = "Filter by username")
            @RequestParam(required = false) String username,
            @Parameter(description = "Filter by name")
            @RequestParam(required = false) String name,
            @Parameter(description = "Filter by surname")
            @RequestParam(required = false) String surname
    ) {
        return userAggregationService.getAllUsers(username, name, surname);
    }
}
