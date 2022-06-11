package com.gsnotes.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Niveau;
import com.gsnotes.dao.INiveauDao;
import com.gsnotes.services.INiveauService;
import com.gsnotes.services.impl.NiveauServiceImpl;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class NiveauServiceImpl implements INiveauService {

	@Autowired
	private INiveauDao niveauDao;

	public List<Niveau> getAllNiveaux() {

		return niveauDao.findAll();
	}

	public void deleteNiveau(Long id) {
		niveauDao.deleteById(id);

	}

	public Niveau getNiveauById(long id) {
		return niveauDao.getById(id);

	}

	public void addNiveau(Niveau niveau) {
		niveauDao.save(niveau);

	}

	public void updateNiveau(Niveau niveau) {
		niveauDao.save(niveau);

	}

	public ExcelExporter prepareNiveauExport(List<InscriptionAnnuelle> inscriptions) {
		String[] columnNames = new String[] { "Etudiant", "Niveau" , "Année", "Etat", "Type", "Rang","Validation", "Mention" , "Plus Info" };
		String[][] data = new String[inscriptions.size()][9];

		int i = 0;
		for (InscriptionAnnuelle m : inscriptions) {
			data[i][0] = m.getEtudiant().getNom()+" "+m.getEtudiant().getPrenom();
			data[i][1] = m.getNiveau().getAlias();
			data[i][2] =String.valueOf(m.getAnnee());
			data[i][3] =String.valueOf(m.getEtat());
			data[i][4] =m.getType();
			data[i][5] =String.valueOf(m.getRang());
			data[i][6] =m.getValidation();
			data[i][7] =m.getMention();
			data[i][8] =m.getPlusInfos();
			
			i++;
		}

		return new ExcelExporter(columnNames, data, "niveaux");

	}

	public Niveau getNiveauByAlias(String alias) {

		return niveauDao.getNiveauByAlias(alias);

	}

}
