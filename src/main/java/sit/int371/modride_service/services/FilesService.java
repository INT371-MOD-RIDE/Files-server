package sit.int371.modride_service.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sit.int371.modride_service.beans.APIResponseBean;
import sit.int371.modride_service.beans.ErrorsBean;
import sit.int371.modride_service.beans.UsersBean;
import sit.int371.modride_service.beans.driver_profile.LicensesBean;
import sit.int371.modride_service.beans.driver_profile.VehiclesBean;
import sit.int371.modride_service.beans.files_beans.FilesDataBean;
import sit.int371.modride_service.controllers.BaseController;
import sit.int371.modride_service.repositories.FilesRepository;

@Service
public class FilesService extends BaseController {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final long User_MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

    @Value("${uri_userfile_storage}")
    public String uriUserProfile;
    @Value("${uri_vehicle_storage}")
    public String uriVehicle;
    @Value("${uri_license_storage}")
    public String uriLicense;

    @Autowired
    private FilesRepository filesRepository;

    ErrorsBean errorsBean = new ErrorsBean();

    public boolean checkTypeFileUpload(String filext) throws Exception {
        switch (filext.toLowerCase()) {
            case "jpg":
                return true;
            case "jpeg":
                return true;
            case "png":
                return true;
            default:
                return false;
        }
    }

    public Integer isMoreThanMaxSize(MultipartFile file, String category) throws Exception {
        long fileSize = file.getSize();
        System.out.println("category: "+category);
        // Check if the file size exceeds the limit
        switch (category) {
            case "user":
                if (fileSize > User_MAX_FILE_SIZE) {
                    return 5;
                } else {
                    return 0;
                }

            default:
                if (fileSize > MAX_FILE_SIZE) {
                    return 10;
                } else {
                    return 0;
                }
        }
    }

    public String createAttachmentContent(String id, MultipartFile file, String category)
            throws Exception {
        try {
            System.out.println("origin-filename: " + file.getOriginalFilename());
            // String[] nameSplit = file.getOriginalFilename().split(Pattern.quote("."));
            // System.out.println("nameSplit: "+nameSplit);
            String fileExtension = '.' + file.getOriginalFilename().replaceAll(".*\\.", "");
            // for (int i = 0; i < nameSplit.length; i++) {
            // if (i == (nameSplit.length - 1)) {
            // fileExtension += ".";
            // fileExtension += nameSplit[i];
            // } else {
            // fileExtension += nameSplit[i];
            // }
            // }
            // String path = "";

            // path = attachmentBean.getAttachment_dir();

            // attachmentBean.setAttachment_name(fileName);
            // attachmentBean.setAttachment_path(path + fileName);
            // System.out.println("after-bean: " + attachmentBean);

            System.out.println("files-ext: " + fileExtension);
            String fileName = "";
            File targetOriginJpgFile = new File("");
            File targetOriginJpegFile = new File("");
            File targetOriginPngFile = new File("");
            File target = new File("");
            File uploadDir = new File("");
            switch (category) {
                case "user":
                    fileName = 'u' + id; // setCustomfileNameWithIdAndFX
                    targetOriginJpgFile = new File(uriUserProfile + fileName + ".jpg");
                    targetOriginJpegFile = new File(uriUserProfile + fileName + ".jpeg");
                    targetOriginPngFile = new File(uriUserProfile + fileName + ".png");
                    // set finaltarget ในการ upload-file
                    fileName = fileName + fileExtension;
                    target = new File(uriUserProfile + fileName);
                    uploadDir = new File(uriUserProfile);
                    break;
                case "license":
                    fileName = 'l' + id; // setCustomfileNameWithIdAndFX
                    targetOriginJpgFile = new File(uriLicense + fileName + ".jpg");
                    targetOriginJpegFile = new File(uriLicense + fileName + ".jpeg");
                    targetOriginPngFile = new File(uriLicense + fileName + ".png");
                    // set finaltarget ในการ upload-file
                    fileName = fileName + fileExtension;
                    target = new File(uriLicense + fileName);
                    uploadDir = new File(uriLicense);
                    break;
                case "vehicle":
                    fileName = 'v' + id; // setCustomfileNameWithIdAndFX
                    targetOriginJpgFile = new File(uriVehicle + fileName + ".jpg");
                    targetOriginJpegFile = new File(uriVehicle + fileName + ".jpeg");
                    targetOriginPngFile = new File(uriVehicle + fileName + ".png");
                    // set finaltarget ในการ upload-file
                    fileName = fileName + fileExtension;
                    target = new File(uriVehicle + fileName);
                    uploadDir = new File(uriVehicle);
                    break;
            }

            // เพื่อทำ replace file ที่เป็นชื่อเดียวกัน แต่คนละสกุลไฟล์
            // (ลบไฟล์พวกนั้นทั้งหมด)
            // 📝 อนาคต เด่วเขียน function แยกออกไป เพื่อให้ใน method นี้ดู clean กว่านี้
            if (targetOriginJpgFile.exists() || targetOriginJpegFile.exists() || targetOriginPngFile.exists()) {
                System.out.println("same-name(jpg)-exist???: " + targetOriginJpgFile.exists());
                System.out.println("same-name(jpeg)-exist???: " + targetOriginJpegFile.exists());
                System.out.println("same-name(png)-exist???: " + targetOriginPngFile.exists());
                targetOriginJpgFile.delete();
                targetOriginJpegFile.delete();
                targetOriginPngFile.delete();
            }

            System.out.println("uploadDir: " + uploadDir);
            System.out.println("target: " + target);

            // การ upload file ลงไปยัง dir ที่ต้องการ
            if (uploadDir.mkdir()) {
                System.out.println("uploadDir.mkdir()");
                file.transferTo(target);
                System.out.println("if : " + target);
            } else {
                System.out.println("not uploadDir.mkdir()");
                file.transferTo(target);
                System.out.println("else : " + target);
            }

            return fileName;

        } catch (Exception e) {
            System.out.println("Got an exception. {}" + e.getMessage());
            throw e;
        }
    }

