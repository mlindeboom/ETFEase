package com.etfease.restlet.servlets;

import java.io.File;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.data.Protocol;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

public class ETFListServerApplication extends Application {
	/** 
	 * Creates a root Restlet that will receive all incoming calls. 
	 */
	

	   @Override
	    public Restlet createInboundRoot() {
	        Router router = new Router(getContext());
	        getConnectorService().getClientProtocols().add(Protocol.FILE);

	        // Serve the files generated by the GWT compilation step.
	        File warDir = new File("");
	        if (!"war".equals(warDir.getName())) {
	            warDir = new File(warDir, "war/");
	        }

	        Directory dir = new Directory(getContext(), LocalReference
	                .createFileReference(warDir));
	        router.attachDefault(dir);
	        router.attach("/etflist", ETFListServerResource.class);
	        router.attach("/portfoliolist", PortfolioServerResource.class);


	        return router;
	    }
	
	
	
}
