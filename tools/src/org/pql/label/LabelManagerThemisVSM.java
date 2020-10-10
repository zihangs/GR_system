package org.pql.label;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.themis.ir.Document;
import org.themis.ir.vsm.VSM;
import org.themis.util.LETTERCASE;
import org.themis.util.PREPROCESS;
import org.themis.util.STEMMER;

/**
 * @author Artem Polyvyanyy
 */
public class LabelManagerThemisVSM extends AbstractLabelManagerMySQL {
	
	private VSM	vsm = null;
	
	public LabelManagerThemisVSM(Connection con,
			String pgHost, String pgName, String pgUser, String pgPassword, 
			double defaultSim, Set<Double> indexedSims) throws ClassNotFoundException, SQLException {
		super(con,defaultSim,indexedSims);
		
		this.vsm = new VSM(pgHost,pgName,pgUser,pgPassword);
		this.vsm.setParameter(PREPROCESS.LETTERCASE.toString(), LETTERCASE.UPPER.toString());
		this.vsm.setParameter(PREPROCESS.STEMMER.toString(), STEMMER.PORTER.toString());
	}
	
	@Override
	public int indexLabel(String label) throws SQLException {
		int labelID = this.createLabel(label);
		vsm.addDocument("LABEL UDDI: " + Integer.toString(labelID), label, false);
		return labelID;
	
	}
	
	@Override
	public Set<LabelScore> getSimilarLabels(String searchString, int n) {
		Set<LabelScore> result = new HashSet<LabelScore>();
		
		try {
			List<Document> search = this.vsm.searchFull(searchString,0,n);
			if (search!=null && !search.isEmpty()) {
				for (Document doc : search)
					result.add(new LabelScore(doc.getContent(),doc.getSimilarity()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@Override
	public Set<LabelScore> getSimilarLabels(String searchString, double sim) {
		Set<LabelScore> result = new HashSet<LabelScore>();
		
		try {
			List<Document> search = this.vsm.searchFull(searchString,0,20);
			if (search!=null && !search.isEmpty()) {
				for (Document doc : search)
					if (sim <= doc.getSimilarity())
						result.add(new LabelScore(doc.getContent(),doc.getSimilarity()));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}


}
