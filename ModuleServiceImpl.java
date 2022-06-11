package com.gsnotes.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gsnotes.bo.Module;
import com.gsnotes.dao.IModuleDao;
import com.gsnotes.services.IModuleService;
import com.gsnotes.services.impl.ModuleServiceImpl;
import com.gsnotes.utils.export.ExcelExporter;

@Service
@Transactional
public class ModuleServiceImpl implements IModuleService {

	@Autowired
	private IModuleDao moduleDao;

	public List<Module> getAllModules() {

		return moduleDao.findAll();
	}

	public void deleteModule(Long id) {
		moduleDao.deleteById(id);

	}

	public Module getModuleById(long id) {
		return moduleDao.getById(id);

	}

	public void addModule(Module module) {
		moduleDao.save(module);

	}

	public void updateModule(Module module) {
		moduleDao.save(module);

	}

	public ExcelExporter prepareModuleExport(List<Module> modules) {
		String[] columnNames = new String[] { "Titre", "Code", "Niveau" };
		String[][] data = new String[modules.size()][3];

		int i = 0;
		for (Module m : modules) {
			data[i][0] = m.getTitre();
			data[i][1] = m.getCode();
			data[i][1] = m.getNiveau().toString();
			i++;
		}

		return new ExcelExporter(columnNames, data, "modules");

	}

	public Module getModuleByCode(String code) {

		return moduleDao.getModuleByCode(code);

	}

}
