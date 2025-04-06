package org.example.labwork55.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    private int id;
    private String title;
    private String description;
    private String createdBy;
}