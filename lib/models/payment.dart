// lib/models/payment.dart
class Payment {
  final String paymentId;
  final String rideId;
  final int amount;
  final int driverAmount; // 85% share
  final int platformAmount; // 15% share
  final String method; // 'mpesa' | 'cash'
  final String status; // 'pending' | 'completed' | 'failed'
  final String? mpesaReceiptNumber;

  Payment({
    required this.paymentId,
    required this.rideId,
    required this.amount,
    required this.driverAmount,
    required this.platformAmount,
    required this.method,
    required this.status,
    this.mpesaReceiptNumber,
  });

  factory Payment.fromJson(Map<String, dynamic> json) {
    return Payment(
      paymentId: json['paymentId'],
      rideId: json['rideId'],
      amount: json['amount'],
      driverAmount: json['driverAmount'],
      platformAmount: json['platformAmount'],
      method: json['method'],
      status: json['status'],
      mpesaReceiptNumber: json['mpesaReceiptNumber'],
    );
  }
}
