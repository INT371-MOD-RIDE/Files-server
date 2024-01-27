package sit.int371.modride_service.beans.files_beans;

import lombok.Data;

@Data
public class FilesDataBean {
    private String id; // เพราะ get-id มาจากจากการส่งผ่าน form-data (ส่งได้แค่ string เลยต้องรับเป็น String)
    private String file_name;
    private String download_url;
    private String uri_files_storage;
}
