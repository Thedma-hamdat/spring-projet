package com.gsnotes.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Filiere;
import com.gsnotes.dao.IFiliereDao;
import com.gsnotes.services.IFiliereService;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class FiliereServiceImpl implements IFiliereService {

	@Autowired
	private IFiliereDao filiereDao;

	public List<Filiere> getAllFilieres() {

		return filiereDao.findAll();
	}

	public void deleteFiliere(Long id) {
		filiereDao.deleteById(id);

	}

	public Filiere getFiliereById(long id) {
		return filiereDao.getById(id);

	}

	public void addFiliere(Filiere filiere) {
		filiereDao.save(filiere);

	}

	public void updateFiliere(Filiere filiere) {
		filiereDao.save(filiere);

	}

	public Filiere getFiliereByCode(String code) {

		return filiereDao.getFiliereByCode(code);

	}


}
