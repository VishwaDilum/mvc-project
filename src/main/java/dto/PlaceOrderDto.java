package dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlaceOrderDto {
    private String code;
    private int qty;

}
