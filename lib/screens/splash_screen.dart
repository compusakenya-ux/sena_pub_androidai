// lib/screens/splash_screen.dart
import 'package:flutter/material.dart';
import 'package:go_router/go_router.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  void initState() {
    super.initState();
    _navigateToMap();
  }

  Future<void> _navigateToMap() async {
    await Future.delayed(const Duration(seconds: 2)); // Simulate auth check / loading
    if (mounted) context.go('/map');
  }

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            FlutterLogo(size: 100), // Replace with Sena Logo
            SizedBox(height: 24),
            Text('SENA', style: TextStyle(fontSize: 32, fontWeight: FontWeight.bold, letterSpacing: 4)),
            SizedBox(height: 8),
            Text('Ride Smart. Ride Sena.', style: TextStyle(color: Colors.grey)),
          ],
        ),
      ),
    );
  }
}
