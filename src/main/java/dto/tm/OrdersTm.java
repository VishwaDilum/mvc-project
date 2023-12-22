package dto.tm;

import lombok.*;

import java.util.Date;
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrdersTm {
    private String id;
    private Date date;
    private String customerid;


}
