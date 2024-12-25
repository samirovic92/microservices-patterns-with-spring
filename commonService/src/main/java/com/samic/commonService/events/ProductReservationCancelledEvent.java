package com.samic.commonService.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReservationCancelledEvent {
    private String productId;
    private String orderId;
    private String userId;
    private int quantity;
    private String reason;
}
