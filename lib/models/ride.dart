// lib/models/ride.dart
class Ride {
  final String rideId;
  final String riderId;
  final String? driverId;
  final String status; // requested | accepted | arrived | started | completed | cancelled
  final double pickupLat;
  final double pickupLng;
  final double dropoffLat;
  final double dropoffLng;
  final int fare;
  final double surgeMultiplier;
  final DateTime requestedAt;
  final DateTime? completedAt;

  Ride({
    required this.rideId, required this.riderId, this.driverId, required this.status,
    required this.pickupLat, required this.pickupLng, required this.dropoffLat, required this.dropoffLng,
    required this.fare, required this.surgeMultiplier, required this.requestedAt, this.completedAt,
  });

  factory Ride.fromJson(Map<String, dynamic> json) {
    return Ride(
      rideId: json['rideId'],
      riderId: json['riderId'],
      driverId: json['driverId'],
      status: json['status'],
      pickupLat: json['pickup']['lat'],
      pickupLng: json['pickup']['lng'],
      dropoffLat: json['dropoff']['lat'],
      dropoffLng: json['dropoff']['lng'],
      fare: json['fare'],
      surgeMultiplier: json['surgeMultiplier'],
      requestedAt: DateTime.parse(json['requestedAt']),
      completedAt: json['completedAt'] != null ? DateTime.parse(json['completedAt']) : null,
    );
  }
}
