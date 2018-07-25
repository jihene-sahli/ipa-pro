package com.imaginepartners.imagineworkflow.controller.template;

import com.imaginepartners.imagineworkflow.dashboard.data.DocxData;
import com.imaginepartners.imagineworkflow.form.FormTemplate;
import com.imaginepartners.imagineworkflow.models.business.BizFournisseur;
import com.imaginepartners.imagineworkflow.models.business.BizLigneCommande;
import com.imaginepartners.imagineworkflow.models.rh.AldFormation;
import com.imaginepartners.imagineworkflow.util.FacesUtil;
import org.activiti.engine.task.TaskInfo;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.text.DecimalFormat;

/**
 * Created by Rafik on 28/03/17.
 */
public class Produits extends FormTemplate implements Serializable {

	private ArrayList<BizLigneCommande> produits;

	private ArrayList<BizLigneCommande> removedProduits;

	private List<String> codeBudgetList;

	private String loggedUser;

	private String initiator;

	private String taskId;

	private TaskInfo currentTask;

	private int taskNumber;

	private BizLigneCommande produitsTotal;

	private boolean showTemplateProduit;

	@Override
	public void putVars() {

	}

	@Override
	public void init() {
		if (varValues.get("produittotal") != null) {

			produitsTotal = (BizLigneCommande) varValues.get("produittotal");
		} else
			produitsTotal = new BizLigneCommande();
		if (varValues.get("produits") != null) {

			produits = (ArrayList<BizLigneCommande>) varValues.get("produits");
		} else
			produits = new ArrayList<BizLigneCommande>();


		removedProduits = new ArrayList<BizLigneCommande>();

		loggedUser = userService.getLoggedInUser().getId();

		if (varValues.get("initiator") != null)
			initiator = (String) varValues.get("initiator");
		else
			initiator = "";

		codeBudgetList = Arrays.asList(
		"ANA.",
		"BAL",
		"BD3",
		"BDES",
		"BDON",
		"BID",
		"BL",
		"BOBI",
		"BOIT",
		"BON",
		"BOUT",
		"BT",
		"BTE",
		"CARN",
		"CART",
		"CM",
		"CT",
		"ENS",
		"ES",
		"FARD",
		"FEUI",
		"FL",
		"FLAC",
		"FUT",
		"G",
		"H",
		"J",
		"J= 8",
		"J=02",
		"J=04",
		"J=05",
		"J=06",
		"J=08",
		"J=09",
		"J=12",
		"J=16",
		"J=3",
		"J=5",
		"JEU",
		"JEU=",
		"JEUX",
		"JR",
		"KG",
		"KILO",
		"KIT",
		"KM",
		"L",
		"LITR",
		"LONG",
		"LTR",
		"M",
		"M2",
		"M2 .",
		"ML",
		"MOIS",
		"NB",
		"PACK",
		"PAIR",
		"PAIRE",
		"PAQT",
		"PAQU",
		"PCE",
		"PIEC",
		"PN",
		"PQ",
		"PQ100",
		"PQ200",
		"PQ250",
		"PQ50",
		"PQ500",
		"QNT",
		"R",
		"RAME",
		"RLX",
		"RX",
		"SAC.",
		"SACH",
		"sachet",
		"SACHET10",
		"SCH",
		"SCHT",
		"TON",
		"TONE",
		"TUBE",
		"U",
		"UN",
		"UNIT"
		);
		String procDefId = null;
		if (StringUtils.isNotBlank(FacesUtil.getUrlParam("tacheHist"))) {
			taskId = FacesUtil.getUrlParam("tacheHist");
			currentTask = activitiService.getTaskHistById(taskId);
			taskNumber = Integer.parseInt(currentTask.getName().substring(1, 3));

		} else {
			taskId = StringUtils.isNotBlank(FacesUtil.getUrlParam("task")) ? FacesUtil.getUrlParam("task") : FacesUtil.getUrlParam("taskAutonome");
			currentTask = activitiService.getTask(taskId);
			taskNumber = Integer.parseInt(currentTask.getName().substring(1, 3));
		}

		procDefId = currentTask.getProcessDefinitionId();

		if (procDefId != null && procDefId.contains("Effectuerunachat"))
			showTemplateProduit = false;
		else
			showTemplateProduit = true;
	}

