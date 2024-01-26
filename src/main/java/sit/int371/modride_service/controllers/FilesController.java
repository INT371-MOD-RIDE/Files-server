package sit.int371.modride_service.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import sit.int371.modride_service.beans.APIResponseBean;
import sit.int371.modride_service.beans.UsersBean;
import sit.int371.modride_service.entities.File;
import sit.int371.modride_service.payload.Response;
import sit.int371.modride_service.repositories.FileRepository;
import sit.int371.modride_service.services.DBFileService;
import sit.int371.modride_service.services.FilesService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api_files/v1/files")
public class FilesController extends BaseController {
    @Autowired
    private FilesService filesService;

    @PostMapping(value = "/attachflie", consumes = { "multipart/form-data" })
    @ResponseBody
    public APIResponseBean createAttachment(
            HttpServletRequest request, @RequestPart String userId,
            @RequestParam(required = false) MultipartFile file) {
        APIResponseBean res = new APIResponseBean();

        System.out.println("user-id: "+userId);
        logger.info("file-upload" + file);

        try {
            System.out.println("file-data: " + file);
            String[] nameSplit = file.getOriginalFilename().split(Pattern.quote("."));
            String nameExtension = nameSplit[nameSplit.length - 1];
            if (filesService.checkTypeFileUpload(nameExtension)) {
                filesService.createAttachmentContent(userId,file);
                // res.setData(attachmentBean);
            } else {
                throw new Exception("Invalid File Type Upload");
            }

        } catch (Exception e) {
            this.checkException(e, res);
        }
        return res;
    }

    // ⚠️ code เก่า...แต่เก็บไว้ก่อน
    // ---------------------------------------------------------------------
    // @Autowired
    // private DBFileService dbFileService;
    // @Autowired
    // private FileRepository fileRepository;

    // @PostMapping("/upload")
    // @ResponseStatus(HttpStatus.CREATED)
    // public Response uploadFile(@RequestParam("file") MultipartFile file,
    // @RequestParam("eventStartTime") String eventStartTime,
    // HttpServletRequest request) throws Exception {
    // System.out.println("\n--------\nการทำงานของ post-file\n--------");

    // // guest (ไม่มี token)
    // // if (request.getHeader("Authorization") == null) {
    // // System.out.println("this is [guest] user : guest cannot upload-file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot
    // // upload-file");
    // // }
    // // else:condition ตัวนี้สำหรับทำงานกรณีมี token
    // // else {
    // //// decode token เพื่อเช็ค algorithm
    // // final String authorizationHeader = request.getHeader("Authorization");
    // // String token = authorizationHeader.substring(7);
    // // DecodedJWT tokenDecoded = JWT.decode(token);
    // // System.out.println(tokenDecoded.getAlgorithm());
    // //// token from azure & oasip
    // //// alg : azure
    // // if (tokenDecoded.getAlgorithm().contains("RS256")) {
    // // System.out.println("this is token from azure");
    // // if (tokenDecoded.getClaims().get("roles") == null) {
    // // System.out.println("this is [guest] user : guest cannot upload-file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot
    // // upload-file");
    // // } else if
    // // (tokenDecoded.getClaims().get("roles").toString().contains(lecturer)) {
    // // System.out.println("role: lecturer");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Lecturer not
    // have
    // // permission to upload-file");
    // // }
    // // }
    // //// alg : oasip
    // // else if (tokenDecoded.getAlgorithm().contains("HS512")) {
    // // System.out.println("token from oasip");
    // // String role =
    // //
    // SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    // // if (role.contains(lecturer)) {
    // // System.out.println("role: " + role);
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Lecturer not
    // have
    // // permission to upload-file");
    // // }
    // // }
    // //
    // //// หาก เงื่อนไขก่อนหน้า ไม่พบว่าเป็น role: lecturer ก็จะสามารถเข้าถึง
    // feature
    // // นี้ได้
    // // if (tokenDecoded.getAlgorithm().contains("RS256") ||
    // // tokenDecoded.getAlgorithm().contains("HS512")) {
    // //
    // System.out.println("size of file: " + file.getSize());
    // File fileName = dbFileService.storeFile(file, eventStartTime);

    // String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
    // .path("/api/files/download/")
    // .path(fileName.getId())
    // .toUriString();

    // return new Response(fileName.getFileName(), fileDownloadUri,
    // file.getContentType(), file.getSize());
    // // }
    // // }
    // // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "something went
    // // wrong");
    // }

    // // ---------------------------------------------------------------

