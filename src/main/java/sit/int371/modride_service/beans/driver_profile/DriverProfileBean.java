package sit.int371.modride_service.beans.driver_profile;

import java.util.List;

import lombok.Data;

@Data
public class DriverProfileBean {
    private LicensesBean licenseDetail;
    private List<VehiclesBean> vehicleList;

}
