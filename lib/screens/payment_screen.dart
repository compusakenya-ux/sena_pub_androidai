import 'dart:async';
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';
import 'package:provider/provider.dart';
import 'package:sena/services/api_client.dart';
import 'package:sena/services/websocket_service.dart';

enum PaymentState { idle, awaitingPin, confirming, success, failed }

class PaymentScreen extends StatefulWidget {
  final String rideId;
  const PaymentScreen({super.key, required this.rideId});

  @override
  State<PaymentScreen> createState() => _PaymentScreenState();
}

class _PaymentScreenState extends State<PaymentScreen> {
  PaymentState _state = PaymentState.idle;
  Timer? _timeoutTimer;
  late RideSocketService _socketService;

  @override
  void initState() {
    super.initState();
    _socketService = context.read<RideSocketService>(); // Assume provided
    
    // Listen to WebSocket for payment_status frames (SDD 5.1)
    _socketService.stream.listen((frame) {
      if (frame['type'] == 'payment_status') {
        if (frame['status'] == 'completed') {
          _setState(PaymentState.success);
        } else if (frame['status'] == 'failed') {
          _setState(PaymentState.failed);
        }
      }
    });
  }

  void _setState(PaymentState newState) {
    if (mounted) setState(() => _state = newState);
    
    if (newState == PaymentState.confirming) {
      // SDD 8: 60-second failsafe Timer forces 'failed' state
      _timeoutTimer?.cancel();
      _timeoutTimer = Timer(const Duration(seconds: 60), () {
        if (_state == PaymentState.confirming) _setState(PaymentState.failed);
      });
    }
  }

  Future<void> _handleMpesaPayment() async {
    _setState(PaymentState.awaitingPin);
    try {
      final api = context.read<ApiClient>();
      await api.initiateMpesaPayment({'rideId': widget.rideId, 'amount': 150}); // Example amount
      _setState(PaymentState.confirming); // STK push accepted by Daraja
    } catch (e) {
      _setState(PaymentState.failed);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Complete Payment')),
      body: Center(
        child: _buildStateUI(),
      ),
    );
  }

  Widget _buildStateUI() {
    switch (_state) {
      case PaymentState.idle:
        return ElevatedButton(onPressed: _handleMpesaPayment, child: const Text('Pay with M-Pesa'));
      
      case PaymentState.awaitingPin:
        return const CircularProgressIndicator(); // Waiting for Daraja to trigger STK
      
      case PaymentState.confirming:
        return const Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [CircularProgressIndicator(), Text('Confirming payment on your phone...')],
        );
      
      case PaymentState.success:
        return Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.check_circle, color: Colors.green, size: 64),
            const Text('Payment Successful!'),
            ElevatedButton(onPressed: () => context.go('/map'), child: const Text('Done')),
          ],
        );
      
      case PaymentState.failed:
        return Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.error, color: Colors.red, size: 64),
            const Text('Payment Failed'),
            ElevatedButton(onPressed: _handleMpesaPayment, child: const Text('Try Again')),
            TextButton(onPressed: () => context.go('/map'), child: const Text('Pay with Cash instead')),
          ],
        );
    }
  }

  @override
  void dispose() {
    _timeoutTimer?.cancel();
    _socketService.disconnect();
    super.dispose();
  }
}
