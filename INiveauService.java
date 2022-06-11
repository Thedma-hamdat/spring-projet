package com.gsnotes.services;

import java.util.List;

import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Niveau;
import com.gsnotes.utils.export.ExcelExporter;

public interface INiveauService {

	public void addNiveau(Niveau niveau);

	public void updateNiveau(Niveau niveau);

	public List<Niveau> getAllNiveaux();

	public void deleteNiveau(Long id);

	public Niveau getNiveauById(long idNiveau);
	
	public Niveau getNiveauByAlias(String alias);
	
	public ExcelExporter prepareNiveauExport(List<InscriptionAnnuelle> inscriptions);
	
	

}
