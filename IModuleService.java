package com.gsnotes.services;

import java.util.List;

import com.gsnotes.bo.Module;
import com.gsnotes.utils.export.ExcelExporter;

public interface IModuleService {

	public void addModule(Module module);

	public void updateModule(Module module);

	public List<Module> getAllModules();

	public void deleteModule(Long id);

	public Module getModuleById(long idModule);
	
	public Module getModuleByCode(String code);
	
	public ExcelExporter prepareModuleExport(List<Module> modules);
	
	

}
