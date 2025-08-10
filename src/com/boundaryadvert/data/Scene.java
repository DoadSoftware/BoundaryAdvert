package com.boundaryadvert.data;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

public class Scene {
	
	private String broadcaster;
	private String scene_path;
	private String which_layer;
	
	public Scene() {
		super();
	}
	public String getBroadcaster() {
		return broadcaster;
	}
	public void setBroadcaster(String broadcaster) {
		this.broadcaster = broadcaster;
	}
	public String getScene_path() {
		return scene_path;
	}
	public void setScene_path(String scene_path) {
		this.scene_path = scene_path;
	}
	public String getWhich_layer() {
		return which_layer;
	}
	public void setWhich_layer(String which_layer) {
		this.which_layer = which_layer;
	}
	public void sceneLoad(String whichPlatformToUse, PrintWriter print_writer, String vizScene, Integer whichLayer) throws InterruptedException
	{
		switch (whichPlatformToUse) {
		case "EVEREST":
			System.out.println("sceneLoad: whichLayer = " + whichLayer);
			System.out.println("sceneLoad: vizScene = " + vizScene);
			print_writer.println("LAYER" + whichLayer + "*EVEREST*SCENE LOAD " + vizScene + ";");
			print_writer.println("LAYER" + whichLayer + "*EVEREST*STAGE*DIRECTOR*In STOP;");
			print_writer.println("LAYER" + whichLayer + "*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			for(int iLayer = whichLayer + 1; iLayer <= 3; iLayer++) {
				print_writer.println("LAYER" + iLayer + "*EVEREST*STAGE*DIRECTOR*In STOP;");
				print_writer.println("LAYER" + iLayer + "*EVEREST*STAGE*DIRECTOR*In SHOW 0.0;");
			}
			this.scene_path = vizScene;
			this.which_layer = String.valueOf(whichLayer);
			TimeUnit.MILLISECONDS.sleep(500);
			break;
		}
	}
	@Override
	public String toString() {
		return "Scene [broadcaster=" + broadcaster + ", scene_path=" + scene_path + ", which_layer=" + which_layer
				+ "]";
	}
}