    // // getFileById
    // @GetMapping("/{bookingId}")
    // public File getFileByBookingId(@PathVariable Integer bookingId,
    // HttpServletRequest request) {
    // System.out.println("\n--------\nการทำงานของ
    // get-file-by-bookingId\n--------");
    // // guest (ไม่มี token)
    // if (request.getHeader("Authorization") == null) {
    // System.out.println("this is [guest] user : guest cannot get file by
    // bookingid");
    // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot get
    // file by bookingid");
    // } else {
    // final String authorizationHeader = request.getHeader("Authorization");
    // String token = authorizationHeader.substring(7);
    // DecodedJWT tokenDecoded = JWT.decode(token);
    // System.out.println(tokenDecoded.getAlgorithm());
    // // alg : azure สำหรับดัก กรณี ไม่มี role ใน token จะถือว่าเป็น guest
    // if (tokenDecoded.getAlgorithm().contains("RS256")) {
    // System.out.println("this is token from azure");
    // if (tokenDecoded.getClaims().get("roles") == null) {
    // System.out.println("this is [guest] user : guest cannot get file by
    // bookingid");
    // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot get
    // file by bookingid");
    // }
    // }
    // // Load file as Resource
    // File file = fileRepository.getFileByBookingId(bookingId);
    // if (file == null) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    // }
    // return fileRepository.getFileByBookingId(bookingId);
    // }
    // }

    // // download file เราจะ download โดยใช้ bookingId
    // @GetMapping("/download/{fileId:.+}")
    // public ResponseEntity<Resource> downloadFile(@PathVariable String fileId,
    // HttpServletRequest request) {
    // System.out.println("\n--------\nการทำงานของ download-file\n--------");
    // // guest (ไม่มี token)
    // // if (request.getHeader("Authorization") == null) {
    // // System.out.println("this is [guest] user : guest cannot download file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot
    // // download file");
    // // } else {
    // // final String authorizationHeader = request.getHeader("Authorization");
    // // String token = authorizationHeader.substring(7);
    // // DecodedJWT tokenDecoded = JWT.decode(token);
    // // System.out.println(tokenDecoded.getAlgorithm());
    // //// alg : azure สำหรับดัก กรณี ไม่มี role ใน token จะถือว่าเป็น guest
    // // if (tokenDecoded.getAlgorithm().contains("RS256")) {
    // // System.out.println("this is token from azure");
    // // if (tokenDecoded.getClaims().get("roles") == null) {
    // // System.out.println("this is [guest] user : guest cannot download file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot
    // // download file");
    // // }
    // // }

    // // Load file as Resource
    // File file = dbFileService.getFileById(fileId);
    // return ResponseEntity.ok()
    // .contentType(MediaType.parseMediaType(file.getFileType()))
    // .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
    // file.getFileName() + "\"")
    // .body(new ByteArrayResource(file.getData()));
    // // }
    // }

    // // ---------------------------------------------------------------
    // // Patch
    // @PatchMapping("/update/{bookingId}")
    // public void updateFile(@PathVariable Integer bookingId, @RequestParam("file")
    // MultipartFile file,
    // HttpServletRequest request) throws Exception {
    // System.out.println("\n--------\nการทำงานของ edit-file\n--------");
    // // guest (ไม่มี token)
    // // if (request.getHeader("Authorization") == null) {
    // // System.out.println("this is [guest] user : guest cannot get edit file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot
    // edit
    // // file");
    // // }
    // // else:condition ตัวนี้สำหรับทำงานกรณีมี token
    // // else {
    // //// decode token เพื่อเช็ค algorithm
    // // final String authorizationHeader = request.getHeader("Authorization");
    // // String token = authorizationHeader.substring(7);
    // // DecodedJWT tokenDecoded = JWT.decode(token);
    // // System.out.println(tokenDecoded.getAlgorithm());
    // // กรองว่าเจอไฟล์ที่จะ edit ไหม
    // File getFile = dbFileService.getFileByBookingId(bookingId);
    // if (getFile == null) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
    // }
    // dbFileService.updateFile(bookingId, file);

