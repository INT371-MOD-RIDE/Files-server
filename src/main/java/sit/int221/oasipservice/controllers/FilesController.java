package sit.int221.oasipservice.controllers;


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
import sit.int221.oasipservice.entities.File;
import sit.int221.oasipservice.payload.Response;
import sit.int221.oasipservice.repositories.FileRepository;
import sit.int221.oasipservice.services.DBFileService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/files")
public class FilesController {

    @Autowired
    private DBFileService dbFileService;
    @Autowired
    private FileRepository fileRepository;

    //assign role จาก string ดิบๆ เก็บเป็น object-String เลย เพื่อความสะดวกในการเช็ค roles
    private final String admin = "admin";
    private final String lecturer = "lecturer";
    private final String student = "student";

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public Response uploadFile(@RequestParam("file") MultipartFile file,
                               @RequestParam("eventStartTime") String eventStartTime,
                               HttpServletRequest request) throws Exception {
        System.out.println("\n--------\nการทำงานของ post-file\n--------");

//guest (ไม่มี token)
        if (request.getHeader("Authorization") == null) {
            System.out.println("this is [guest] user : guest cannot upload-file");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot upload-file");
        }
//else:condition ตัวนี้สำหรับทำงานกรณีมี token
        else {
//decode token เพื่อเช็ค algorithm
            final String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            DecodedJWT tokenDecoded = JWT.decode(token);
            System.out.println(tokenDecoded.getAlgorithm());
//token from azure & oasip
//alg : azure
            if (tokenDecoded.getAlgorithm().contains("RS256")) {
                System.out.println("this is token from azure");
                if (tokenDecoded.getClaims().get("roles").toString().contains(lecturer)) {
                    System.out.println("role: lecturer");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Lecturer not have permission to upload-file");
                }
                System.out.println("not lecturer role = cannot upload-file");
            }
//alg : oasip
            else if (tokenDecoded.getAlgorithm().contains("HS512")) {
                System.out.println("token from oasip");
                String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
                if (role.contains(lecturer)) {
                    System.out.println("role: " + role);
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Lecturer not have permission to upload-file");
                }
                System.out.println("not lecturer role = cannot upload-file");
            }

//หาก เงื่อนไขก่อนหน้า ไม่พบว่าเป็น role: lecturer ก็จะสามารถเข้าถึง feature นี้ได้
            if (tokenDecoded.getAlgorithm().contains("RS256") || tokenDecoded.getAlgorithm().contains("HS512")) {

                System.out.println("size of file: " + file.getSize());
                File fileName = dbFileService.storeFile(file, eventStartTime);


                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/files/download/")
                        .path(fileName.getId())
                        .toUriString();

                return new Response(fileName.getFileName(), fileDownloadUri, file.getContentType(), file.getSize());
            }
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "something went wrong");
    }

    //---------------------------------------------------------------
//    @GetMapping("")
//    public List<File> getAllFile(){
//        return fileRepository.findAll();
//    }

//    // test post man ใส่ /downloadFile/<fileId>
//    @GetMapping("/download/{fileId:.+}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, HttpServletRequest request) {
//        // Load file as Resource
//        File file = dbFileService.getFileById(fileId);
//
//
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(file.getFileType()))
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
//                .body(new ByteArrayResource(file.getData()));
//    }

    //    getFileById
    @GetMapping("/{bookingId}")
    public File getFileByBookingId(@PathVariable Integer bookingId, HttpServletRequest request) {
        System.out.println("\n--------\nการทำงานของ get-file-by-bookingId\n--------");
//guest (ไม่มี token)
        if (request.getHeader("Authorization") == null) {
            System.out.println("this is [guest] user : guest cannot get file by bookingid");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot get file by bookingid");
        }
//else:condition ตัวนี้สำหรับทำงานกรณีมี token : role อะไรก็สามารถ get ได้หมด
        else {
            // Load file as Resource
            File file = fileRepository.getFileByBookingId(bookingId);
            if (file == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            return fileRepository.getFileByBookingId(bookingId);
        }
    }

    //    download file เราจะ download โดยใช้ bookingId
    @GetMapping("/download/{fileId:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId, HttpServletRequest request) {
        System.out.println("\n--------\nการทำงานของ download-file\n--------");
//guest (ไม่มี token)
        if (request.getHeader("Authorization") == null) {
            System.out.println("this is [guest] user : guest cannot get file by bookingid");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot get file by bookingid");
        }
//else:condition ตัวนี้สำหรับทำงานกรณีมี token : role อะไรก็สามารถ download ได้หมด
        else {
            // Load file as Resource
            File file = dbFileService.getFileById(fileId);
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(new ByteArrayResource(file.getData()));
        }
    }


    //---------------------------------------------------------------
    //Patch
    @PatchMapping("/update/{bookingId}")
    public void updateFile(@PathVariable Integer bookingId, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        System.out.println("\n--------\nการทำงานของ edit-file\n--------");
//guest (ไม่มี token)
        if (request.getHeader("Authorization") == null) {
            System.out.println("this is [guest] user : guest cannot get edit file");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "guest cannot edit file");
        }
//else:condition ตัวนี้สำหรับทำงานกรณีมี token
        else {
//decode token เพื่อเช็ค algorithm
            final String authorizationHeader = request.getHeader("Authorization");
            String token = authorizationHeader.substring(7);
            DecodedJWT tokenDecoded = JWT.decode(token);
            System.out.println(tokenDecoded.getAlgorithm());
//กรองว่าเจอไฟล์ที่จะ edit ไหม
            File getFile = dbFileService.getFileByBookingId(bookingId);
            if (getFile == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found");
            }
//alg : azure
            if (tokenDecoded.getAlgorithm().contains("RS256")) {
                System.out.println("token from azure");
                //----code-----//
            }
//token of oasip
            else if (tokenDecoded.getAlgorithm().contains("HS512")) {
                System.out.println("token from oasip");
                String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
                if (role.contains(lecturer)) {
                    System.out.println("lecturer cannot update file");
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "lecturer cannot edit file");
                } else if (role.contains(admin)) {
                    System.out.println("admin role");
                    dbFileService.updateFile(bookingId, file);
                }
                else if(role.contains(student)){
                    System.out.println("student role");
                    String email = SecurityContextHolder.getContext().getAuthentication().getName();
                    //เช็คก่อนว่า student คนนี้เป็นเจ้าของ event ทีเชื่อมกับ file นี้หรือไม่
                    List<File> checkData = fileRepository.getDataByEmailAndBookingIdWithFile(email,bookingId);
                    if (checkData.isEmpty()){
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "This student cannot edit file of other user");
                    }else{
                        dbFileService.updateFile(bookingId, file);
                    }
                }
            }
        }
    }

//---------------------------------------------------------------

    //    Delete
    @DeleteMapping("/delete/{fileId}")
    public void deleteFile(@PathVariable String fileId) {
        fileRepository.findById(fileId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, fileId + " does not exist !"));
        fileRepository.deleteById(fileId);
    }


}