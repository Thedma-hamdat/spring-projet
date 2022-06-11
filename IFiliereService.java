package com.gsnotes.services;

import java.util.List;

import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Filiere;
import com.gsnotes.utils.export.ExcelExporter;

public interface IFiliereService {

	public void addFiliere(Filiere filiere);

	public void updateFiliere(Filiere filiere);

	public List<Filiere> getAllFilieres();

	public void deleteFiliere(Long id);

	public Filiere getFiliereById(long idFiliere);
	
	public Filiere getFiliereByCode(String code);


}
