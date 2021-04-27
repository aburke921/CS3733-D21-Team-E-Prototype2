package edu.wpi.TeamE;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static edu.wpi.cs3733.D21.teamE.QRCode.readQR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QRCodeTests {

	@Test
	public void testRealQR() {
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		String result = null;
		try {
			result = readQR("src/main/resources/edu/wpi/TeamE/temp/RealQR.png", "UTF-8", hintMap);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertEquals(result, "https://www.geeksforgeeks.org/how-to-generate-and-read-qr-code-with-java-using-zxing-library/");
	}

	@Test
	public void testFakeQR() {
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		String result = null;
		try {
			result = readQR("src/main/resources/edu/wpi/TeamE/temp/FakeQR.png", "UTF-8", hintMap);
		} catch (IOException e) {
			e.printStackTrace();
		}

		assertNull(result);
	}
}
