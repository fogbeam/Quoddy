package org.fogbeam.quoddy.semantics

import com.hp.hpl.jena.query.QuerySolution

class EntityQuerySolution 
{
	QuerySolution solution;
	String type;
	Entity entity;
	Object entityObject;
	
	public String toString()
	{
		return solution.toString();
	}
}
