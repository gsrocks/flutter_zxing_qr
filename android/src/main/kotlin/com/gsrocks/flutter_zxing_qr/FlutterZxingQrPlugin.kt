package com.gsrocks.flutter_zxing_qr

import android.util.Log
import androidx.annotation.NonNull
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.Encoder
import com.google.zxing.qrcode.encoder.QRCode

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import java.lang.Exception

/** FlutterZxingQrPlugin */
class FlutterZxingQrPlugin : FlutterPlugin, MethodCallHandler {
    private lateinit var channel: MethodChannel

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_zxing_qr")
        channel.setMethodCallHandler(this)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        } else if (call.method == "generateQr") {
            val contentToEncode: String? = call.argument("content")
            val qrVersion: Int? = call.argument("version")
            val correctionLevelInt: Int? = call.argument("correction")
            val errorCorrectionLevel = when {
                correctionLevelInt == null
                        || correctionLevelInt < 0
                        || correctionLevelInt > 3 -> ErrorCorrectionLevel.M
                else -> ErrorCorrectionLevel.forBits(correctionLevelInt)
            }
            if (contentToEncode != null) {
                try {
                    val qrResult = generateOr(contentToEncode, qrVersion, errorCorrectionLevel)
                    result.success(qrResult)
                } catch (e: Exception) {
                    Log.d("FlutterZxingQrPlugin", "$e")
                }
            }
        } else {
            result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun generateOr(
        content: String,
        qrVersion: Int?,
        ecLevel: ErrorCorrectionLevel
    ): List<ByteArray> {
        val qr: QRCode = if (qrVersion != null) {
            val encodingParams: Map<EncodeHintType, Int> = mapOf(
                EncodeHintType.QR_VERSION to qrVersion
            )
            Encoder.encode(content, ecLevel, encodingParams)
        } else {
            Encoder.encode(content, ecLevel)
        }

        return qr.matrix.array.toList()
    }
}
