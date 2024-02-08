package sit.int371.modride_service.repositories;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.Delete;

import sit.int371.modride_service.beans.EventDetailBean;
import sit.int371.modride_service.beans.EventMemberBean;
import sit.int371.modride_service.beans.EventsBean;
import sit.int371.modride_service.beans.UsersBean;
import sit.int371.modride_service.beans.driver_profile.VehiclesBean;

@Mapper
public interface VehiclesRepository {
    @Insert({
            " insert into vehicles (user_id,brand,model,vehicle_type,vehicle_color,license,seats,car_img_path) ",
            " values (#{user_id},#{brand},#{model},#{vehicle_type},#{vehicle_color},#{license},#{seats},#{car_img_path}) "
    })
    @Options(useGeneratedKeys = true, keyColumn = "vehicle_id", keyProperty = "vehicle_id")
    // public void createVehicles(VehiclesBean vehiclesBean) throws Exception;
    public void createVehicles(HashMap<String, Object> params) throws Exception;

    @Update({
            " update vehicles set brand = #{brand},model = #{model},vehicle_type = #{vehicle_type},vehicle_color = #{vehicle_color},license = #{license},car_img_path = #{car_img_path} ",
            " where vehicle_id = #{vehicle_id} "
    })
    public void editVehicles(HashMap<String, Object> params) throws Exception;

    @Delete({
            " delete from vehicles where vehicle_id = #{vehicle_id} "
    })
    public void deleteVehicles(HashMap<String, Object> params) throws Exception;

    @Select({
            " select vehicle_id from vehicles where user_id = #{user_id} "
    })
    public Integer getVehicleIdByUserId(HashMap<String, Object> params) throws Exception;

    @Select({
            " select vehicle_id from vehicles where license = #{license} "
    })
    public Integer getVehiclesByLicense(HashMap<String, Object> params) throws Exception;
}
