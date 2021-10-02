
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterZxingQr {
  static const MethodChannel _channel =
      const MethodChannel('flutter_zxing_qr');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
