// lib/config/theme.dart
import 'package:flutter/material.dart';

const Color senaPrimary = Color(0xFF00A859); // Kenyan Green inspired
const Color senaSecondary = Color(0xFF000000);
const Color senaBackground = Color(0xFFF8F9FA);
const Color senaError = Color(0xFFD32F2F);

final ThemeData senaThemeData = ThemeData(
  primaryColor: senaPrimary,
  scaffoldBackgroundColor: senaBackground,
  colorScheme: ColorScheme.fromSeed(
    seedColor: senaPrimary,
    primary: senaPrimary,
    secondary: senaSecondary,
    error: senaError,
  ),
  appBarTheme: const AppBarTheme(
    backgroundColor: Colors.white,
    foregroundColor: Colors.black,
    elevation: 0,
    centerTitle: true,
  ),
  elevatedButtonTheme: ElevatedButtonThemeData(
    style: ElevatedButton.styleFrom(
      backgroundColor: senaPrimary,
      foregroundColor: Colors.white,
      padding: const EdgeInsets.symmetric(horizontal: 32, vertical: 16),
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
    ),
  ),
);
