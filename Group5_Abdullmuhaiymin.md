#Features
1. Stock reservation(prevents overselling)
- Check availability before confirming an order
- Reserve stock on confirmation, deduct on shipment, release on cancellation
- Throw `InsufficientStockException` if stock is too low

2. Order status tracking(prevents lost or invalid orders)
- `OrderStatus` enum: PENDING → CONFIRMED → PICKED → PACKED → SHIPPED → DELIVERED (plus CANCELLED)
- Reject invalid jumps with `InvalidStatusTransitionException`
- Log every change with a timestamp

