// lib/screens/wallet_screen.dart
import 'package:flutter/material.dart';
import 'package:sena/services/api_client.dart';

class WalletScreen extends StatefulWidget {
  const WalletScreen({super.key});

  @override
  State<WalletScreen> createState() => _WalletScreenState();
}

class _WalletScreenState extends State<WalletScreen> {
  final ApiClient _api = ApiClient(baseUrl: const String.fromEnvironment('SENA_API_BASE_URL', defaultValue: 'https://api.sena.co.ke'));
  int _balance = 0;
  bool _isLoading = true;
  final TextEditingController _amountController = TextEditingController();
  String? _validationError;

  @override
  void initState() {
    super.initState();
    _fetchBalance();
  }

  Future<void> _fetchBalance() async {
    try {
      final response = await _api.getWalletBalance();
      if (mounted) {
        setState(() {
          _balance = response.data['balance'] ?? 0;
          _isLoading = false;
        });
      }
    } catch (e) {
      if (mounted) setState(() => _isLoading = false);
    }
  }

  void _validateAndWithdraw() {
    final amountText = _amountController.text.trim();
    final amount = int.tryParse(amountText);

    // SDD Section 5 & 8: Validates withdrawal amount against balance client-side
    if (amount == null || amount <= 0) {
      setState(() => _validationError = 'Please enter a valid amount.');
      return;
    }
    if (amount > _balance) {
      setState(() => _validationError = 'Insufficient balance.');
      return;
    }

    setState(() => _validationError = null);
    
    // Proceed with API call (Server-side will also verify as source of truth)
    _api.requestWithdrawal(amount).then((_) {
      ScaffoldMessenger.of(context).showSnackBar(const SnackBar(content: Text('Withdrawal requested')));
      _amountController.clear();
      _fetchBalance();
    }).catchError((e) {
      ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Error: $e')));
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('My Wallet')),
      body: Padding(
        padding: const EdgeInsets.all(24.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text('Available Balance', style: TextStyle(color: Colors.grey)),
            const SizedBox(height: 8),
            _isLoading
                ? const CircularProgressIndicator()
                : Text('KES $_balance', style: const TextStyle(fontSize: 36, fontWeight: FontWeight.bold)),
            const SizedBox(height: 40),
            const Text('Withdraw to M-Pesa', style: TextStyle(fontSize: 18, fontWeight: FontWeight.w600)),
            const SizedBox(height: 16),
            TextField(
              controller: _amountController,
              keyboardType: TextInputType.number,
              decoration: InputDecoration(
                prefixText: 'KES ',
                hintText: 'Enter amount',
                border: const OutlineInputBorder(),
                errorText: _validationError,
              ),
              onChanged: (_) => setState(() => _validationError = null), // Clear error on type
            ),
            const SizedBox(height: 24),
            SizedBox(
              width: double.infinity,
              child: ElevatedButton(
                onPressed: _validateAndWithdraw,
                child: const Text('Withdraw'),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
