// lib/models/rating_submission.dart
class RatingSubmission {
  final String rideId;
  final int stars; // 1 (worst) to 4 (best)
  final String? comment;

  RatingSubmission({required this.rideId, required this.stars, this.comment}) {
    assert(stars >= 1 && stars <= 4, 'Rating must be between 1 and 4');
  }

  Map<String, dynamic> toJson() => {
    'stars': stars,
    if (comment != null) 'comment': comment,
  };
}
