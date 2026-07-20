// lib/screens/rating_screen.dart
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:sena/models/rating_submission.dart';
import 'package:sena/services/api_client.dart';

class RatingScreen extends StatefulWidget {
  final String rideId;
  const RatingScreen({super.key, required this.rideId});

  @override
  State<RatingScreen> createState() => _RatingScreenState();
}

class _RatingScreenState extends State<RatingScreen> {
  final ApiClient _api = ApiClient(baseUrl: const String.fromEnvironment('SENA_API_BASE_URL', defaultValue: 'https://api.sena.co.ke'));
  int _selectedStars = 0;
  final TextEditingController _commentController = TextEditingController();
  bool _isSubmitting = false;

  Future<void> _submitRating() async {
    if (_selectedStars == 0) return;

    setState(() => _isSubmitting = true);

    // Enforce 1-4 bound via the Model's assert before making the network call
    final submission = RatingSubmission(
      rideId: widget.rideId,
      stars: _selectedStars,
      comment: _commentController.text.isEmpty ? null : _commentController.text,
    );

    try {
      await _api.submitRating(widget.rideId, submission.stars, submission.comment);
    } catch (e) {
      // SDD Section 8: Caught silently; ride still proceeds to payment.
      // We do not show an error dialog to the user.
    } finally {
      // SDD Section 5: Always navigates to payment regardless of outcome (non-blocking)
      if (mounted) {
        context.go('/ride/${widget.rideId}/pay');
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Rate your ride')),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          children: [
            const SizedBox(height: 32),
            const Text('How was your trip?', style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold)),
            const SizedBox(height: 32),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: List.generate(4, (index) {
                final starValue = index + 1;
                return IconButton(
                  iconSize: 48,
                  icon: Icon(
                    Icons.star_rounded,
                    color: starValue <= _selectedStars ? Colors.amber : Colors.grey.shade300,
                  ),
                  onPressed: () => setState(() => _selectedStars = starValue),
                );
              }),
            ),
            const SizedBox(height: 32),
            TextField(
              controller: _commentController,
              decoration: const InputDecoration(
                hintText: 'Leave a comment (optional)',
                border: OutlineInputBorder(),
              ),
              maxLines: 3,
            ),
            const Spacer(),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: _selectedStars > 0 && !_isSubmitting ? _submitRating : null,
                child: _isSubmitting ? const SizedBox(height: 20, width: 20, child: CircularProgressIndicator(strokeWidth: 2, color: Colors.white)) : const Text('Submit & Pay'),
              ),
            ),
            const SizedBox(height: 16),
          ],
        ),
      ),
    );
  }
}
