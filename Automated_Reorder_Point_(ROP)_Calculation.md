# F/ND/25/3210039

# Feature name
  Dynamic Economic Order Quantity (EOQ) & Reorder Point Calculator
# Description:
  A tool that continuously recalculates stock thresholds based on historical sales velocity and supplier lead times rather than using static minimums.
​# Purpose: 
​  To prevent stockouts of fast-moving items while minimizing excess holding costs for slow-moving goods.
​# How it works: 
 The system tracks daily sales averages and lead-time delays, automatically updating the threshold where a new purchase order must be triggered.
# ​Information Required: 
  Average daily consumption rate, supplier lead time in days, safety stock buffer, and current inventory level.
# ​Output / Action:
  Generates a low-stock alert or auto-creates a purchase order draft when stock hits the newly calculated threshold.
# ​Benefits: 
  Eliminates manual monitoring, reduces human error, and adapts automatically to seasonal buying trends.
# ​Limitations / Dependencies: 
  Relies heavily on accurate, real-time sales logging; sudden spikes in demand can temporarily throw off predictive averages until updated.
# ​Sources: 
  Internal System Logic & Inventory Optimization Principles
