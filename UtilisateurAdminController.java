package com.gsnotes.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.gsnotes.bo.CadreAdministrateur;
import com.gsnotes.bo.Compte;
import com.gsnotes.bo.Enseignant;
import com.gsnotes.bo.Etudiant;
import com.gsnotes.bo.Filiere;
import com.gsnotes.bo.InscriptionAnnuelle;
import com.gsnotes.bo.Module;
import com.gsnotes.bo.Niveau;
import com.gsnotes.bo.Utilisateur;
import com.gsnotes.services.ICompteService;
import com.gsnotes.services.IFiliereService;
import com.gsnotes.services.IModuleService;
import com.gsnotes.services.INiveauService;
import com.gsnotes.services.IPersonService;
import com.gsnotes.services.impl.ModuleServiceImpl;
import com.gsnotes.utils.export.ExcelExporter;
import com.gsnotes.web.models.ModuleModel;
import com.gsnotes.web.models.PersonModel;
import com.gsnotes.web.models.UserAndAccountInfos;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Ce controleur gère les personnes de type Etudiant, Enseignant et Cadre Admin
 * 
 * @author Boudaa
 *
 */

@Controller
@RequestMapping("/admin")
public class UtilisateurAdminController {

	@Autowired
	private IPersonService personService;
	
	@Autowired
	private IModuleService moduleService;
	
	@Autowired
	private IFiliereService filiereService;
	
	@Autowired
	private INiveauService niveauService;
	
	@Autowired
	private HttpSession httpSession;
	
	
	/** Utilisé pour la journalisation */
	private Logger LOGGER = LoggerFactory.getLogger(getClass());


	public UtilisateurAdminController() {
	}

