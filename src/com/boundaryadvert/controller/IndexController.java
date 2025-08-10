package com.boundaryadvert.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.boundaryadvert.data.Scene;
import com.boundaryadvert.model.Configurations;
import com.boundaryadvert.model.Matrix;
import com.boundaryadvert.service.BoundaryAdvertService;
import com.boundaryadvert.util.BoundaryAdvertFunctions;
import com.boundaryadvert.util.BoundaryAdvertUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
public class IndexController 
{
	@Autowired
	BoundaryAdvertService boundaryAdvertService;
	
	public static String expiry_date = "2025-12-31";
	public static String current_date = "";
	public static String error_message = "";
	public static Configurations session_Configurations;
	public static Socket session_socket;
	public static PrintWriter print_writer;
	public static List<Scene> this_scene = new ArrayList<Scene>();
	public static List<Matrix> session_matrices = new ArrayList<Matrix>();
	
	@RequestMapping(value = {"/","/initialise"}, method={RequestMethod.GET,RequestMethod.POST}) 
	public String initialisePage(ModelMap model) throws JAXBException, IOException 
	{
		
		if(current_date == null || current_date.isEmpty()) {
			current_date = BoundaryAdvertFunctions.getOnlineCurrentDate();
		}
		model.addAttribute("session_viz_scenes", new File(BoundaryAdvertUtil.BOUNDARY_ADVERT_DIRECTORY + 
				BoundaryAdvertUtil.SCENES_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".sum") && pathname.isFile();
		    }
		}));

		model.addAttribute("configuration_files", new File(BoundaryAdvertUtil.BOUNDARY_ADVERT_DIRECTORY 
			+ BoundaryAdvertUtil.CONFIGURATIONS_DIRECTORY).listFiles(new FileFilter() {
			@Override
		    public boolean accept(File pathname) {
		        String name = pathname.getName().toLowerCase();
		        return name.endsWith(".xml") && pathname.isFile();
		    }
		}));
		
		session_matrices = updateSessionVariables("GET_ALL_MATRICES");
		model.addAttribute("session_Configurations",session_Configurations);
	
		return "initialise";
	}
	
	public List<Matrix> updateSessionVariables(String whatToProcess) 
	{
		switch (whatToProcess) {
		case "GET_ALL_MATRICES":
			return boundaryAdvertService.getAllMatrices();
		case "GROUP_BY_COMMON_MATRIX":
			return  boundaryAdvertService.getAllMatrices().stream()
				.filter(BoundaryAdvertFunctions.distinctByKey(m -> m.getDescription()))
				.collect(Collectors.toList());				
		}
		return null;
	}
	
	@RequestMapping(value = {"/match"}, method = {RequestMethod.POST,RequestMethod.GET})
	public String boundaryAdvertMatchPage(ModelMap model,
		@RequestParam(value = "configurationFileName", required = false, defaultValue = "") String configurationFileName,
		@RequestParam(value = "selectedBroadcaster", required = false, defaultValue = "") String selectedBroadcaster,
		@RequestParam(value = "vizIPAddress", required = false, defaultValue = "") String vizIPAddresss,
		@RequestParam(value = "vizPortNumber", required = false, defaultValue = "") String vizPortNumber,
		@RequestParam(value = "vizScene", required = false, defaultValue = "") String vizScene) 
		throws NumberFormatException, UnknownHostException, ParseException, IOException, 
			InterruptedException, JAXBException
	{
		if(current_date == null || current_date.isEmpty()) {
		
			model.addAttribute("error_message","You must be connected to the internet online");
			return "error";
		
		} else if(new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date).before(new SimpleDateFormat("yyyy-MM-dd").parse(current_date))) {
			
			model.addAttribute("error_message","This software has expired");
			return "error";
			
		}else {

			model.addAttribute("licence_expiry_message",
				"Software licence expires on " + new SimpleDateFormat("E, dd MMM yyyy").format(
				new SimpleDateFormat("yyyy-MM-dd").parse(expiry_date)));

			session_Configurations = new Configurations();
			session_Configurations.setBroadcaster(selectedBroadcaster);
			session_Configurations.setFilename(configurationFileName);
			
			if(!vizIPAddresss.trim().isEmpty() && !vizPortNumber.trim().isEmpty()) {
				session_socket = new Socket(vizIPAddresss, Integer.valueOf(vizPortNumber));
				print_writer = new PrintWriter(session_socket.getOutputStream(), true);
				session_Configurations.setIpAddress(vizIPAddresss);
				session_Configurations.setPortNumber(vizPortNumber);
				session_Configurations.setVizscene(vizScene);
				this_scene.add(new Scene());
				switch (selectedBroadcaster.toUpperCase()) {
				case BoundaryAdvertUtil.DOAD_BOUNDARY_ADVERT:
					this_scene.get(0).sceneLoad("EVEREST", print_writer, BoundaryAdvertUtil.BOUNDARY_ADVERT_DIRECTORY + 
						BoundaryAdvertUtil.SCENES_DIRECTORY + vizScene, 1);
					break;
				}
			}

			JAXBContext.newInstance(Configurations.class).createMarshaller().marshal(session_Configurations, 
				new File(BoundaryAdvertUtil.BOUNDARY_ADVERT_DIRECTORY + BoundaryAdvertUtil.CONFIGURATIONS_DIRECTORY 
				+ configurationFileName));
			
			model.addAttribute("matrices", updateSessionVariables("GROUP_BY_COMMON_MATRIX"));
			model.addAttribute("session_Configurations", session_Configurations);
			
			return "match";
		}
	}
	
	@RequestMapping(value = {"/processBoundaryAdvertProcedures"}, method={RequestMethod.GET,RequestMethod.POST})    
	public @ResponseBody String processBoundaryAdvertProcedures(
			@RequestParam(value = "whatToProcess", required = false, defaultValue = "") String whatToProcess,
			@RequestParam(value = "valueToProcess", required = false, defaultValue = "") String valueToProcess) 
			throws JAXBException, JsonMappingException, JsonProcessingException
	{	
		switch (whatToProcess.toUpperCase()) {
		case "UPDATE_ALL":
			
			updateSessionVariables("UPDATE_ALL");
			return JSONObject.fromObject(session_matrices).toString();
			
		case "PROCESS_MATRIX":
			
			switch (session_Configurations.getBroadcaster().toUpperCase()) {
			case BoundaryAdvertUtil.DOAD_BOUNDARY_ADVERT:
				List<Matrix> thisMatrices = session_matrices.stream().filter(
					m -> m.getDescription().equalsIgnoreCase(valueToProcess)).collect(Collectors.toList());

				System.out.println("PROCESS_MATRIX: thisMatrices = " + thisMatrices);
					
				String everestCode = BoundaryAdvertFunctions.convertDataToDesigCode(
					session_Configurations.getBroadcaster(), new ObjectMapper().writeValueAsString(thisMatrices));
					
				System.out.println("everestCode = " + everestCode);
				print_writer.print("LAYER1*EVEREST*TREEVIEW*GRP*FUNCTION_SET_PROP*SCENE_BUILDER stdstrData=" + everestCode + ";");
				print_writer.print("LAYER1*EVEREST*TREEVIEW*GRP*FUNCTION_SET_PROP*SCENE_BUILDER ParamBuildScene=1;");
				
				return JSONArray.fromObject(thisMatrices).toString();
			}
			
		case "GET-CONFIG-DATA":
			
			session_Configurations = (Configurations)JAXBContext.newInstance(Configurations.class).createUnmarshaller().unmarshal(
				new File(BoundaryAdvertUtil.BOUNDARY_ADVERT_DIRECTORY+ BoundaryAdvertUtil.CONFIGURATIONS_DIRECTORY + valueToProcess));
			return JSONObject.fromObject(session_Configurations).toString();
			
		}
		return valueToProcess;
	}
}