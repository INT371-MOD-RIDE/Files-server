package sit.int371.modride_service.repositories;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import sit.int371.modride_service.beans.UsersBean;
import sit.int371.modride_service.beans.driver_profile.LicensesBean;
import sit.int371.modride_service.beans.driver_profile.VehiclesBean;
import sit.int371.modride_service.beans.files_beans.FilesDataBean;
import sit.int371.modride_service.beans.files_beans.LicensesFilesBean;
import sit.int371.modride_service.beans.files_beans.UsersFilesBean;
import sit.int371.modride_service.beans.files_beans.VehiclesFilesBean;

@Mapper
public interface FilesRepository {
        @Select({
                        " select * from events e  ",
                        " inner join licenses l on e.user_id = l.user_id ",
                        " where l.license_id = #{license_id} ",
        })
        public List<LicensesBean> checkEventWithLicenseId(Integer license_id) throws Exception;

        @Select({
                        " select * from events e  ",
                        " inner join vehicles v on v.vehicle_id = e.vehicle_id ",
                        " where v.vehicle_id = #{vehicle_id} ",
        })
        public List<VehiclesBean> checkEventWithVehicleId(Integer vehicle_id) throws Exception;

        @Select({
                        " select l.user_id,l.license_id,l.license_fn,l.license_ln ",
                        " ,lf.license_file_name,lf.license_download,lf.license_size ",
                        " ,ls.approval_status,ls.denied_detail,ls.timestamp ",
                        " from licenses l  ",
                        " inner join users u on l.user_id = u.user_id ",
                        " inner join license_files lf on l.license_id = lf.license_id ",
                        " inner join license_approval_status ls on l.license_id = ls.license_id ",
                        " where l.license_id = #{license_id} ",
        })
        public LicensesBean getLicenseDetail(Integer license_id) throws Exception;

        @Select({
                        " select concat(#{uri_files_storage},profile_img_name) as file_name from users_files ",
                        " where profile_img_name = #{file_name} ",
        })
        public FilesDataBean getUserProfilePicture(FilesDataBean filesDataBean)
                        throws Exception;

        @Select({
                        " select concat(#{uri_files_storage},license_file_name) as file_name from license_files ",
                        " where license_file_name = #{file_name} ",
        })
        public FilesDataBean getLicensePicture(FilesDataBean filesDataBean)
                        throws Exception;

        @Select({
                        " select concat(#{uri_files_storage},vehicle_file_name) as file_name from vehicle_files ",
                        " where vehicle_file_name = #{file_name} ",
        })
        public FilesDataBean getVehiclePicture(FilesDataBean filesDataBean)
                        throws Exception;

        // 😐 users-profile
        @Select({
                        "select * from users_files where owner_id = #{id} "
        })
        public List<UsersFilesBean> checkUserFiles(String id) throws Exception;

        @Insert({
                        " insert into users_files(owner_id,profile_img_name,download_url) ",
                        " values(#{id},#{file_name},#{download_url}) ",
        })
        public void insertUserProfilePicture(FilesDataBean filesDataBean) throws Exception;

        @Update({
                        " update users_files ",
                        " set profile_img_name = #{file_name} ",
                        " ,download_url = #{download_url} ",
                        " where owner_id =  #{id} ",
        })
        public void updateUserProfilePicture(FilesDataBean filesDataBean) throws Exception;

        // 🪪 license
        @Select({
                        " select * from license_files where license_id = #{id} "
        })
        public List<LicensesFilesBean> checkLicenseFiles(String id) throws Exception;

        @Insert({
                        " insert into license_files(license_id,license_file_name,license_download,license_size) ",
                        " values(#{id},#{file_name},#{download_url},#{size}) ",
        })
        public void insertLicensePicture(FilesDataBean filesDataBean) throws Exception;

        @Update({
                        " update license_files ",
                        " set license_file_name = #{file_name} ",
                        " ,license_download = #{download_url} ",
                        " where license_id =  #{id}  ",
        })
        public void updateLicensePicture(FilesDataBean filesDataBean) throws Exception;

        // 🚕 vehicle
        @Select({
                        " select * from vehicle_files where vehicle_id = #{id} "
        })
        public List<VehiclesFilesBean> checkVehicleFiles(String id) throws Exception;

        @Insert({
                        " insert into vehicle_files(vehicle_id,vehicle_file_name,vehicle_download) ",
                        " values(#{id},#{file_name},#{download_url}) ",
        })
        public void insertVehiclePicture(FilesDataBean filesDataBean) throws Exception;

        @Update({
                        " update vehicle_files ",
                        " set vehicle_file_name = #{file_name} ",
                        " ,vehicle_download = #{download_url} ",
                        " where vehicle_id =  #{id} ",
        })
        public void updateVehiclePicture(FilesDataBean filesDataBean) throws Exception;

        // if delete: driver-profile = use all delete() below
        // delete vehicles with vehicle_files
        @Delete({
                        " delete from vehicle_files  ",
                        " where vehicle_id = #{vehicle_id} ",
        })
        public void deleteVehicleFile(VehiclesBean vehiclesBean) throws Exception;

        @Delete({
                        " delete from vehicles ",
                        " where vehicle_id = #{vehicle_id} ",
        })
        public void deleteVehicle(VehiclesBean vehiclesBean) throws Exception;

        @Delete({
                        " delete from license_files where license_id = #{license_id} "
        })
        public void deleteLicenseFile(LicensesBean licensesBean) throws Exception;

        @Delete({
                        " delete from license_approval_status where license_id = #{license_id} "
        })
        public void deleteLicenseAppStatus(LicensesBean licensesBean) throws Exception;

        @Delete({
                        " delete from licenses where license_id = #{license_id} "
        })
        public void deleteLicense(LicensesBean licensesBean) throws Exception;

}
