package com.ibm.replication.iidr.igc.assets;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class Assets {

	private ArrayList<Datastore> datastores;
	private ArrayList<Subscription> subscriptions;
	private ArrayList<TableMapping> tableMappings;
	private ArrayList<ColumnMapping> columnMappings;
	private ArrayList<RuleSet> ruleSets;
	private ArrayList<RSTableMapping> rsTableMappings;

	private int datastoresID;
	private int subscriptionsID;
	private int tableMappingsID;
	private int columnMappingsID;
	private int ruleSetsID;
	private int rsTableMappingsID;

	final static Logger logger = Logger.getLogger(Assets.class.getName());

	public Assets() {
		datastores = new ArrayList<Datastore>();
		subscriptions = new ArrayList<Subscription>();
		tableMappings = new ArrayList<TableMapping>();
		columnMappings = new ArrayList<ColumnMapping>();
		ruleSets = new ArrayList<RuleSet>();
		rsTableMappings = new ArrayList<RSTableMapping>();

		datastoresID = 0;
		subscriptionsID = 0;
		tableMappingsID = 0;
		columnMappingsID = 0;
		ruleSetsID = 0;
		rsTableMappingsID = 0;

	}

	private String getNextDatastoreID() {
		++datastoresID;
		return "ds" + this.datastoresID;
	}

	private String getNextSubscriptionID() {
		++subscriptionsID;
		return "sub" + this.subscriptionsID;
	}

	private String getNextTableMappingID() {
		++tableMappingsID;
		return "tm" + this.tableMappingsID;
	}

	private String getNextColumnMappingID() {
		++columnMappingsID;
		return "cm" + this.columnMappingsID;
	}

	private String getNextRuleSetID() {
		++ruleSetsID;
		return "rs" + this.ruleSetsID;
	}

	private String getNextRSTableMappingID() {
		++rsTableMappingsID;
		return "rstm" + this.rsTableMappingsID;
	}

	public Datastore addDatastore(String name, String description, String hostName, String port, String version,
			String platform, String database, String type) {
		logger.debug(MessageFormat.format("Adding datastore asset {0} with host name {1} and port {2}",
				new Object[] { name, hostName, new Integer(port) }));
		Datastore returnDatastore = null;
		for (Datastore datastore : datastores) {
			if (datastore.getName().equals(name)) {
				returnDatastore = datastore;
				break;
			}
		}
		if (returnDatastore == null) {
			returnDatastore = new Datastore(getNextDatastoreID(), name, description, hostName, port, version, platform,
					database, type);
			datastores.add(returnDatastore);
		}
		return returnDatastore;
	}

	public Subscription addSubscription(String name, String description, String sourceDatastoreName,
			String targetDatastoreName, String sourceID, String hostName, String firewallPort, String persistency,
			Datastore parentDatastore) {
		logger.debug(
				MessageFormat.format("Adding subscription asset {0} with source datastore {1} and target datastore {2}",
						new Object[] { name, sourceDatastoreName, targetDatastoreName }));
		Subscription returnSubscription = null;
		for (Subscription subscription : subscriptions) {
			if (subscription.getClass().equals(name)) {
				returnSubscription = subscription;
				break;
			}
		}
		if (returnSubscription == null) {
			// TODO: Get ID of target datastore
			returnSubscription = new Subscription(getNextSubscriptionID(), name, description, sourceDatastoreName,
					targetDatastoreName, sourceID, hostName, firewallPort, persistency, parentDatastore.getID(),
					getDatastoreByName(targetDatastoreName).getID());
			subscriptions.add(returnSubscription);
		}
		return returnSubscription;
	}

	public TableMapping addTableToTableMapping(String source_schema, String source_table, String target_schema,
			String target_table, String mapping_type, String method, String prevent_recursion, String parent_id) {
		TableMapping returnTableMapping = null;
		logger.debug(
				MessageFormat.format("Adding table to table mapping for source table {0}.{1} to target table {2}.{3}",
						new Object[] { source_schema, source_table, target_schema, target_table }));
		returnTableMapping = new TableMapping(getNextTableMappingID(), source_schema, source_table, target_schema,
				target_table, mapping_type, method, prevent_recursion, parent_id);
		tableMappings.add(returnTableMapping);
		return returnTableMapping;
	}

	public TableMapping addTableToFlatFileMapping(String source_schema, String source_table, String directory,
			String mapping_type, String method, String prevent_recursion, String parent_id) {
		TableMapping returnTableMapping = null;
		logger.debug(MessageFormat.format("Adding table to table mapping for source table {0}.{1} to directory {3}",
				new Object[] { source_schema, source_table, directory }));
		returnTableMapping = new TableMapping(getNextTableMappingID(), source_schema, source_table, directory,
				mapping_type, method, prevent_recursion, parent_id);
		tableMappings.add(returnTableMapping);
		return returnTableMapping;
	}

	public ColumnMapping addColumnMapping(String source_column, String target_column, String initial_value,
			String parent_id) {
		ColumnMapping returnColumnMapping = null;
		logger.debug(MessageFormat.format("Adding source column {0} to target column {1}",
				new Object[] { source_column, target_column }));
		returnColumnMapping = new ColumnMapping(getNextColumnMappingID(), source_column, target_column, initial_value,
				parent_id);
		columnMappings.add(returnColumnMapping);
		return returnColumnMapping;
	}

	public RuleSet addRuleSet(String name, String schema, String include_tables, String exclude_tables,
			String structure_only, String context, String parent_id) {
		RuleSet returnRuleSet = null;
		logger.debug(MessageFormat.format("Adding rule set {0}", new Object[] { name }));
		returnRuleSet = new RuleSet(getNextRuleSetID(), name, schema, include_tables, exclude_tables, structure_only,
				context, parent_id);
		ruleSets.add(returnRuleSet);
		return returnRuleSet;
	}

	public RSTableMapping addRSTableMapping(String schema, String table, String structure_only, String parent_id) {
		RSTableMapping returnRSTableMapping = null;
		logger.debug(MessageFormat.format("Adding rule set table mapping for schema {0} and table {1}",
				new Object[] { schema, table }));
		returnRSTableMapping = new RSTableMapping(getNextRSTableMappingID(), schema, table, structure_only, parent_id);
		rsTableMappings.add(returnRSTableMapping);
		return returnRSTableMapping;
	}

	public Datastore datastoreExists(String name) {
		for (Datastore datastore : this.datastores) {
			if (datastore.getName().equalsIgnoreCase(name)) {
				return datastore;
			}
		}

		return null;
	}

	public boolean subscriptionExists(String name, String sourceDatastore) {
		for (Subscription subscription : this.subscriptions) {
			if (subscription.getName().equalsIgnoreCase(name)
					&& subscription.getSourceDatastore().equalsIgnoreCase(sourceDatastore)) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<Subscription> getSubscriptionsByParentID(String parentID) {
		ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();
		for (Subscription subscription : this.subscriptions) {
			if (subscription.getParentID().equalsIgnoreCase(parentID)) {
				subscriptions.add(subscription);
			}
		}
		return subscriptions;
	}

	public ArrayList<TableMapping> getTableMappingsByParentID(String parentID) {
		ArrayList<TableMapping> tableMappings = new ArrayList<TableMapping>();
		for (TableMapping tableMapping : this.tableMappings) {
			if (tableMapping.getParentID().equalsIgnoreCase(parentID)) {
				tableMappings.add(tableMapping);
			}
		}
		return tableMappings;
	}

	public ArrayList<RSTableMapping> getRSTableMappingsByParentID(String parentID) {
		ArrayList<RSTableMapping> tableMappings = new ArrayList<RSTableMapping>();
		for (RSTableMapping tableMapping : this.rsTableMappings) {
			if (tableMapping.getParentID().equalsIgnoreCase(parentID)) {
				tableMappings.add(tableMapping);
			}
		}
		return tableMappings;
	}

	public ArrayList<RuleSet> getRuleSetsByParentID(String parentID) {

		ArrayList<RuleSet> ruleSets = new ArrayList<RuleSet>();

		for (RuleSet ruleSet : this.ruleSets) {
			if (ruleSet.getParentID().equalsIgnoreCase(parentID)) {
				ruleSets.add(ruleSet);
			}
		}

		return ruleSets;
	}

	public ArrayList<ColumnMapping> getColumnMappingsByParentID(String parentID) {

		ArrayList<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>();

		for (ColumnMapping columnMapping : this.columnMappings) {
			if (columnMapping.getParentID().equalsIgnoreCase(parentID)) {
				columnMappings.add(columnMapping);
			}
		}

		return columnMappings;
	}

	public ArrayList<ColumnMapping> getColumnMappingByParentID(String parentID) {
		ArrayList<ColumnMapping> columnMappings = new ArrayList<ColumnMapping>();
		for (ColumnMapping columnMapping : this.columnMappings) {
			if (columnMapping.getParentID().equalsIgnoreCase(parentID)) {
				columnMappings.add(columnMapping);
			}
		}

		return columnMappings;
	}

	public String toXML() {
		String assetsXML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
				+ "<doc xmlns=\"http://www.ibm.com/iis/flow-doc\">\n" + "\t<assets>\n";

		String completeAssetIDs = "";
		for (Datastore datastore : this.datastores) {
			assetsXML += datastore.toAssetXML();
			completeAssetIDs += datastore.getID() + " ";
		}

		for (Subscription subscription : this.subscriptions) {
			assetsXML += subscription.toAssetXML();
		}

		for (TableMapping tableMapping : this.tableMappings) {
			assetsXML += tableMapping.toAssetXML();
		}

		for (RuleSet ruleSet : this.ruleSets) {
			assetsXML += ruleSet.toXML();
		}

		for (ColumnMapping columnMapping : this.columnMappings) {
			assetsXML += columnMapping.toXML();
		}

		for (RSTableMapping rsTableMapping : this.rsTableMappings) {
			assetsXML += rsTableMapping.toXML();
		}

		assetsXML += "\t</assets>\n";
		assetsXML += "\t<importAction partialAssetIDs=\"" + completeAssetIDs + "\" />\n";
		assetsXML += "</doc>";
		return assetsXML;
	}

	public ArrayList<String> listDatastores() {
		ArrayList<String> datastoresList = new ArrayList<String>();
		for (Datastore datastore : this.datastores) {
			datastoresList.add(datastore.toString());
		}
		return datastoresList;
	}

	public ArrayList<String> listSubscriptions() {
		ArrayList<String> subscriptionList = new ArrayList<String>();
		for (Subscription subscription : this.subscriptions) {
			subscriptionList.add(subscription.toString());
		}
		return subscriptionList;
	}

	public ArrayList<String> listTableMappings() {
		ArrayList<String> tableMAppingList = new ArrayList<String>();

		for (TableMapping tableMapping : this.tableMappings) {
			tableMAppingList.add(tableMapping.toString());
		}
		return tableMAppingList;
	}

	public ArrayList<String> listRuleSets() {
		ArrayList<String> ruleSetList = new ArrayList<String>();
		for (RuleSet ruleSet : this.ruleSets) {
			ruleSetList.add(ruleSet.toString());
		}
		return ruleSetList;
	}

	public ArrayList<String> listColumnMappings() {
		ArrayList<String> columnMappingList = new ArrayList<String>();
		for (ColumnMapping columnMapping : this.columnMappings) {
			columnMappingList.add(columnMapping.toString());
		}
		return columnMappingList;
	}

	public ArrayList<String> listRSTableMappings() {
		ArrayList<String> rsTableMAppingList = new ArrayList<String>();
		for (RSTableMapping rsTableMapping : this.rsTableMappings) {
			rsTableMAppingList.add(rsTableMapping.toString());
		}
		return rsTableMAppingList;
	}

	public ArrayList<Datastore> getDatastores() {
		return this.datastores;
	}

	public ArrayList<Subscription> getSubscriptions() {
		return this.subscriptions;
	}

	public ArrayList<TableMapping> getTableMappings() {
		return this.tableMappings;
	}

	public ArrayList<RSTableMapping> getRSTableMappings() {
		return this.rsTableMappings;
	}

	public ArrayList<TableMapping> getTableMappingsByType(int type) {

		ArrayList<TableMapping> foundTableMappings = new ArrayList<TableMapping>();

		for (TableMapping tableMapping : this.tableMappings) {
			if (tableMapping.getTableMappingType() == type) {
				foundTableMappings.add(tableMapping);
			}
		}

		return foundTableMappings;
	}

	public ArrayList<ColumnMapping> getColumnMappings() {
		return this.columnMappings;
	}

	public Subscription getSubscription(String id) {

		for (Subscription subscription : this.subscriptions) {
			if (subscription.getID().equalsIgnoreCase(id)) {
				return subscription;
			}
		}

		return null;
	}

	public Datastore getDatastoreByID(String id) {
		for (Datastore datastore : datastores) {
			if (datastore.getID().equalsIgnoreCase(id)) {
				return datastore;
			}
		}
		return null;
	}

	public Datastore getDatastoreByName(String name) {
		for (Datastore datastore : datastores) {
			if (datastore.getName().equals(name)) {
				return datastore;
			}
		}
		return null;
	}

}