    public byte[] downloadFileFromFileSystem(String fileName) throws IOException {
        APIResponseBean res = new APIResponseBean();
        FilesDataBean filesDataBean = new FilesDataBean();
        filesDataBean.setFile_name(fileName);
        char firstLetterFN = getFirstLetter(fileName);
        System.out.println("first-letter-file: " + firstLetterFN);
        switch (firstLetterFN) {
            case 'u':
                filesDataBean.setUri_files_storage(uriUserProfile);
                break;
            case 'l':
                filesDataBean.setUri_files_storage(uriLicense);
                break;
            case 'v':
                filesDataBean.setUri_files_storage(uriVehicle);
                break;
        }
        // ต้อง get filename ออกมา
        try {
            switch (firstLetterFN) {
                case 'u':
                    filesDataBean = filesRepository.getUserProfilePicture(filesDataBean);
                    break;
                case 'l':
                    filesDataBean = filesRepository.getLicensePicture(filesDataBean);
                    break;
                case 'v':
                    filesDataBean = filesRepository.getVehiclePicture(filesDataBean);
                    break;
            }
            if (filesDataBean == null) {
                throw new Exception("files not found !");
            }
            System.out.println("filesdata-bean: " + filesDataBean);
        } catch (Exception e) {
            this.checkException(e, res, errorsBean.getErrGetFile());
        }
        System.out.println(filesDataBean.getFile_name());
        byte[] files = Files.readAllBytes(new File(filesDataBean.getFile_name()).toPath());
        return files;

    }

    private static char getFirstLetter(String text) {
        // Check if the text is not empty
        if (text != null && !text.isEmpty()) {
            // Get the first character
            return text.charAt(0);
        } else {
            // Return a default value or handle the case where the text is empty
            throw new IllegalArgumentException("Text cannot be null or empty");
        }
    }

    public void deleteFiles(String fileName, String category)
            throws Exception {
        try {
            File targetFile = new File("");
            switch (category) {
                case "user":

                    break;
                case "license":
                    targetFile = new File(uriLicense + fileName);

                    break;
                case "vehicle":
                    targetFile = new File(uriVehicle + fileName);
                    System.out.println("targetFile: " + targetFile);

                    break;
            }

            if (targetFile.exists()) {
                System.out.println("exist!!!");
                targetFile.delete();
            }

        } catch (Exception e) {
            System.out.println("Got an exception. {}" + e.getMessage());
            throw e;
        }
    }

    public boolean checkRelateWithEvent(String deleteType, Integer id)
            throws Exception {
        try {
            switch (deleteType) {
                case "license":
                    List<LicensesBean> licensesBeans = filesRepository
                            .checkEventWithLicenseId(id);
                    if (licensesBeans.isEmpty()) {
                        return true;
                    } else {
                        return false;
                    }

                case "vehicle":
                    List<VehiclesBean> vehiclesBeans = filesRepository
                            .checkEventWithVehicleId(id);
                    if (vehiclesBeans.isEmpty()) {
                        return true;
                    } else {
                        return false;
                    }
            }

        } catch (Exception e) {
            System.out.println("Got an exception. {}" + e.getMessage());
            throw e;
        }

        return true;
    }

}
