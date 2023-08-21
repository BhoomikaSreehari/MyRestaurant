package res;
	import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import dao.MyDao;
import dto.Customer;

	//This is to Map the action URL to this class(Should be Same as action - Case sensitive)
	@WebServlet("/signup")
	//To receive image we need to use this-enctype
	@MultipartConfig
	//This is to make Class as Servlet Class
	public class CustomerSignup extends HttpServlet {
		@Override
		// When there is form and we want data to be secured so doPost
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// Logic to Receive Values from Front End
			String fullName = req.getParameter("fullname");
			String password = req.getParameter("password");
			long mobile = Long.parseLong(req.getParameter("mobile"));
			String email = req.getParameter("email");
			String gender = req.getParameter("gender");
			String country = req.getParameter("country");
			LocalDate dob = LocalDate.parse(req.getParameter("dob"));
			int age = Period.between(dob, LocalDate.now()).getYears();

			//Logic to Receive image and convert to byte[]
			Part pic = req.getPart("picture");
			byte[] picture=null;
			picture=new byte[pic.getInputStream().available()];
			pic.getInputStream().read(picture);
			
			MyDao dao = new MyDao();
			
			if(dao.fetchByEmail(email)==null && dao.fetchByMobile(mobile)==null)
{
			Customer customer = new Customer();
			customer.setAge(age);
			customer.setCountry(country);
			customer.setDob(dob);
			customer.setEmail(email);
			customer.setFullname(fullName);
			customer.setGender(gender);
			customer.setMobile(mobile);
			customer.setPassword(password);
			customer.setPicture(picture);
			
			
			dao.save(customer);

			resp.getWriter().print("<h1 style='color:green'>Account Created Successfully</h1>");
			req.getRequestDispatcher("login.html").include(req, resp);
}	
			else {
				resp.getWriter().print("<h1 style='color:red'>Email and Mobile should be unique</h>");
				req.getRequestDispatcher("signUp.html").include(req, resp);
			}
}
}