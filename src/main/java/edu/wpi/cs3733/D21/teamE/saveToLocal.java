package edu.wpi.cs3733.D21.teamE;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;

public class saveToLocal {

	public static void main(String[] args) {

		boolean gotCode = false;

		Webcam webcam = Webcam.getDefault();

		for (int i = 0; i < 1000; i++) {
			WebcamUtils.capture(webcam, "src/main/resources/edu/wpi/TeamE/temp/temp", ImageUtils.FORMAT_PNG);
		}

		while (!gotCode) {
			WebcamUtils.capture(webcam, "src/main/resources/edu/wpi/TeamE/temp/temp", ImageUtils.FORMAT_PNG);
		}

		// save files directly to disk
		// creates temp.png
		WebcamUtils.capture(webcam, "src/main/resources/edu/wpi/TeamE/temp/temp", ImageUtils.FORMAT_PNG);
	}
}