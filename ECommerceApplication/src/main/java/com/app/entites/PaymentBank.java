package com.app.entites;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "payment_bank")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    private Double value;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentBankStatus status;

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;

}
