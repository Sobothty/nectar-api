package istad.co.nectarapi.base;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasedError<T> {
    // Request entity too large, bad request,...
    // For some company can custom this status code
    private String code;

    // Detail error for user
    private T description;
}