    // // alg : azure
    // // if (tokenDecoded.getAlgorithm().contains("RS256")) {
    // // System.out.println("token from azure");
    // // if (tokenDecoded.getClaims().get("roles").toString().contains(admin)) {
    // // System.out.println("admin role");
    // // dbFileService.updateFile(bookingId, file);
    // // } else if
    // // (tokenDecoded.getClaims().get("roles").toString().contains(student)) {
    // // System.out.println("student role");
    // // String msEmail =
    // // tokenDecoded.getClaims().get("preferred_username").toString();
    // //// เช็คก่อนว่า student คนนี้เป็นเจ้าของ event ทีเชื่อมกับ file นี้หรือไม่
    // // List<File> checkData =
    // // fileRepository.getDataByEmailAndBookingIdWithFile(msEmail, bookingId);
    // // if (checkData.isEmpty()) {
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This student
    // // cannot edit file of other user");
    // // } else {
    // // dbFileService.updateFile(bookingId, file);
    // // }
    // // }
    // //// guest เมื่อ ไม่พบ role ใน token
    // // else if (tokenDecoded.getClaims().get("roles") == null) {
    // // System.out.println("this is [guest] user : guest cannot get edit file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot
    // edit
    // // file");
    // // } else if
    // // (tokenDecoded.getClaims().get("roles").toString().contains(lecturer)) {
    // // System.out.println("lecturer cannot update file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "lecturer
    // cannot
    // // edit file");
    // // }
    // // }
    // //// token of oasip
    // // else if (tokenDecoded.getAlgorithm().contains("HS512")) {
    // // System.out.println("token from oasip");
    // // String role =
    // //
    // SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    // // if (role.contains(lecturer)) {
    // // System.out.println("lecturer cannot update file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "lecturer
    // cannot
    // // edit file");
    // // } else if (role.contains(admin)) {
    // // System.out.println("admin role");
    // // dbFileService.updateFile(bookingId, file);
    // // } else if (role.contains(student)) {
    // // System.out.println("student role");
    // // String email =
    // // SecurityContextHolder.getContext().getAuthentication().getName();
    // // //เช็คก่อนว่า student คนนี้เป็นเจ้าของ event ทีเชื่อมกับ file นี้หรือไม่
    // // List<File> checkData =
    // // fileRepository.getDataByEmailAndBookingIdWithFile(email, bookingId);
    // // if (checkData.isEmpty()) {
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This student
    // // cannot edit file of other user");
    // // } else {
    // // dbFileService.updateFile(bookingId, file);
    // // }
    // // }
    // // }
    // // }
    // // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "something went
    // // wrong");
    // }

    // // ---------------------------------------------------------------

    // // Delete
    // @DeleteMapping("/delete/{fileId}")
    // public void deleteFile(@PathVariable String fileId, HttpServletRequest
    // request) {
    // System.out.println("\n--------\nการทำงานของ delete-file \n--------");
    // // if (request.getHeader("Authorization") == null) {
    // // System.out.println("this is [guest] user : guest cannot delete-file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot
    // // delete-file");
    // // } else {
    // // final String authorizationHeader = request.getHeader("Authorization");
    // // String token = authorizationHeader.substring(7);
    // // DecodedJWT tokenDecoded = JWT.decode(token);
    // // System.out.println(tokenDecoded.getAlgorithm());
    // // กรองว่า fileId นี้ มีอยู่ไหม
    // fileRepository.findById(fileId)
    // .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, fileId +
    // " does not exist !"));

    // // alg : azure
    // // if (tokenDecoded.getAlgorithm().contains("RS256")) {
    // // System.out.println("token from azure");
    // // if (tokenDecoded.getClaims().get("roles").toString().contains(admin)) {
    // // System.out.println("admin role");
    // fileRepository.deleteById(fileId);
    // // } else if
    // // (tokenDecoded.getClaims().get("roles").toString().contains(student)) {
    // // System.out.println("student role");
    // // String msEmail =
    // // tokenDecoded.getClaims().get("preferred_username").toString();
    // // เช็คก่อนว่า student คนนี้เป็นเจ้าของ event ทีเชื่อมกับ file นี้หรือไม่
    // // List<File> checkData = fileRepository.getDataByEmailAndFileId(msEmail,
    // // fileId);
    // // if (checkData.isEmpty()) {
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This student
    // // cannot delete file of other user");
    // // } else {
    // // fileRepository.deleteById(fileId);
    // // }
    // // }
    // //// guest เมื่อไม่พบ role ใน token
    // // else if (tokenDecoded.getClaims().get("roles") == null) {
    // // System.out.println("this is [guest] user : guest cannot delete-file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot
    // // delete-file");
    // // } else if
    // // (tokenDecoded.getClaims().get("roles").toString().contains(lecturer)) {
    // // System.out.println("lecturer cannot delete file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "lecturer
    // cannot
    // // delete file");
    // // }
    // // }
    // //// token of oasip
    // // else if (tokenDecoded.getAlgorithm().contains("HS512")) {
    // // System.out.println("token from oasip");
    // // String role =
    // //
    // SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    // // String email =
    // // SecurityContextHolder.getContext().getAuthentication().getName();
    // // if (role.contains(admin)) {
    // // System.out.println("admin role");
    // // fileRepository.deleteById(fileId);
    // // } else if (role.contains(student)) {
    // // System.out.println("student role");
    // // //เช็คก่อนว่า student คนนี้เป็นเจ้าของ event ทีเชื่อมกับ file นี้หรือไม่
    // // List<File> checkData = fileRepository.getDataByEmailAndFileId(email,
    // fileId);
    // // if (checkData.isEmpty()) {
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This student
    // // cannot delete file of other user");
    // // } else {
    // // fileRepository.deleteById(fileId);
    // // }
    // // } else if (role.contains(lecturer)) {
    // // System.out.println("lecturer cannot delete file");
    // // throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "lecturer
    // cannot
    // // delete file");
    // // }
    // // }
    // // }
    // }

}