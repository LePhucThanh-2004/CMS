package vn.edu.ntu.cms.domain.dto;

import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private Set<String> roles;
} 