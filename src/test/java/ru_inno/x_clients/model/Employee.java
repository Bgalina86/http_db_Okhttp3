package ru_inno.x_clients.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Employee(String firstName, String lastName, String middleName, int companyId, String EMAIL, String url, String phone,
                       String birthdate, boolean isActive) {
}
