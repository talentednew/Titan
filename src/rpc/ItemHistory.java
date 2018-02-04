package rpc;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dbConnection.DBConnection;
import dbConnection.DBConnectionFactory;

import entity.Item;


/**
 * Servlet implementation class ItemHistory
 */
@WebServlet("/history")
public class ItemHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private DBConnection conn = DBConnectionFactory.getDBConnection();   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ItemHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		String userId = request.getParameter("user_id");
	    Set<Item> items = conn.getFavoriteItems(userId);
	    JSONArray array = new JSONArray();
	    for (Item item : items) {
	      JSONObject obj = item.toJSONObject();
	      try {
	    	  	obj.append("favorite", true);
	      } catch (JSONException e) {
	    	  	e.printStackTrace();
	      }
	      array.put(obj);
	    }
	    RpcHelper.writeJsonArray(response, array);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		try {
//			JSONObject input = RpcHelper.readJsonObject(request);
//			if (input.has("user_id") && input.has("visited")) {
//				String userId = (String) input.get("user_id");
//				JSONArray array = (JSONArray) input.get("visited");
//				List<String> visitedEvents = new ArrayList<>();
//				for (int i = 0; i < array.length(); i++) {
//					String eventId = (String) array.get(i);
//					visitedEvents.add(eventId);
//				}
//				conn.setFavoriteItems(userId, itemIds);
//				RpcHelper.writeJsonObject(response,
//						new JSONObject().put("status", "OK"));
//			} else {
//				RpcHelper.writeJsonObject(response,
//						new JSONObject().put("status", "InvalidParameter"));
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String userId = input.getString("user_id");
			JSONArray array = (JSONArray)input.get("favorite");
			
			List<String> histories = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				String itemId = (String) array.get(i);
				histories.add(itemId);
			}
			conn.setFavoriteItems(userId, histories);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//throws ServletException, IOException;
		try {
			JSONObject input = RpcHelper.readJsonObject(request);
			String userId = input.getString("user_id");
			JSONArray array = (JSONArray) input.get("favorite");
			
			List<String> histories = new ArrayList<>();
			for (int i = 0; i < array.length(); i++) {
				String itemId = (String) array.get(i);
				histories.add(itemId);
			}
			conn.unsetFavoriteItems(userId, histories);
			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
