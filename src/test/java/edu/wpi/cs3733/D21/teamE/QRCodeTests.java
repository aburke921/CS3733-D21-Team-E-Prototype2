package edu.wpi.cs3733.D21.teamE;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static edu.wpi.cs3733.D21.teamE.QRCode.readQR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QRCodeTests {

	@Test
	public void testRealQR() {
		String result = null;
		result = readQR("src/main/resources/edu/wpi/cs3733/D21/teamE/QRcode/RealQR.png");
		assertEquals(result, "https://anrong.online/emeraldemus/EEXIT00101.html");
	}

	@Test
	public void testFakeQR() {
		String result = null;
		result = readQR("src/main/resources/edu/wpi/cs3733/D21/teamE/QRcode/FakeQR.png");
		assertNull(result);
	}
}
