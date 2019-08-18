package sales;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SalesAppTest {

	@InjectMocks
	private SalesDao salesDao = spy(new SalesDao());

	@InjectMocks
	private SalesReportDao salesReportDao = spy(new SalesReportDao());

	@InjectMocks
	private EcmService ecmService = spy(new EcmService());

	@InjectMocks
	private SalesApp salesApp = spy(new SalesApp());

	@Test
	public void test_sales_app_generate_sales_activity_report_when_is_nat_trade_equal_to_true() {

		Sales sales = spy(new Sales());

		SalesReportData salesReportData = spy(new SalesReportData());

		SalesActivityReport salesActivityReport = spy(new SalesActivityReport());
		when(salesActivityReport.toXml()).thenReturn("XML Content");

		doReturn(sales).when(salesDao).getSalesBySalesId(anyString());
		doReturn(false).when(salesApp).isEffectiveTime(any(Sales.class));
		doReturn(salesActivityReport).when(salesApp).generateReport(anyListOf(String.class),  anyListOf(SalesReportData.class));
		doReturn(Arrays.asList(new SalesReportData())).when(salesApp).filterReportData(anyInt(),anyBoolean(),anyListOf(SalesReportData.class),anyListOf(SalesReportData.class));


		salesApp.generateSalesActivityReport("DUMMY", 1000, true, false);


		verify(salesApp).generateReport(eq(Arrays.asList("Sales ID", "Sales Name", "Activity", "Time")), anyListOf(SalesReportData.class));
		verify(salesApp,times(1)).generateReport(anyListOf(String.class),  anyListOf(SalesReportData.class));
		verify(ecmService).uploadDocument("XML Content");
	}

	@Test
	public void test_sales_app_generate_sales_activity_report_when_is_nat_trade_equal_to_false() {

		Sales sales = spy(new Sales());

		SalesReportData salesReportData = spy(new SalesReportData());

		SalesActivityReport salesActivityReport = spy(new SalesActivityReport());
		when(salesActivityReport.toXml()).thenReturn("XML Content");

		doReturn(sales).when(salesDao).getSalesBySalesId(anyString());
		doReturn(false).when(salesApp).isEffectiveTime(any(Sales.class));
		doReturn(salesActivityReport).when(salesApp).generateReport(anyListOf(String.class),  anyListOf(SalesReportData.class));
		doReturn(Arrays.asList(new SalesReportData())).when(salesApp).filterReportData(anyInt(),anyBoolean(),anyListOf(SalesReportData.class),anyListOf(SalesReportData.class));


		salesApp.generateSalesActivityReport("DUMMY", 1000, false, false);


		verify(salesApp).generateReport(eq(Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time")), anyListOf(SalesReportData.class));
		verify(salesApp,times(1)).generateReport(anyListOf(String.class),  anyListOf(SalesReportData.class));
		verify(ecmService).uploadDocument("XML Content");
	}


	@Test
	public void test_filterReportData() {
		Sales sales = mock(Sales.class);
		doReturn(Arrays.asList(new SalesReportData())).when(salesApp).filterReportData(anyInt(),anyBoolean(),anyListOf(SalesReportData.class),anyListOf(SalesReportData.class));

		salesApp.filterReportData(1000,true,Arrays.asList(new SalesReportData()),Arrays.asList(new SalesReportData()));

		verify(salesApp,times(1)).filterReportData(anyInt(),anyBoolean(),anyListOf(SalesReportData.class),anyListOf(SalesReportData.class));
	}

	@Test
	public void test_sales_dao_get_sale_by_sales_id_when_give_a_sales_id() {
		Sales sales = mock(Sales.class);
		when(sales.isSupervisor()).thenReturn(true);
		doReturn(sales).when(salesDao).getSalesBySalesId(anyString());

		Sales sales1 = salesDao.getSalesBySalesId("a1111");

		assertEquals(true,salesDao.getSalesBySalesId("a1111").isSupervisor());
		assertEquals(sales.getEffectiveFrom(),sales1.getEffectiveFrom());
		assertEquals(sales.getEffectiveTo(),sales1.getEffectiveTo());
	}

	@Test
	public void test_sales_dao_get_report_data_by_sales_should_return_sales_activity() {

		Sales sales = mock(Sales.class);
		SalesReportData salesReportData = mock(SalesReportData.class);
		when(salesReportData.getType()).thenReturn("SalesActivity");

		doReturn(Arrays.asList(salesReportData)).when(salesDao).getReportData(sales);

		assertEquals("SalesActivity",salesDao.getReportData(sales).get(0).getType());
	}

	@Test
	public void test_sales_app_should_return_true_when_is_effective_time() {

		Sales sales = mock(Sales.class);
		when(sales.getEffectiveFrom()).thenReturn(new Date(new Date().getTime() - 2 * 24 * 60 * 60 * 1000));
		when(sales.getEffectiveTo()).thenReturn(new Date(new Date().getTime() - 1 * 24 * 60 * 60 * 1000));
		when(sales.isSupervisor()).thenReturn(true);

		boolean result = salesApp.isEffectiveTime(sales);

		assertTrue(salesApp.isEffectiveTime(sales));
	}




	@Test
	public void test_sales_app_should_return_true_when_is_not_effective_time() {

		Sales sales = mock(Sales.class);
		when(sales.getEffectiveFrom()).thenReturn(new Date(new Date().getTime() - 1 * 24 * 60 * 60 * 1000));
		when(sales.getEffectiveTo()).thenReturn(new Date(new Date().getTime() + 1 * 24 * 60 * 60 * 1000));
		when(sales.isSupervisor()).thenReturn(true);

		boolean result = salesApp.isEffectiveTime(sales);

		assertFalse(salesApp.isEffectiveTime(sales));
	}

}
