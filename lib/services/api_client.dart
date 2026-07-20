// lib/services/api_client.dart
import 'package:dio/dio.dart';

class ApiClient {
  late final Dio _dio;

  ApiClient({required String baseUrl}) {
    _dio = Dio(BaseOptions(
      baseUrl: baseUrl,
      connectTimeout: const Duration(seconds: 10),
      receiveTimeout: const Duration(seconds: 10),
    ));

    // Interceptor to attach auth token (SDD 4.1)
    _dio.interceptors.add(InterceptorsWrapper(
      onRequest: (options, handler) {
        // TODO: Retrieve token from secure storage and attach
        // options.headers['Authorization'] = 'Bearer $token';
        handler.next(options);
      },
    ));
  }

  Future<Response> getFareEstimate(Map<String, dynamic> payload) => _dio.post('/rides/estimate', data: payload);
  Future<Response> requestRide(Map<String, dynamic> payload) => _dio.post('/rides/request', data: payload);
  Future<Response> submitRating(String id, int stars, String? comment) => 
      _dio.post('/rides/$id/rating', data: {'stars': stars, 'comment': comment});
  
  Future<Response> initiateMpesaPayment(Map<String, dynamic> payload) => _dio.post('/payments/mpesa/stkpush', data: payload);
  // ... other endpoints from SDD 4.1
}
