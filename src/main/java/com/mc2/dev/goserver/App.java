package com.mc2.dev.goserver;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.mc2.dev.resource.MatchResource;
import com.mc2.dev.resource.PlayResource;


@ApplicationPath("/")
public class App extends Application
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
    
	@Override
	public	Set<Class<?>>	getClasses()
	{
		Set<Class<?>>	classes	= new	HashSet<Class<?>>();
		classes.add(MatchResource.class);
		classes.add(PlayResource.class);
		return super.getClasses();
	}
}
