package org.pql.query;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.Token;
import org.pql.core.IPQLBasicPredicatesOnTasks;
import org.pql.core.PQLAttribute;
import org.pql.core.PQLException;
import org.pql.core.PQLLocation;
import org.pql.core.PQLQuantifier;
import org.pql.core.PQLTask;
import org.pql.core.PQLTrace;

/**
 * Interface to a PQL query.
 * 
 * @author Artem Polyvyanyy
 */
public interface IPQLQuery {
	
	/**
	 * Check if a model (as per configuration, {@link IPQLQuery.configure(obj)}) matches this PQL query. 
	 * 
	 * @return {@code TRUE} if the model matches the query; {@code FALSE} otherwise.
	 */
	public boolean check();
	
	/**
	 * Configure this PQL query.
	 * 
	 * @param obj A configuration object.
	 * @throws PQLException 
	 */
	public void configure(Object obj) throws PQLException;

	/**
	 * Get all variable declarations of this PQL query.
	 * 
	 * @return The {@link Map} of all (variable name, set of tasks) key-value pairs declared in this PQL query.
	 */
	public Map<String, Set<PQLTask>> getVariables();

	/**
	 * Get all {@link PQLAttribute}s that are specified in this PQL query.
	 * 
	 * @return The {@link Set} of all {@link PQLAttribute}s specified in this PQL query.
	 */
	public Set<PQLAttribute> getAttributes();
	
	/**
	 * Get all {@link PQLLocation}s that are specified in this PQL query.
	 * 
	 * @return The {@link Set} of all {@link PQLLocation}s specified in this PQL query.
	 */
	public Set<PQLLocation> getLocations();
	
	/**
	 * Get task interpretations.
	 * 
	 * @return The {@link Map} from {@link PQLTask}s specified in this PQL query to interpreted {@link PQLTask}s, i.e., indexed tasks.
	 */
	public Map<PQLTask,PQLTask> getTaskMap();
	
	/**
	 * Get the number of syntax errors in the PQL query string.
	 * 
	 * @return The number of syntax errors in the PQL query string.
	 */
	public int getNumberOfParseErrors();
	
	/**
	 * Get PQL query string parse error messages.
	 * 
	 * @return The {@link List} of all parse error messages for this PQL query.
	 */
	public List<String> getParseErrorMessages();

	//A.P.
	public PQLTrace getInsertTrace();
	
	//A.P.
	public IPQLBasicPredicatesOnTasks getBP();
    
	//A.P.
	boolean interpretUnaryPredicateMacroV2(Token op, Set<PQLTask> tasks,
			PQLQuantifier Q);
    //A.P.
	boolean interpretBinaryPredicateMacro(Token op, Set<PQLTask> set1,
			Set<PQLTask> set2, PQLQuantifier Q);
}