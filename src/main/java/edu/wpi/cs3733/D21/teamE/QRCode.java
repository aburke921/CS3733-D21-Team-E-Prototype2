package edu.wpi.cs3733.D21.teamE;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;

public class QRCode {

	public static String scanQR() {
		Webcam webcam = Webcam.getDefault();

		webcam.open();
		WebcamPanel panel = new WebcamPanel(webcam);
		panel.setMirrored(false);

		JFrame window = new JFrame("QR Scanner");
		window.add(panel);
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		String result = null;
		while (result == null) {
			WebcamUtils.capture(webcam, "src/main/resources/edu/wpi/cs3733/D21/teamE/QRcode/temp", ImageUtils.FORMAT_PNG);
			result = QRCode.readQR("src/main/resources/edu/wpi/cs3733/D21/teamE/QRcode/temp.png");
		}
		window.setVisible(false);
		window.dispose();
		webcam.close();
		return result;
	}

	public static String readQR(String path){
		BinaryBitmap binaryBitmap = null;
		try {
			binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(path)))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Result result;
		try {
			result = new MultiFormatReader().decode(binaryBitmap);
		} catch (NotFoundException e) {
			//e.printStackTrace();
			return null;
		}
		return result.getText();
	}

	public static void main(String[] args) {
		//System.out.println(scanQR());                                             // comment out to enable debug mode

		String result = "https://anrong.online/emeraldemus/n/EEXIT00101.html";
		//result = QRCode.scanQR();                                             // uncomment to enable camera debug mode
		System.out.println("Scanned String: " + result);
		String pure = result.substring(result.lastIndexOf('/') - 1, result.lastIndexOf('.'));
		System.out.println("Scanned pure: " + pure);
		String lable = result.substring(result.lastIndexOf('/') - 1, result.lastIndexOf('/'));
		System.out.println("Scanned lable: " + lable);
		String code = result.substring(result.lastIndexOf('/') + 1, result.lastIndexOf('.'));
		System.out.println("Scanned code: " + code);
	}
}