	@RequestMapping(value = "showForm", method = RequestMethod.GET)
	public String showForm(@RequestParam int typePerson, Model model) {

		PersonModel pmodel = new PersonModel(typePerson);
		model.addAttribute("personModel", pmodel);

		// Nous avons choisit d'utiliser une classe modèle personnalisée
		// définie par PersonModel pour une meilleur flexibilité

		List<Utilisateur> persons = personService.getAllPersons();
		List<PersonModel> modelPersons = new ArrayList<PersonModel>();
		// On copie les données des personnes vers le modèle
		for (int i = 0; i < persons.size(); i++) {
			PersonModel pm = new PersonModel();
			if (persons.get(i) instanceof Etudiant) {
				// permet de copier les données d'un objet à l'autre à codition
				// d'avoir les meme attributs (getters/setters)
				BeanUtils.copyProperties((Etudiant) persons.get(i), pm);
				// On fixe le type (cet attribut ne sera pas copié automatiquement)
				pm.setTypePerson(PersonModel.TYPE_STUDENT);

				// Mettre la personne dans le modèle
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof Enseignant) {

				BeanUtils.copyProperties((Enseignant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
				modelPersons.add(pm);
			}
		}

		// Mettre la liste des personnes dans le modèle de Spring MVC
		model.addAttribute("personList", modelPersons);

		return "admin/form";
	}

	@RequestMapping(value = "addPerson", method = RequestMethod.POST)
	public String process(@Valid @ModelAttribute("personModel") PersonModel person, BindingResult bindingResult,
			Model model, HttpServletRequest rq) {

		// En cas d'erreur de validation
		if (bindingResult.hasErrors()) {
			// rq.setAttribute("typePerson", +person.getTypePerson());
			return "admin/form";
		}

		// Copier les données de l'objet PersonModel vers l'objet Etudiant (cas du
		// formulaire de l'étudiant)
		if (person.getTypePerson() == PersonModel.TYPE_STUDENT) {
			Etudiant etd = new Etudiant();
			BeanUtils.copyProperties(person, etd);

			personService.addPerson(etd);

		}
		// Copier les données de l'objet PersonModel vers l'objet Enseignant (cas du
		// formulaire de l'Enseignant)

		else if (person.getTypePerson() == PersonModel.TYPE_PROF) {
			Enseignant prof = new Enseignant();
			BeanUtils.copyProperties(person, prof);
			personService.addPerson(prof);

		}
		// Copier les données de l'objet PersonModel vers l'objet CadreAdministrateur
		// (cas du
		// formulaire de CadreAdministrateur)
		else if (person.getTypePerson() == PersonModel.TYPE_CADRE_ADMIN) {
			CadreAdministrateur ca = new CadreAdministrateur();
			BeanUtils.copyProperties(person, ca);
			personService.addPerson(ca);

		}

		// rediriger vers l'action shwoForm avec le meme type de personne
		return "redirect:/admin/showForm?typePerson=" + person.getTypePerson();
	}

	@RequestMapping(value = "updatePersonForm/{idPerson}", method = RequestMethod.GET)
	public String updatePersonForm(@PathVariable int idPerson, Model model) {

		// On reoit comme paramètre l'id de la personne à mettre à jour
		Utilisateur utl = personService.getPersonById(Long.valueOf(idPerson));

		// On construit le modèle
		PersonModel pm = new PersonModel();

		// En fonction du type de l'utilisateur à modifier
		// Ceci va nous pemettre d'afficher un formulaire adapté
		// selon le type de la personne
		if (utl instanceof Etudiant) {
			BeanUtils.copyProperties((Etudiant) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_STUDENT);
		} else if (utl instanceof Enseignant) {
			BeanUtils.copyProperties((Enseignant) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_PROF);
		} else if (utl instanceof CadreAdministrateur) {
			BeanUtils.copyProperties((CadreAdministrateur) utl, pm);
			pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
		}

		// Initialiser le modele avec la personne
		model.addAttribute("personModel", pm);

		return "admin/updateForm";
	}

	@RequestMapping(value = "serachPerson", method = RequestMethod.GET)
	public String serachPerson(@RequestParam String cin, Model model) {

		// On reoit comme paramètre l'id de la personne à mettre à jour
		Utilisateur utl = personService.getPersonByCin(cin);

		if (utl == null) {

			// Initialiser le modele avec la personne
			model.addAttribute("personModel", new ArrayList<PersonModel>());
		} else {

			// On construit le modèle
			PersonModel pm = new PersonModel();

			// En fonction due type de l'utilisateur à modifier
			// Ceci va nous pemettre d'afficher un formulaire adapté
			// slon le type de la personne
			if (utl instanceof Etudiant) {
				BeanUtils.copyProperties((Etudiant) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_STUDENT);
			} else if (utl instanceof Enseignant) {
				BeanUtils.copyProperties((Enseignant) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
			} else if (utl instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) utl, pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);

			}
			List<PersonModel> modelPersons = new ArrayList<PersonModel>();
			modelPersons.add(pm);
			// Initialiser le modele avec la personne
			model.addAttribute("personList", modelPersons);
		}
		return "admin/listPersons";
	}

	@RequestMapping("updatePerson")
	public String updatePerson(@Valid @ModelAttribute("personModel") PersonModel person, BindingResult bindingResult,
			Model model) {

		// En cas d'erreur
		if (bindingResult.hasErrors()) {

			return "admin/updateForm";
		}

		// On copie les données du modèle vers l'objet métier puis on appel le service
		// pour faire la mise à jour
		if (person.getTypePerson() == PersonModel.TYPE_STUDENT) {
			Etudiant etd = new Etudiant();
			BeanUtils.copyProperties(person, etd);

			personService.updatePerson(etd);

		} else if (person.getTypePerson() == PersonModel.TYPE_PROF) {
			Enseignant prof = new Enseignant();
			BeanUtils.copyProperties(person, prof);
			personService.updatePerson(prof);

		} else if (person.getTypePerson() == PersonModel.TYPE_CADRE_ADMIN) {
			CadreAdministrateur ca = new CadreAdministrateur();
			BeanUtils.copyProperties(person, ca);
			personService.updatePerson(ca);

		}

		// Mettre le message de succès dans le modèle
		model.addAttribute("msg", "Opération effectuée avec succès");

		return "admin/updateForm";
	}

	@RequestMapping("managePersons")
	public String managePersons(Model model) {

		List<Utilisateur> persons = personService.getAllPersons();
		List<PersonModel> modelPersons = new ArrayList<PersonModel>();

		// Copier les objets metier vers PersonModel plus flexible
		for (int i = 0; i < persons.size(); i++) {
			PersonModel pm = new PersonModel();
			if (persons.get(i) instanceof Etudiant) {
				BeanUtils.copyProperties((Etudiant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_STUDENT);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof Enseignant) {
				BeanUtils.copyProperties((Enseignant) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_PROF);
				modelPersons.add(pm);
			} else if (persons.get(i) instanceof CadreAdministrateur) {
				BeanUtils.copyProperties((CadreAdministrateur) persons.get(i), pm);
				pm.setTypePerson(PersonModel.TYPE_CADRE_ADMIN);
				modelPersons.add(pm);
			}
		}

		model.addAttribute("personList", modelPersons);

		return "admin/listPersons";
	}

	@RequestMapping(value = "deletePerson/{idPerson}", method = RequestMethod.GET)
	public String delete(@PathVariable int idPerson) {

		personService.deletePerson(Long.valueOf(idPerson));

		return "redirect:/admin/managePersons";
	}

	@GetMapping("exportPersons")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		String currentDateTime = dateFormatter.format(new Date());

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
		response.setHeader(headerKey, headerValue);

		List<Utilisateur> persons = personService.getAllPersons();

		ExcelExporter excelExporter = personService.preparePersonExport(persons);

		excelExporter.export(response);
	}
	
	
	
	
	///////*****Partie Module*****\\\\\\\\\

	
	 @GetMapping("/addmodule")
	    public String add(Model model) {
	    	List<Module> listmodule = moduleService.getAllModules();
	        model.addAttribute("listmodule", listmodule);
	        model.addAttribute("module", new Module());
	        return "admin/ajouterModule";
	    }
	 
	    @RequestMapping(value = "/saveModule", method = RequestMethod.POST)
	    public String saveModule(@ModelAttribute("module") Module module) {
	        moduleService.addModule(module);
	        return "/admin/formModule";
	    }

	    @RequestMapping("/editModule/{idModule}")
	    public ModelAndView showEditModulePage(@PathVariable(name = "idModule") long idModule) {
	        ModelAndView mav = new ModelAndView("addmodule");
	        Module module = moduleService.getModuleById(idModule);
	        mav.addObject("module", module);
	        return mav;
	        
	    }
	    @RequestMapping("/deleteModule/{idModule}")
	    public String deleteModulePage(@PathVariable(name = "idModule") long idModule) {
	    	moduleService.deleteModule(idModule);
	        return "redirect:/admin/formModule";
	    }
	    
	    
	///////*****Partie Niveau*****\\\\\\\\\
	    
	    @GetMapping("/addniveau")
	    public String addNiveau(Model mod) {
	    	List<Niveau> listniveau = niveauService.getAllNiveaux();
	        mod.addAttribute("listniveau", listniveau);
	        mod.addAttribute("niveau", new Niveau());
	        return "admin/ajouterNiveau";
	    }
	 
	    @RequestMapping(value = "/saveNiveau", method = RequestMethod.POST)
	    public String saveNiveau(@ModelAttribute("niveau") Niveau niveau) {
	        niveauService.addNiveau(niveau);
	        return "/admin/formNiveau";
	    }

	    @RequestMapping("/editModule/{idNiveau}")
	    public ModelAndView showEditNiveauPage(@PathVariable(name = "idNiveau") long idNiveau) {
	        ModelAndView mav = new ModelAndView("addniveau");
	        Niveau niveau = niveauService.getNiveauById(idNiveau);
	        mav.addObject("niveau", niveau);
	        return mav;
	        
	    }
	    @RequestMapping("/deleteNiveau/{idNiveau}")
	    public String deleteNiveauPage(@PathVariable(name = "idNiveau") long idNiveau) {
	    	niveauService.deleteNiveau(idNiveau);
	        return "redirect:/admin/formNiveau";
	    }
	    
	    
	    
	    
	    
	///////*****Partie Filiere*****\\\\\\\\\
	    
		    @GetMapping("/addfiliere")
		    public String addFiliere(Model mod) {
		    	List<Filiere> listfiliere = filiereService.getAllFilieres();
		        mod.addAttribute("listfiliere", listfiliere);
		        mod.addAttribute("filiere", new Filiere());
		        return "admin/ajouterFiliere";
		    }
		 
		    @RequestMapping(value = "/saveFiliere", method = RequestMethod.POST)
		    public String saveFiliere(@ModelAttribute("filiere") Filiere filiere) {
		        filiereService.addFiliere(filiere);
		        return "/admin/formFiliere";
		    }

		    @RequestMapping("/editFiliere/{idFiliere}")
		    public ModelAndView showEditFilierePage(@PathVariable(name = "idFiliere") long idFiliere) {
		        ModelAndView mav = new ModelAndView("addfiliere");
		        Filiere filiere = filiereService.getFiliereById(idFiliere);
		        mav.addObject("filiere", filiere);
		        return mav;
		        
		    }
		    @RequestMapping("/deleteFiliere/{idFiliere}")
		    public String deleteFilierePage(@PathVariable(name = "idFiliere") long idFiliere) {
		    	niveauService.deleteNiveau(idFiliere);
		        return "redirect:/admin/formFiliere";
		    }
	    
}