package sit.int371.modride_service.beans.files_beans;

import lombok.Data;

@Data
public class UsersFilesBean {
    private Integer file_id;
    private Integer owner_id;
    private String profile_img_name;
    private String download_url;
    
}
