package com.etudiants.formation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Main {

	public static void main(String[] args) {

		Connection cnx = null;
		Statement stmt = null;
		ResultSet rs = null;
		// Chargement pilote
		try {
			Class.forName("com.mysql.jdbc.Driver");

		} catch (ClassNotFoundException ex) {
			System.out.println("Echec chargement driver\nRaison invoqu�e\n" + ex.getClass().getSimpleName());

			System.exit(10);
		}
		// Creation et ouverture de connexion vers la base
		try {
			cnx = DriverManager.getConnection(
					"jdbc:mysql://localhost/etude_sql?user=utilisateur_sql&password=pwd_sql&useSSL=false");
		} catch (SQLException e) {
			System.out.println("Echec cr�ation connexion.\nException de type : " + e.getClass().getSimpleName()
					+ "\nMessage : " + e.getMessage());
			System.exit(20);
		}

		// Cr�ation d'un objet de classe Statement (cadre d'�xecution de
		// requete)

		try {
			stmt = cnx.createStatement();
		}

		catch (SQLException ex)

		{
			System.out.println(
					"Echec cr�ation connexion : " + ex.getClass().getSimpleName() + "\nMessage : " + ex.getMessage());
			System.exit(30);

		}

		// injection dans le statement d'une requete SQLet demande de son
		// execution
		String sql = null;

		// Selection de l'id, nom, pr�nom et pays
		sql = "select E.idEmploye, E.Nom, E.Prenom, P.Pays, E.DateNaissance, E.salaireMensuel, E.salaireMensuel * 12 as 'SalaireAnnuel', concat (R.Nom, ' ', R.Prenom) as 'Responsable' "
				+ "from employes E, pays P, employes R "
				+ "where E.idPays = P.idPays and  E.idResponsable = R.idEmploye;";
		System.out.println(sql);

		// Exercice 21 :
		// Selectionner le nom, prenom, salaire, Pays, date de naissance et nom
		// du reponsable des employes de la base etude_SQL
		// -Afficher leurs metadonn�s

		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException ex) {
			System.out
					.println("Echec requete SQL : " + ex.getClass().getSimpleName() + "\nMessage : " + ex.getMessage());
			System.exit(40);
		}

		// Le result set obtenu contient :
		// -des meta donn�es
		// -les donn�es selectionn�es

		// Recup�ration des m�tadonnn�es
		// -les bases, tables, colonnes d'o� viennent les colonnes
		// -le nombre de colonnes
		// -nom, type de chaque colonne

		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int nbColonnes = rsmd.getColumnCount();
			System.out.println("Nombre de colonnes selectionn�es : " + nbColonnes);

			//
			for (int i = 1; i <= nbColonnes; i++) {
				System.out.println("Colone " + i + " : " + "\n\tBase : " + rsmd.getCatalogName(i) + "\n\tTable : "
						+ rsmd.getTableName(i) + "\n\tColonne : " + rsmd.getColumnName(i) + "\n\tLib�l� colonne : "
						+ rsmd.getColumnLabel(i) + "\n\tType colonne : " + rsmd.getColumnTypeName(i)
						+ "\n\tTaille colonne : " + rsmd.getColumnDisplaySize(i) + "\n\tTaille d�cimales : "
						+ rsmd.getPrecision(i) + "\n\tNullable ? : " + (rsmd.isNullable(i) == 0 ? "Non" : "Oui")
						+ "\n\tAuto increment ? : " + (rsmd.isAutoIncrement(i) ? "Oui" : "Non")

				);
			}

		} catch (SQLException e) {
			System.out.println("Echec recup�ration ResultSet meta donn�es : " + e.getClass().getSimpleName()
					+ "\nMessage : " + e.getMessage());
			System.exit(50);
		}

		// R�cup�ration des donn�es selectionn�es
		// Remarque : L'objet result set ne contient pas les donn�es, il peut
		// �tre vu comme un cursor SQL
		// qui acc�de aux donn�es ligne par ligne et qui doit �tre deplac� de
		// ligne en ligne
		// O� pointe l'objet ResultSet juste apr�s sa cr�ation ?
		// R�ponse : son pointeur de lecture est avant la premiere ligne donc je
		// dois le positioner explicitement sur la premi�re ligne
		// Quand la derni�re ligne est atteinte, l'objet resultSet retourne
		// false

		try {
			while (rs.next()) {
				//L'objet rs contient 1 tuple de donn�es
				
				int id = rs.getInt(1);
				String nomEmploye = rs.getString("Nom");
				String prenomEmploye = rs.getString("Prenom");
				String pays = rs.getString("pays");
				Date dateNaissance = rs.getDate("dateNaissance");
				float salaire = rs.getFloat("salaireMensuel");
				float salaireAnnuel = rs.getFloat("SalaireAnnuel");
				String nomResponsable = rs.getString("Responsable");
				System.out.println("ID : " +id +"\n\t" 
				+ "Nom : " +nomEmploye +"\n\t" 
				+"Pr�nom " + prenomEmploye+"\n\t"  
				+"Pays : " + pays+"\n\t"  
				+"Date de naissance : " +dateNaissance+"\n\t"  
				+"Salaire : " +salaire +"\n\t" 
				+"Salaire annuel : " +salaireAnnuel+"\n\t"  
				+"Nom responsable : " +nomResponsable );
				
				
			} //Fin du while re.next

			
			
		} catch (SQLException e) {
			System.out.println("Echec lecture donn�es ResultSet : " + e.getClass().getSimpleName() + "\nMessage : "
					+ e.getMessage());
			System.exit(60);
		}
		
		finally{
			try {rs.close();} catch (SQLException e) {}
		}

		//C.R.U.D. - Create, Read, Update, Delete
		//Suppression
		sql = "delete from employes where nom like 'LeNom%'; ";
		try {
			int nb= stmt.executeUpdate(sql);
			System.out.println("Nombre de lignes supprim�es : "+nb);

		} catch (SQLException e) {
			System.out
			.println("Echec suppression donn�es : " + e.getClass().getSimpleName() + "\nMessage : " + e.getMessage());
	System.exit(80);
		}
		
		
		//Insertions
		sql = "insert into employes(Nom, prenom, dateNaissance, salaireMensuel, idResponsable, idPays, idSociete) "
				+"values "
				+"('LeNom1', 'LePrenom1', '1993-10-09', '1200', '3','1', 'soc3'), " 
				+"('LeNom2', 'LePrenom2', '1993-07-14', '1800', '2','3', 'soc2');"; 
		System.out.println(sql);
		try {
			int nb= stmt.executeUpdate(sql);
			System.out.println("Nombre de lignes Inser�es : "+nb);
		} catch (SQLException e) {
			System.out
			.println("Echec insertion/creation SQL : " + e.getClass().getSimpleName() + "\nMessage : " + e.getMessage());
	System.exit(70);
		}
		
		//Update
		String suffix =  "" + (new Date()).getTime(); 
		suffix = suffix.substring(suffix.length()-4);
		sql = "update employes " +
		"set " +
		"nom = concat(nom, '"+suffix+ "'), prenom=concat(prenom, '"+suffix+ "')"
				+ "where nom like 'LeNom%' and prenom like 'LePrenom%'";
		
		try {
			int nb = stmt.executeUpdate(sql);
			System.out.println("Nombre de lignes modifi�es : "+nb);
		} catch (SQLException e1) {
			System.out
			.println("Echec mise � jour SQL : " + e1.getClass().getSimpleName() + "\nMessage : " + e1.getMessage());
	System.exit(80);
		}
		
		//A part l'interface statement il en existe une autre, preparedStatement, qui permet d'executer des requetes param�tr�es
		//On l'utilise pour executer des ordres SQL qui ne diff�rent que par leurs parametres, pas par leur contenu
		
		//Insertion d'un seul employ� dans la table et r�petition de l'op�ration 3 fois
		
		//Requete d'insertion :
		
		sql = "insert into employes (nom, prenom) values (?, ?)";
		PreparedStatement prepStmt = null;
		
		try {
			prepStmt = cnx.prepareStatement(sql);
		} catch (SQLException e1) {

			System.out.println("Echec prepared statement : " + e1.getClass().getSimpleName() + "\nMessage : " + e1.getMessage());
	System.exit(100);
		}
		
		//Avant d'executer le prepared statement, je dois injecter les parametres
		
		try{
		for (int i=1; i<=3; i++)
		{
			//D�finition des parametres de la requete
			prepStmt.setString(1, "LeNom"+i+10);
			prepStmt.setString(2, "LePrenom"+i+10);
			
			//Execution de la requete
			int nb = prepStmt.executeUpdate();
			System.out.println("Nombre de lignes ins�r�es avec prepStmt : " +nb);
		}
		
	} catch (SQLException e1) {

		System.out.println("Echec prepared statement : " + e1.getClass().getSimpleName() + "\nMessage : " + e1.getMessage());
System.exit(110);
	}
		//Insertion de 3 employ�s en un seul prepared statement
		sql = "insert into employes (nom, prenom) values ";
		int nbEmployesAInserer = 4;
		for (int i=1; i<=nbEmployesAInserer; i++)
		{
			if (i>1){
				sql += ", ";
			}
			sql += "(?, ?)";
			
		}
		//Injection des param�tres et execution de la requete
		int positionParametre = 1;
		
		try {
			prepStmt = cnx.prepareStatement(sql);
			
			//Definition des parametres
			for (int i=1; i <= nbEmployesAInserer; i++)
			{
				prepStmt.setString(positionParametre++, "LeNom" + (100+i));
				prepStmt.setString(positionParametre++, "LePrenom" + (100+i));
				
			}
			
			int nb = prepStmt.executeUpdate();
			System.out.println("Lignes inser�es : " +nb);
		} catch (SQLException e1) {
			System.out.println("Erreur prepared statement avec x parametres : " + e1.getClass().getSimpleName() + "\nMessage : " + e1.getMessage());
			System.exit(120);
		}
		// Transaction = ensemble d'ordres SQL de modification qui doivent se comporter comme un seul ordre atomique
		// => Ces ordres doivent r�ussir ou echouer ensemble
		//Une transaction est valide si et seulement si tous les ordres SQL sont valides
		
		//Apr�s une transaction valid�e les BDD retrouvent leur �tat definitif
		//Apres une transaction invalide, les bases de donn�es retrouvent leur �tat initial
		
