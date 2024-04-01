package ru.clevertec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO class for <b>Authority</b> with the name field
 *
 * @author Yuryeu Andrei
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorityDto {

    private String name;
}
