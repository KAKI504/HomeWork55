package org.example.labwork55.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    private int id;
    private int questionId;
    private String optionText;
    private boolean isCorrect;
}