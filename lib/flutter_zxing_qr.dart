import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_zxing_qr/error_correction_level.dart';

class FlutterZxingQr {
  static const MethodChannel _channel = const MethodChannel('flutter_zxing_qr');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<List<List<int>>> generateQr(String content, int qrVersion,
      ErrorCorrectionLevel correctionLevel) async {
    final List<Object?> result =
        await _channel.invokeMethod('generateQr', {
          'content': content,
          'version': qrVersion,
          'correction': correctionLevel.index
        });
    List<List<int>> qrRaw = result.map((e) => e as List<int>).toList();
    return qrRaw;
  }
}
