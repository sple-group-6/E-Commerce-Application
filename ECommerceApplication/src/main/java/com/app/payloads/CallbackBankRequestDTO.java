package com.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallbackBankRequestDTO {

    private Long paymentId;
    private Long paymentBankId;
    private Double value;

}
