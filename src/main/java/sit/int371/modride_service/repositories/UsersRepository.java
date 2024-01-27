package sit.int371.modride_service.repositories;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import sit.int371.modride_service.beans.BranchesBean;
import sit.int371.modride_service.beans.FacultiesBean;
import sit.int371.modride_service.beans.FriendsBean;
import sit.int371.modride_service.beans.UsersBean;

@Mapper
public interface UsersRepository {
        @Select("SELECT u.user_id,u.branch_id,u.email,u.fullname FROM users u")
        public List<UsersBean> getAllUsers() throws Exception;

        @Select({
                " select u.user_id,r.role_id,r.role_name,u.email,u.fullname,COALESCE(u.tel, '') AS tel,u.other_contact,u.contact_info, ",
                " f.faculty_name,b.branch_name from users u  ",
                " inner join branches b on u.branch_id = b.branch_id ",
                " inner join faculties f on b.faculty_id = f.faculty_id ",
                " inner join roles r on u.role_id = r.role_id ",
                " where u.user_id = #{user_id} ",
        })
        public UsersBean getUserById(UsersBean bean) throws Exception;

        // getUserByEmail (เป็นการให้ login เพื่อเข้าใช้แบบ mockup เฉยๆ)
        @Select({
                        " select u.user_id,r.role_id,r.role_name,u.email,u.fullname,COALESCE(u.tel, '') AS tel,  ",
                        " f.faculty_name,b.branch_name from users u  ",
                        " inner join branches b on u.branch_id = b.branch_id ",
                        " inner join faculties f on b.faculty_id = f.faculty_id ",
                        " inner join roles r on u.role_id = r.role_id ",
                        " where u.email = #{email} ",
        })
        public UsersBean getUserByEmail(UsersBean bean) throws Exception;

        // @Select({
        //         " select u.user_id,r.role_id,r.role_name,u.email,u.fullname,COALESCE(u.tel, '') AS tel,  ",
        //         " f.faculty_name,b.branch_name from users u  ",
        //         " inner join branches b on u.branch_id = b.branch_id ",
        //         " inner join faculties f on b.faculty_id = f.faculty_id ",
        //         " inner join roles r on u.role_id = r.role_id ",
        //         " where u.fullname = #{fullname} and u.password = #{password}  ",
        // })
        // public UsersBean getAdminUser(HashMap<String,String> param) throws Exception;

        // @Select({
        // " select r.role_name from users u ",
        // " inner join user_role ur on u.user_id = ur.user_id ",
        // " inner join roles r on ur.role_id = r.role_id ",
        // " where u.user_id = #{user_id} "
        // })
        // public List<String> getRolesById(HashMap<String, Object> params) throws
        // Exception;

        @Select({
                        " select faculty_id,faculty_name from faculties order by faculty_name asc ; "
        })
        public List<FacultiesBean> getFaculties() throws Exception;

        @Select({
                        " select branch_id,faculty_id,branch_name from branches order by branch_name asc "
        })
        public List<BranchesBean> getBranches() throws Exception;

        // sign-up users account
        @Insert({
                        " insert into users(branch_id,role_id,email,fullname,tel,other_contact,contact_info) ",
                        " values(#{branch_id},#{role_id},#{email},#{fullname},#{tel},#{other_contact},#{contact_info}) ",
        })
        @Options(useGeneratedKeys = true, keyColumn = "user_id", keyProperty = "user_id")
        public void createAccount(UsersBean bean) throws Exception;

        // // add role of user from create-account
        // @Insert({
        // " insert into user_role(user_id,role_id) ",
        // " values(#{user_id},#{role_id}) ",
        // })
        // public void addRoleForUser(HashMap<String, Object> params) throws Exception;

        // update user-account
        @Update({
                        " update users ",
                        " set role_id = #{role_id}, tel = #{tel}, other_contact = #{other_contact}, contact_info = #{contact_info} ",
                        " where user_id = #{user_id} ",
        })
        public void updateUserAccount(UsersBean bean) throws Exception;
}
