package br.net.rafaeltuelho.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

/**
 * Servlet implementation class MemoryService
 */
@WebServlet(value="/MemoryService")
public class MemoryService extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int MIN_AMOUNT = 100;
	private static final String SESSION_BUCKET_ATTR_NAME = "bucket";
	
	enum Commands{
		CONSUME,
		REMOVE,
		SIZE
	};
	
	private Commands acceptedCommands;

    /**
     * Default constructor. 
     */
    public MemoryService() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String cmd = !StringUtils.isBlank(request.getParameter("cmd")) ? 
				request.getParameter("cmd") : Commands.CONSUME.toString();
		String value = !StringUtils.isBlank(request.getParameter("value")) ? 
				request.getParameter("value") : String.valueOf(MIN_AMOUNT);

		long currentUsedMemory = 0;
		acceptedCommands = Commands.valueOf(cmd.toUpperCase());
		switch (acceptedCommands) {
		case CONSUME:
			consumeMemory(value, session);
			currentUsedMemory = getUsedMemory(session);
			break;
		case SIZE:
			currentUsedMemory = getUsedMemory(session);
			break;

		default:
			break;
		}
		
		response.getWriter().append("Current Used Memory: " + currentUsedMemory);
	}

	private long getUsedMemory(HttpSession session) {
		Runtime rt = Runtime.getRuntime();
		return (rt.totalMemory() - rt.freeMemory());
	}

	private void consumeMemory(String value, HttpSession session) {
		List<byte[]> currentBucket;
		if (session.getAttribute(SESSION_BUCKET_ATTR_NAME) == null)
			currentBucket = new ArrayList<byte[]>();
		else
			currentBucket = (List<byte[]>)session.getAttribute(SESSION_BUCKET_ATTR_NAME);

		currentBucket.add(new byte[Integer.valueOf(value) * 1000 * 1000]);
		session.setAttribute(SESSION_BUCKET_ATTR_NAME, currentBucket);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
