package parking;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VipParkingStrategyTest {

	@Test
    public void testPark_givenAVipCarAndAFullParkingLog_thenGiveAReceiptWithCarNameAndParkingLotName() {

	    /* Exercise 4, Write a test case on VipParkingStrategy.park()
	    * With using Mockito spy, verify and doReturn */
        Car leoCar = new Car("LeoCarA");
        ParkingLot parkingLot = new ParkingLot("Parking Lot 1",1);

        parkingLot.getParkedCars().add(leoCar);
        List<ParkingLot> parkingLots = Arrays.asList(parkingLot);
        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        doReturn(true).when(spyVipParkingStrategy).isAllowOverPark(leoCar);

        Receipt receipt = spyVipParkingStrategy.park(parkingLots,leoCar);

        assertEquals("LeoCarA",receipt.getCarName());
        assertEquals("Parking Lot 1",receipt.getParkingLotName());


    }

    @Test
    public void testPark_givenCarIsNotVipAndAFullParkingLog_thenGiveNoSpaceReceipt() {

        /* Exercise 4, Write a test case on VipParkingStrategy.park()
         * With using Mockito spy, verify and doReturn */
        Car leoCar = new Car("LeoCar");
        ParkingLot parkingLot = new ParkingLot("Paking Lot 1",1);

        parkingLot.getParkedCars().add(new Car("other Car"));

        List<ParkingLot> parkingLots = Arrays.asList(parkingLot);

        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        doReturn(false).when(spyVipParkingStrategy).isAllowOverPark(leoCar);

        Receipt receipt = spyVipParkingStrategy.park(parkingLots,leoCar);

        verify(spyVipParkingStrategy,times(0)).createReceipt(parkingLot,leoCar);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsVipCar_thenReturnTrue(){

        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */
        Car leoCar = new Car("LeoCarA");

        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        CarDao mCarDao = mock(CarDao.class);
        when(mCarDao.isVip("LeoCarA")).thenReturn(true);
        doReturn(mCarDao).when(spyVipParkingStrategy).getCarDao();

        boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(leoCar);

        assertTrue(allowOverPark);


    }

    @Test
    public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsVipCar_thenReturnFalse(){

        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */
        Car leoCar = new Car("LeoCar");

        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        CarDao mCarDao = mock(CarDao.class);
        when(mCarDao.isVip("LeoCar")).thenReturn(true);
        doReturn(mCarDao).when(spyVipParkingStrategy).getCarDao();

        boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(leoCar);

        assertFalse(allowOverPark);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameContainsCharacterAAndIsNotVipCar_thenReturnFalse(){
        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */
        Car leoCar = new Car("LeoCarA");

        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        CarDao mCarDao = mock(CarDao.class);
        when(mCarDao.isVip("LeoCarA")).thenReturn(false);
        doReturn(mCarDao).when(spyVipParkingStrategy).getCarDao();

        boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(leoCar);

        assertFalse(allowOverPark);
    }

    @Test
    public void testIsAllowOverPark_givenCarNameDoesNotContainsCharacterAAndIsNotVipCar_thenReturnFalse() {
        /* Exercise 5, Write a test case on VipParkingStrategy.isAllowOverPark()
         * You may refactor the code, or try to use
         * use @RunWith(MockitoJUnitRunner.class), @Mock (use Mockito, not JMockit) and @InjectMocks
         */
        Car leoCar = new Car("LeoCar");

        VipParkingStrategy spyVipParkingStrategy = spy(new VipParkingStrategy());

        CarDao mCarDao = mock(CarDao.class);
        when(mCarDao.isVip("LeoCar")).thenReturn(false);
        doReturn(mCarDao).when(spyVipParkingStrategy).getCarDao();

        boolean allowOverPark = spyVipParkingStrategy.isAllowOverPark(leoCar);

        assertFalse(allowOverPark);
    }

    private Car createMockCar(String carName) {
        Car car = mock(Car.class);
        when(car.getName()).thenReturn(carName);
        return car;
    }
}
