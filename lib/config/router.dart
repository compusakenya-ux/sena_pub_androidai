// lib/config/router.dart
import 'package:go_router/go_router.dart';
import 'package:sena/screens/splash_screen.dart';
import 'package:sena/screens/map_screen.dart';
import 'package:sena/screens/rating_screen.dart';
import 'package:sena/screens/payment_screen.dart';
import 'package:sena/screens/wallet_screen.dart';

final GoRouter appRouter = GoRouter(
  initialLocation: '/',
  routes: [
    GoRoute(path: '/', builder: (context, state) => const SplashScreen()),
    GoRoute(path: '/map', builder: (context, state) => const MapScreen()),
    GoRoute(path: '/ride/:id/rate', builder: (context, state) => RatingScreen(rideId: state.pathParameters['id']!)),
    GoRoute(path: '/ride/:id/pay', builder: (context, state) => PaymentScreen(rideId: state.pathParameters['id']!)),
    GoRoute(path: '/wallet', builder: (context, state) => const WalletScreen()),
  ],
);