	public void addProduit() {
		produits.add(new BizLigneCommande());
	}

	public void deleteProduit(int rowId) {
		removedProduits.add(produits.get(rowId));
		produits.remove(rowId);
	}

	@Override
	public Map<String, Object> getVarValuesReport() {

		Map<String, Object> mapReport = new HashMap<String, Object>();
		List<BizLigneCommande> totalProduits = new ArrayList<BizLigneCommande>();
		if (produits != null && !produits.isEmpty()) {
			mapReport.put("produits_data", getProduitsDocxData(produits));
		}
		if (produitsTotal != null) {
			String textBD;
			if (produitsTotal.getTotal() != null && produitsTotal.getTotalHorsTaxe() != null) {
				if (produitsTotal.getTaux() == null || produitsTotal.getTaux().intValue() == 0) {
					mapReport.put("tvaTotal", "0");
				} else if (produitsTotal.getTaux() != null && produitsTotal.getTaux().intValue() != 0) {
					mapReport.put("tvaTotal", produitsTotal.getTva().toPlainString());
				}
				mapReport.put("montant", (produitsTotal.getTotal()).toPlainString());
				mapReport.put("totalHT", (produitsTotal.getTotalHorsTaxe().toPlainString()));

				textBD = produitsTotal.getTotal().toPlainString();
				int radixLoc = textBD.indexOf('.');
				String vir = "";
				if (radixLoc > 0) {
					vir = textBD.substring(radixLoc + 1, textBD.length());
					if (vir.length() == 1)
						mapReport.put("paieLettreVir", convert(Long.parseLong(vir) * 100).toString());
					else if (vir.length() == 2)
						mapReport.put("paieLettreVir", convert(Long.parseLong(vir) * 10).toString());
				} else if (radixLoc < 0 || radixLoc == 0)
					mapReport.put("paieLettreVir", "zéro");
				mapReport.put("paieLettreEnt", convert(produitsTotal.getTotal().longValue()).toString());

			}
		}
		return mapReport;
	}

	private DocxData getProduitsDocxData(List listProduits) {
		DocxData data = new DocxData();
		List<String> placeholders = new ArrayList<String>();

		placeholders.add("desc");
		placeholders.add("quant");
		//placeholders.add("ref");
		placeholders.add("prixU");
		placeholders.add("montantHT");
		placeholders.add("taux");
		placeholders.add("tva");
		placeholders.add("montantTTC");
		List<Map<String, String>> rows = new ArrayList<Map<String, String>>();
		Map<String, String> row;

		for (Object produit : listProduits) {
			row = new HashMap<String, String>();
			if (produit != null && ((BizLigneCommande) produit).getDescription() != null) {
				row.put("desc", ((BizLigneCommande) produit).getDescription());
			}
			if (produit != null && ((BizLigneCommande) produit).getQuantite() != null) {
				row.put("quant", ((BizLigneCommande) produit).getQuantite().toPlainString());
			}
			//if(produit!= null && ((BizLigneCommande) produit).getReference() != null){
			//	row.put("ref",  ((BizLigneCommande) produit).getReference());
			//}
			if (produit != null && ((BizLigneCommande) produit).getPrixUnitaireHt() != null) {
				row.put("prixU", ((BizLigneCommande) produit).getPrixUnitaireHt().toPlainString());
			}
			if (produit != null && ((BizLigneCommande) produit).getTotalHorsTaxe() != null) {
				row.put("HT", ((BizLigneCommande) produit).getTotalHorsTaxe().toPlainString());
			}
			if (produit != null && ((BizLigneCommande) produit).getTaux() != null) {
				row.put("taux", ((BizLigneCommande) produit).getTaux().toPlainString());
			}
			if (produit != null && ((BizLigneCommande) produit).getTva() != null) {
				row.put("tva", ((BizLigneCommande) produit).getTva().toPlainString());
			}
			if (produit != null && ((BizLigneCommande) produit).getTotal() != null) {
				row.put("TTC", ((BizLigneCommande) produit).getTotal().toPlainString());
			}

			rows.add(row);
		}
		data.setColumns(placeholders);
		data.setRows(rows);
		return data;
	}

	public ArrayList<BizLigneCommande> getProduits() {
		return produits;
	}

