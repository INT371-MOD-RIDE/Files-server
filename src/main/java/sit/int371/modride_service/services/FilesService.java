package sit.int371.modride_service.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import sit.int371.modride_service.beans.APIResponseBean;
import sit.int371.modride_service.beans.ErrorsBean;
import sit.int371.modride_service.beans.UsersBean;
import sit.int371.modride_service.beans.files_beans.FilesDataBean;
import sit.int371.modride_service.controllers.BaseController;
import sit.int371.modride_service.repositories.FilesRepository;

@Service
public class FilesService extends BaseController {

    @Value("${uri_userfile_storage}")
    public String uriUserProfile;

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

    public String createAttachmentContent(String userId, MultipartFile file)
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
            String fileName = 'u' + userId; // setCustomfileNameWithUserIdAndFX

            // เพื่อทำ replace file ที่เป็นชื่อเดียวกัน แต่คนละสกุลไฟล์
            // (ลบไฟล์พวกนั้นทั้งหมด)
            // 📝 อนาคต เด่วเขียน function แยกออกไป เพื่อให้ใน method นี้ดู clean กว่านี้
            File targetOriginJpgFile = new File(uriUserProfile + fileName + ".jpg");
            File targetOriginJpegFile = new File(uriUserProfile + fileName + ".jpeg");
            File targetOriginPngFile = new File(uriUserProfile + fileName + ".png");
            if (targetOriginJpgFile.exists() || targetOriginJpegFile.exists() || targetOriginPngFile.exists()) {
                System.out.println("same-name(jpg)-exist???: " + targetOriginJpgFile.exists());
                System.out.println("same-name(jpeg)-exist???: " + targetOriginJpegFile.exists());
                System.out.println("same-name(png)-exist???: " + targetOriginPngFile.exists());
                targetOriginJpgFile.delete();
                targetOriginJpegFile.delete();
                targetOriginPngFile.delete();
            }

            // การ upload file ลงไปยัง dir ที่ต้องการ
            File uploadDir = new File(uriUserProfile);
            fileName = fileName + fileExtension;
            File target = new File(uriUserProfile + fileName);
            if (uploadDir.mkdir()) {
                file.transferTo(target);
                System.out.println("if : " + target);
            } else {
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
        filesDataBean.setUri_files_storage(uriUserProfile);
        // ต้อง get filename ออกมา
        try {
            System.out.println("service ทำงาน?");
            filesDataBean = filesRepository.getUserProfilePicture(filesDataBean);
            if (filesDataBean == null) {
                throw new Exception("files not found !");
            }
            System.out.println("filesdata-bean: " + filesDataBean);
        } catch (Exception e) {
            this.checkException(e, res, errorsBean.getErrGetFile());
        }
        System.out.println(filesDataBean.getFile_name());
        byte[] files = Files.readAllBytes(new File(filesDataBean.getFile_name()).toPath());
        System.out.println("files???: " + files);
        return files;

    }

}
