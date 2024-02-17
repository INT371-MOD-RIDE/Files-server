package sit.int371.modride_service.beans.driver_profile;

import lombok.Data;

@Data
public class LicensesBean {
    private Integer license_id;
    private Integer user_id;
    private String license_fn;
    private String license_ln;
    private String license_file_name;
    private String license_download;
    private Float license_size;
    // license-app-status
    private Integer approval_status;
    private String denied_detail;
    private String timestamp;
    private String category = "license";

    
}