	public void setProduits(ArrayList<BizLigneCommande> produits) {
		this.produits = produits;
	}

	public List<String> getCodeBudgetList() {
		return codeBudgetList;
	}

	public void setCodeBudgetList(List<String> codeBudgetList) {
		this.codeBudgetList = codeBudgetList;
	}

	public String getLoggedUser() {
		return loggedUser;
	}

	public void setLoggedUser(String loggedUser) {
		this.loggedUser = loggedUser;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public int getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}

	public BizLigneCommande getProduitsTotal() {
		return produitsTotal;
	}

	public void setProduitsTotal(BizLigneCommande produitsTotal) {
		this.produitsTotal = produitsTotal;
	}

	@Override
	public Map<String, Object> getVarValues() {
		ArrayList<BizLigneCommande> produitsToSave = new ArrayList<BizLigneCommande>();
		BizLigneCommande produitsTotalToSave = new BizLigneCommande();
		for (BizLigneCommande produit : produits) {
			produitsToSave.add(produit);
		}
		businessService.saveEntityList(produitsToSave);
		produitsTotalToSave = produitsTotal;
		varValues.put("produits", produitsToSave);
		businessService.saveEntite(produitsTotal);
		varValues.put("produittotal", produitsTotalToSave);
		for (BizLigneCommande produit : removedProduits) {
			if (produit.getId() != null)
				businessService.removeEntite(produit);
		}
		return varValues;
	}

	public void calculTotal(BizLigneCommande produit) {
		BigDecimal tva = new BigDecimal(1.17).setScale(2, RoundingMode.HALF_UP);
		produit.setTotalHorsTaxe(produit.getPrixUnitaireHt().multiply(produit.getQuantite()));
		produit.setTotal(produit.getTotalHorsTaxe().multiply(tva));
	}

	public Boolean greaterThan(int a, int b) {
		if (a > b)
			return true;
		return false;
	}

	public void calculTotal() {
		if (!produits.isEmpty()) {
			for (BizLigneCommande produit : produits) {
				if (produit.getPrixUnitaireHt() != null && produit.getQuantite() != null) {
					produit.setTotalHorsTaxe(produit.getPrixUnitaireHt().multiply(produit.getQuantite()));
					BigDecimal tva = null;
					if (produit.getTaux() != null && produit.getTaux().intValue() != 0){
						tva = produit.getTotalHorsTaxe().multiply(produit.getTaux().divide(new BigDecimal("100")));
						if (tva!=null){
							produit.setTva(tva);
						    produit.setTotal(produit.getTotalHorsTaxe().add(tva));
						}
					}
					else if (produit.getTaux() == null || produit.getTaux().intValue() == 0){
						produit.setTotal(produit.getPrixUnitaireHt().multiply(produit.getQuantite()));
						produit.setTva(BigDecimal.ZERO);
					}

				}
			}
		}
		produitsTotal.setTotalHorsTaxe(new BigDecimal(0));
		produitsTotal.setTva(new BigDecimal(0));
		produitsTotal.setTotal(new BigDecimal(0));
		produitsTotal.setTaux(new BigDecimal(0));
		for (BizLigneCommande produit : produits) {
			try {
				produitsTotal.setTotalHorsTaxe(produitsTotal.getTotalHorsTaxe().add(produit.getTotalHorsTaxe()));
			} catch (Exception ex) {
			}
			try {
				produitsTotal.setTaux(produitsTotal.getTaux().add(produit.getTaux()));
			} catch (Exception ex) {
			}
			try {
				produitsTotal.setTva(produitsTotal.getTva().add(produit.getTva()));
			} catch (Exception ex) {
			}
			try {
				produitsTotal.setTotal(produitsTotal.getTotal().add(produit.getTotal()));
			} catch (Exception ex) {
			}

		}
	}

	String[] dizaineNames = {
	"",
	"",
	"vingt",
	"trente",
	"quarante",
	"cinquante",
	"soixante",
	"soixante",
	"quatre-vingt",
	"quatre-vingt"
	};

	String[] unite1 = {
	"",
	"un",
	"deux",
	"trois",
	"quatre",
	"cinq",
	"six",
	"sept",
	"huit",
	"neuf",
	"dix",
	"onze",
	"douze",
	"treize",
	"quatorze",
	"quinze",
	"seize",
	"dix-sept",
	"dix-huit",
	"dix-neuf"
	};

