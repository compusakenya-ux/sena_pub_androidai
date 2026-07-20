// lib/main.dart
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:sena/config/router.dart';
import 'package:sena/config/theme.dart';
import 'package:sena/services/api_client.dart';
import 'package:sena/services/location_service.dart';

void main() {
  // Environment variables injected via --dart-define (See SDD Section 9.1)
  const String apiBaseUrl = String.fromEnvironment('SENA_API_BASE_URL', defaultValue: 'https://api.sena.co.ke');
  const String wsBaseUrl = String.fromEnvironment('SENA_WS_BASE_URL', defaultValue: 'wss://api.sena.co.ke');

  runApp(
    ProviderScope(
      overrides: [
        apiClientProvider.overrideWithValue(ApiClient(baseUrl: apiBaseUrl)),
        locationServiceProvider.overrideWithValue(LocationService()),
        // wsBaseUrlProvider.overrideWithValue(wsBaseUrl),
      ],
      child: const SenaApp(),
    ),
  );
}

class SenaApp extends StatelessWidget {
  const SenaApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp.router(
      title: 'Sena',
      theme: senaThemeData, // Defined in config/theme.dart
      routerConfig: appRouter,
    );
  }
}
