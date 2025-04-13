package lk.ijse.worksphere.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PublicHolidayDTO {
    private LocalDate date;
    private String localName;
    private String name;
    private String countryCode;
}
