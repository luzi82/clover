package clover;

import org.junit.Assert;
import org.junit.Test;

import com.luzi82.adb.Adb;

public class AdbTest {

	@Test
	public void testDeviceEq() {
		Adb.Device ad0 = new Adb.Device();
		Adb.Device ad1 = new Adb.Device();
		Adb.Device ada = new Adb.Device();
		Adb.Device adb = new Adb.Device();

		ad0.serial_number = "123";
		ad0.state = Adb.Device.State.DEVICE;

		ad1.serial_number = "123";
		ad1.state = Adb.Device.State.DEVICE;

		ada.serial_number = "124";
		ada.state = Adb.Device.State.DEVICE;

		adb.serial_number = "123";
		adb.state = Adb.Device.State.NO_DEVICE;

		Assert.assertTrue(ad0.equals(ad1));
		Assert.assertFalse(ad0.equals(ada));
		Assert.assertFalse(ad0.equals(adb));
	}

}
