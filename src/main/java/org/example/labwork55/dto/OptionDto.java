package org.example.labwork55.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionDto {
    private Integer id;
    private String optionText;
    private Boolean isCorrect;
}