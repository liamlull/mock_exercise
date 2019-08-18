package sales;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SalesApp {

	SalesDao salesDao = new SalesDao();
	SalesReportDao salesReportDao = new SalesReportDao();
	EcmService ecmService = new EcmService();

	public void generateSalesActivityReport(String salesId, int maxRow, boolean isNatTrade, boolean isSupervisor) {


		List<String> headers = null;

		List<SalesReportData> filteredReportDataList = new ArrayList<SalesReportData>();

		if (salesId == null) {
			return;
		}
		Sales sales = salesDao.getSalesBySalesId(salesId);
		if (isEffectiveTime(sales)) return;
		List<SalesReportData> reportDataList = salesReportDao.getReportData(sales);

		filterReportData(maxRow, isSupervisor, filteredReportDataList, reportDataList);

		if (isNatTrade) {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Time");
		} else {
			headers = Arrays.asList("Sales ID", "Sales Name", "Activity", "Local Time");
		}

		SalesActivityReport report = this.generateReport(headers, reportDataList);


		ecmService.uploadDocument(report.toXml());

	}

	public List<SalesReportData> filterReportData(int maxRow, boolean isSupervisor, List<SalesReportData> filteredReportDataList, List<SalesReportData> reportDataList) {
		for (SalesReportData data : reportDataList) {
			if ("SalesActivity".equalsIgnoreCase(data.getType())) {
				if (data.isConfidential()) {
					if (isSupervisor) {
						filteredReportDataList.add(data);
					}
				}else {
					filteredReportDataList.add(data);
				}
			}
		}

		List<SalesReportData> tempList = new ArrayList<SalesReportData>();
		for (int i=0; i < reportDataList.size() && i < maxRow; i++) {
			tempList.add(reportDataList.get(i));
		}
		filteredReportDataList = tempList;
		return filteredReportDataList;
	}

	public boolean isEffectiveTime(Sales sales) {
		Date today = new Date();
		if (today.after(sales.getEffectiveTo())
				|| today.before(sales.getEffectiveFrom())){
			return true;
		}
		return false;
	}

	public SalesActivityReport generateReport(List<String> headers, List<SalesReportData> reportDataList) {
		// TODO Auto-generated method stub
		return null;
	}

}