	String[] unite2 = {
	"",
	"",
	"deux",
	"trois",
	"quatre",
	"cinq",
	"six",
	"sept",
	"huit",
	"neuf",
	"dix"
	};

	String convertZeroToHundred(int number) {

		int laDizaine = number / 10;
		int lUnite = number % 10;
		String resultat = "";

		switch (laDizaine) {
			case 1:
			case 7:
			case 9:
				lUnite = lUnite + 10;
				break;
			default:
		}

		//  et
		String laLiaison = "";
		if (laDizaine > 1) {
			laLiaison = "-";
		}
		// cas particuliers
		switch (lUnite) {
			case 0:
				laLiaison = "";
				break;
			case 1:
				if (laDizaine == 8) {
					laLiaison = "-";
				} else {
					laLiaison = " et ";
				}
				break;
			case 11:
				if (laDizaine == 7) {
					laLiaison = " et ";
				}
				break;
			default:
		}

		// dizaines en lettres
		switch (laDizaine) {
			case 0:
				resultat = unite1[lUnite];
				break;
			case 8:
				if (lUnite == 0) {
					resultat = dizaineNames[laDizaine];
				} else {
					resultat = dizaineNames[laDizaine]
					+ laLiaison + unite1[lUnite];
				}
				break;
			default:
				resultat = dizaineNames[laDizaine]
				+ laLiaison + unite1[lUnite];
		}
		return resultat;
	}

	String convertLessThanOneThousand(int number) {

		int lesCentaines = number / 100;
		int leReste = number % 100;
		String sReste = convertZeroToHundred(leReste);

		String resultat;
		switch (lesCentaines) {
			case 0:
				resultat = sReste;
				break;
			case 1:
				if (leReste > 0) {
					resultat = "cent " + sReste;
				} else {
					resultat = "cent";
				}
				break;
			default:
				if (leReste > 0) {
					resultat = unite2[lesCentaines] + " cent " + sReste;
				} else {
					resultat = unite2[lesCentaines] + " cents";
				}
		}
		return resultat;
	}

	String convert(long number) {

		if (number == 0) {
			return "zéro";
		}

		String snumber = Long.toString(number);

		// pad des "0"
		String mask = "000000000000";
		DecimalFormat df = new DecimalFormat(mask);
		snumber = df.format(number);

		// lesMilliards
		int lesMilliards = Integer.parseInt(snumber.substring(0, 3));
		// lesMillions
		int lesMillions = Integer.parseInt(snumber.substring(3, 6));
		// lesCentMille
		int lesCentMille = Integer.parseInt(snumber.substring(6, 9));
		// nnnnnnnnnXXX
		int lesMille = Integer.parseInt(snumber.substring(9, 12));

		String tradMilliards;
		switch (lesMilliards) {
			case 0:
				tradMilliards = "";
				break;
			case 1:
				tradMilliards = convertLessThanOneThousand(lesMilliards)
				+ " milliard ";
				break;
			default:
				tradMilliards = convertLessThanOneThousand(lesMilliards)
				+ " milliards ";
		}
		String resultat = tradMilliards;

		String tradMillions;
		switch (lesMillions) {
			case 0:
				tradMillions = "";
				break;
			case 1:
				tradMillions = convertLessThanOneThousand(lesMillions)
				+ " million ";
				break;
			default:
				tradMillions = convertLessThanOneThousand(lesMillions)
				+ " millions ";
		}
		resultat = resultat + tradMillions;

		String tradCentMille;
		switch (lesCentMille) {
			case 0:
				tradCentMille = "";
				break;
			case 1:
				tradCentMille = "mille ";
				break;
			default:
				tradCentMille = convertLessThanOneThousand(lesCentMille)
				+ " mille ";
		}
		resultat = resultat + tradCentMille;

		String tradMille;
		tradMille = convertLessThanOneThousand(lesMille);
		resultat = resultat + tradMille;

		return resultat;
	}

	public boolean isShowTemplateProduit() {
		return showTemplateProduit;
	}

	public void setShowTemplateProduit(boolean showTemplateProduit) {
		this.showTemplateProduit = showTemplateProduit;
	}
}
