package edu.wpi.cs3733.D21.teamE;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class QRCode {


	public static String scanQR() {
		Webcam webcam = Webcam.getDefault();
		String result = null;
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();

		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		while (result == null) {
			WebcamUtils.capture(webcam, "src/main/resources/edu/wpi/TeamE/temp/temp", ImageUtils.FORMAT_PNG);
			try {
				result = QRCode.readQR("src/main/resources/edu/wpi/TeamE/temp/temp.png", "UTF-8", hintMap);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	// Function to read the QR file
	public static String readQR(String path, String charset, Map hashMap) throws FileNotFoundException, IOException {
		BinaryBitmap binaryBitmap
				= new BinaryBitmap(new HybridBinarizer(
				new BufferedImageLuminanceSource(
						ImageIO.read(
								new FileInputStream(path)))));

		Result result
				= null;
		try {
			result = new MultiFormatReader().decode(binaryBitmap);
		} catch (NotFoundException e) {
			//e.printStackTrace();
			return null;
		}

		return result.getText();
	}

	// Driver code
	public static void main(String[] args) throws IOException {

		// Path where the QR code is saved
		String filePathTrue = "src/main/resources/edu/wpi/TeamE/temp/qrcode_www.geeksforgeeks.org.png";
		String filePathFake = "src/main/resources/edu/wpi/TeamE/temp/FakeQR.png";

		// Encoding charset
		String charset = "UTF-8";

		Map<EncodeHintType, ErrorCorrectionLevel> hintMap
				= new HashMap<EncodeHintType, ErrorCorrectionLevel>();

		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		System.out.println("QRCode output: " + readQR(filePathTrue, charset, hintMap));
		System.out.println("QRCode output: " + readQR(filePathFake, charset, hintMap));
		System.out.println(scanQR());
	}


}
