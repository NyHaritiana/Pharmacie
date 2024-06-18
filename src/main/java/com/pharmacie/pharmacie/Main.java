package com.pharmacie.pharmacie;


import com.pharmacie.pharmacie.components.Finance;

import java.util.Map;

public class Main {
    public static void main(String[] args) {

        ///Pdf pdf= new Pdf();
        ///pdf.creerFacture();
        ///Medicament medicament= new Medicament();
        //medicament.stockMaj();

        Map<String, Integer> monthlyRevenue = Finance.recetteParMois();
        monthlyRevenue.forEach((month, revenue) ->
                System.out.println(STR."Month: \{month}, Revenue: \{revenue}")
        );
    }
}
