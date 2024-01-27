package sit.int371.modride_service.controllers;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import sit.int371.modride_service.beans.APIResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController implements Serializable {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	public static final String MSG_401 = "Unauthorized.";
	public static final String MSG_404 = "Data not found.";

	public static final String METHOD_GET = "get";
	public static final String METHOD_POST = "post";
	public static final String METHOD_PUT = "put";
	public static final String METHOD_DELETE = "delete";

	protected APIResponseBean checkException(Exception e, APIResponseBean res, String errorType) {
		System.out.println("hello custom-exception");
		System.out.println("expception found: " + e.getMessage());

		if (e.getMessage() != null) {
			if (e.getMessage().equals(MSG_404)) {
				res.setResponse_code("40400");
				res.setResponse_desc(e.getMessage());
			} else if (e.getMessage().equals(MSG_401)) {
				res.setResponse_code("40100");
				res.setResponse_desc(e.getMessage());
			} else if (e.getMessage().contains("is required.") || e.getMessage().contains("should not be")) {
				res.setResponse_code("40000");
				res.setResponse_desc(e.getMessage());
			} else if (e.getMessage().contains("Duplicate")) {
				res.setResponse_code("40900");
				if (e.getCause() != null) {
					res.setResponse_desc("Duplicated data, " + e.getCause().getMessage() + ".");
				} else {
					res.setResponse_desc(e.getMessage());
				}
			} else {
				logger.info("Got an exception. {}", e.getMessage());
				res.setResponse_code("400");
				res.setResponse_desc(e.getMessage());
				// res.setResponse_desc("API error please contact administrator.");
			}
		} else {
			System.out.println("getMessage() เป็น null");
			switch (errorType) {
				case "get_file":
					System.out.println("error เกี่ยวกับหาไฟล์ไม่เจอ");
					res.setResponse_code("400");
					res.setResponse_desc("files not found !");
					// throw Exception();
					break;

				default:
					res.setResponse_code("400");
					res.setResponse_desc(e.getMessage());
					break;
			}

		}

		return res;
	}

}
