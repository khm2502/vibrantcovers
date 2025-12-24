package com.vibrantcovers.dto;

import com.vibrantcovers.entity.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}

