package sit.int371.modride_service.beans;
import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.web.context.request.RequestContextHolder;

import lombok.Data;

@Data
public class APIResponseBean {
	
	private String response_ref = RequestContextHolder.currentRequestAttributes().getSessionId();
	private LocalDateTime response_datetime = LocalDateTime.now();
	private Integer response_code = 200;
	private String response_desc = "success";
	private Object data;
	// private Paginate paginate = new Paginate();
	


}