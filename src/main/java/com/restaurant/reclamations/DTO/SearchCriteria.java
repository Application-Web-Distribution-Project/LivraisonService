package com.restaurant.reclamations.DTO;

import com.restaurant.reclamations.Entities.StatusReclamation;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SearchCriteria {
    private String keyword;
    private StatusReclamation status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Override
    public String toString() {
        return "SearchCriteria{" +
                "keyword='" + keyword + '\'' +
                ", status=" + status +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
