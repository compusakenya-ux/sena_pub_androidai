// lib/services/websocket_service.dart
import 'dart:convert';
import 'package:web_socket_channel/web_socket_channel.dart';

class RideSocketService {
  WebSocketChannel? _channel;
  
  void connect(String rideId, String wsBaseUrl, String token) {
    final uri = Uri.parse('$wsBaseUrl/ws/location/$rideId?token=$token');
    _channel = WebSocketChannel.connect(uri);
  }

  Stream<Map<String, dynamic>> get stream {
    if (_channel == null) return const Stream.empty();
    
    return _channel!.stream.map((message) {
      try {
        return jsonDecode(message) as Map<String, dynamic>;
      } catch (e) {
        // SDD 8: Malformed frames are dropped, not thrown
        return {'type': 'error', 'message': 'Malformed frame dropped'}; 
      }
    }).where((frame) => frame['type'] != 'error');
  }

  void disconnect() {
    _channel?.sink.close();
  }
}