/*
		try {
			sql = "delete from employes";
			stmt.executeUpdate(sql);
			sql = "insert into employes (nom, prenom) values ('LeNomxx', 'LePrenomxx')";
			stmt.executeUpdate(sql);
			sql = "insert into employes (nom, prenom) values ('LeNomxx', 'LePrenomxx')";
			stmt.executeUpdate(sql);
			
		} catch (SQLException e1) {
			System.out.println("Echec execution sans transaction : " + e1.getClass().getSimpleName() + "\nMessage : " + e1.getMessage());
			System.exit(130);
		}
		*/
		//L'execution du troisi�me ordre SQL �choue car l'employ� � ins�rer existe d�j� dans la table
		//Resultat : le contenu de la table a �t� perdu
		
		//Je voudrais, en cas d'�chec retrouver l'�tat initial de la table
		//Pour cela, j'utilise une transaction qui contiendra les trois instructions SQL
		
		//Particularit� d'une connexion MySQL : elle traite chaque instruction de mani�re individuelle
		//en les validant s�par�ment
		
		//Pour �viter cela, je dois empecher la connexion d'auto commit chaque ordre SQL
		//De cette fa�on, la validation d'une transaction reste � ma charge car je veux gerer moi-m�me les transactions
		
		try {
			cnx.setAutoCommit(false);
		} catch (SQLException e1) {
			System.out.println("Echec set auto commit : " + e1.getClass().getSimpleName() + "\nMessage : " + e1.getMessage());
			System.exit(130);
		}
		try {
			sql = "delete from employes";
			stmt.executeUpdate(sql);
			
			sql = "insert into employes (nom, prenom) values ('LeNomxx', 'LePrenomxx')";
			stmt.executeUpdate(sql);
			
			sql = "insert into employes (nom, prenom) values ('LeNomxx', 'LePrenomxx')";
			stmt.executeUpdate(sql);
			
			// Si j'arrive ici, toutes les op�rations SQL ont �t� execut�es avec succ�s
			//Je valide donc la transaction
			cnx.commit();
			System.out.println("Transaction r�alis�e avec succ�s");
		}
		
		catch (SQLException e1) {
			System.out.println("Echec execution sans transaction : " + e1.getClass().getSimpleName() + "\nMessage : " + e1.getMessage());
			
			System.exit(130);
			//Je d�clare la transaction non valide et annule les modifs
			try{
				cnx.rollback();
				System.out.println("Modifications sur les tables annul�es");
			
			}catch(Exception e){}
		}
		
		//Exo 22
		//Afficher les capitales, leur population, leur pays et la langue officielle en utilisant une application Java JDBC et la base MySQL world. 
		
		//Fermeture des ressources
		try {
			stmt.close();
		} catch (SQLException e) {}
		try {
			prepStmt.close();
		} catch (SQLException e) {}
		try {
			cnx.close();
		} catch (SQLException e) {}
		
		System.out.println("OK");
	} // Fin methode main

}
