package proyecto2021G03.appettit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDTO {
    Long clientId;
    String tokenWeb;
}
