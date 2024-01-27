package sit.int371.modride_service.repositories;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import sit.int371.modride_service.beans.UsersBean;
import sit.int371.modride_service.beans.files_beans.FilesDataBean;
import sit.int371.modride_service.beans.files_beans.UsersFilesBean;

@Mapper
public interface FilesRepository {

        @Select({
                " select concat(#{uri_files_storage},profile_img_name) as file_name from users_files  ",
                " where profile_img_name = #{file_name} ",
        })
        public FilesDataBean getUserProfilePicture(FilesDataBean filesDataBean) throws Exception;

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
}
