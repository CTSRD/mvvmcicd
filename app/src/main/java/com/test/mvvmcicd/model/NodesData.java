package com.test.mvvmcicd.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NodesData {

	/**
	 * status : 200
	 * message : Success
	 * data : {"nodes":[{"lanmacaddr":"00:06:19:65:26:a1","distance":3,"location":"MyRoomYoYoYoYoHmmm","ipaddr":"192.168.0.1","macaddr":"00:06:19:65:26:a0","role":"CAP","connected":true},{"lanmacaddr":"00:06:19:65:26:d0","distance":3,"location":"MWR-3102","ipaddr":"","macaddr":"00:06:19:65:26:cf","role":"RE","connected":false}]}
	 */

	@SerializedName("status")
	private Integer status;

	@SerializedName("message")
	private String message;

	@SerializedName("data")
	private DataBean data;

	public static NodesData objectFromData(String str) {

		return new Gson().fromJson(str, NodesData.class);
	}

	public static NodesData objectFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);

			return new Gson().fromJson(jsonObject.getString(str), NodesData.class);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static List<NodesData> arrayNodesDataFromData(String str) {

		Type listType = new TypeToken<ArrayList<NodesData>>() {
		}.getType();

		return new Gson().fromJson(str, listType);
	}

	public static List<NodesData> arrayNodesDataFromData(String str, String key) {

		try {
			JSONObject jsonObject = new JSONObject(str);
			Type listType = new TypeToken<ArrayList<NodesData>>() {
			}.getType();

			return new Gson().fromJson(jsonObject.getString(str), listType);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new ArrayList();


	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public static class DataBean {
		@SerializedName("nodes")
		private List<NodesBean> nodes;

		public static DataBean objectFromData(String str) {

			return new Gson().fromJson(str, DataBean.class);
		}

		public static DataBean objectFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);

				return new Gson().fromJson(jsonObject.getString(str), DataBean.class);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		public static List<DataBean> arrayDataBeanFromData(String str) {

			Type listType = new TypeToken<ArrayList<DataBean>>() {
			}.getType();

			return new Gson().fromJson(str, listType);
		}

		public static List<DataBean> arrayDataBeanFromData(String str, String key) {

			try {
				JSONObject jsonObject = new JSONObject(str);
				Type listType = new TypeToken<ArrayList<DataBean>>() {
				}.getType();

				return new Gson().fromJson(jsonObject.getString(str), listType);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return new ArrayList();


		}

		public List<NodesBean> getNodes() {
			return nodes;
		}

		public void setNodes(List<NodesBean> nodes) {
			this.nodes = nodes;
		}

		public static class NodesBean {
			/**
			 * lanmacaddr : 00:06:19:65:26:a1
			 * distance : 3
			 * location : MyRoomYoYoYoYoHmmm
			 * ipaddr : 192.168.0.1
			 * macaddr : 00:06:19:65:26:a0
			 * role : CAP
			 * connected : true
			 */

			@SerializedName("lanmacaddr")
			private String lanmacaddr;
			@SerializedName("distance")
			private Integer distance;
			@SerializedName("location")
			private String location;
			@SerializedName("ipaddr")
			private String ipaddr;
			@SerializedName("macaddr")
			private String macaddr;
			@SerializedName("role")
			private String role;
			@SerializedName("connected")
			private Boolean connected;

			public static NodesBean objectFromData(String str) {

				return new Gson().fromJson(str, NodesBean.class);
			}

			public static NodesBean objectFromData(String str, String key) {

				try {
					JSONObject jsonObject = new JSONObject(str);

					return new Gson().fromJson(jsonObject.getString(str), NodesBean.class);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return null;
			}

			public static List<NodesBean> arrayNodesBeanFromData(String str) {

				Type listType = new TypeToken<ArrayList<NodesBean>>() {
				}.getType();

				return new Gson().fromJson(str, listType);
			}

			public static List<NodesBean> arrayNodesBeanFromData(String str, String key) {

				try {
					JSONObject jsonObject = new JSONObject(str);
					Type listType = new TypeToken<ArrayList<NodesBean>>() {
					}.getType();

					return new Gson().fromJson(jsonObject.getString(str), listType);

				} catch (JSONException e) {
					e.printStackTrace();
				}

				return new ArrayList();


			}

			public String getLanmacaddr() {
				return lanmacaddr;
			}

			public void setLanmacaddr(String lanmacaddr) {
				this.lanmacaddr = lanmacaddr;
			}

			public Integer getDistance() {
				return distance;
			}

			public void setDistance(Integer distance) {
				this.distance = distance;
			}

			public String getLocation() {
				return location;
			}

			public void setLocation(String location) {
				this.location = location;
			}

			public String getIpaddr() {
				return ipaddr;
			}

			public void setIpaddr(String ipaddr) {
				this.ipaddr = ipaddr;
			}

			public String getMacaddr() {
				return macaddr;
			}

			public void setMacaddr(String macaddr) {
				this.macaddr = macaddr;
			}

			public String getRole() {
				return role;
			}

			public void setRole(String role) {
				this.role = role;
			}

			public Boolean isConnected() {
				return connected;
			}

			public void setConnected(Boolean connected) {
				this.connected = connected;
			}
		}
	}
}
