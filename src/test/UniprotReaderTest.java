/* Copyright (C) 2006-2016 Patrick G. Durand
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  You may obtain a copy of the License at
 *
 *     https://www.gnu.org/licenses/agpl-3.0.txt
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 */
package test;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import bzh.plealog.bioinfo.api.core.config.CoreSystemConfigurator;
import bzh.plealog.bioinfo.seqvertor.BiojavaUtils;
import bzh.plealog.bioinfo.seqvertor.SequenceDataBag;

public class UniprotReaderTest {

  private static final String swFile = "./data/P12265.sw";
  
  private static final String sequence = 
    "MSLKWSACWVALGQLLCSCALALKGGMLFPKESPSRELKALDGLWHFRADLSNNRLQ"+
    "GFEQQWYRQPLRESGPVLDMPVPSSFNDITQEAALRDFIGWVWYEREAILPRRWTQDT"+
    "DMRVVLRINSAHYYAVVWVNGIHVVEHEGGHLPFEADISKLVQSGPLTTCRITIAINNTL"+ 
    "TPHTLPPGTIVYKTDTSMYPKGYFVQDTSFDFFNYAGLHRSVVLYTTPTTYIDDITVITN"+
    "VEQDIGLVTYWISVQGSEHFQLEVQLLDEDGKVVAHGTGNQGQLQVPSANLWWPYLMHEH"+
    "PAYMYSLEVKVTTTESVTDYYTLPVGIRTVAVTKSKFLINGKPFYFQGVNKHEDSDIRGK"+
    "GFDWPLLVKDFNLLRWLGANSFRTSHYPYSEEVLQLCDRYGIVVIDECPGVGIVLPQSFGN"+
    "ESLRHHLEVMEELVRRDKNHPAVVMWSVANEPSSALKPAAYYFKTLITHTKALDLTRPVTF"+
    "VSNAKYDADLGAPYVDVICVNSYFSWYHDYGHLEVIQPQLNSQFENWYKTHQKPIIQSEYG"+
    "ADAIPGIHEDPPRMFSEEYQKAVLENYHSVLDQKRKEYVVGELIWNFADFMTNQSPLRVIGN"+
    "KKGIFTRQRQPKTSAFILRERYWRIANETGGHGSGPRTQCFGSRPFTF";
  
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    // required to use the Plealog Bioinformatics library
    CoreSystemConfigurator.initializeSystem();
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testUniprotProt() {
    //read sequence sample
    SequenceDataBag sdb = BiojavaUtils.readUniProtEntry(new File(swFile));
    
    //...then check that content is ok
    assertTrue(sdb.getSeqInfo().getId().equals("P12265"));
    assertTrue(sdb.getSeqInfo().getDescription().equals("RecName: Full=Beta-glucuronidase;          EC=3.2.1.31; Flags: Precursor;"));
    assertTrue(sdb.getSeqInfo().getCreationDate()==19891001);
    assertTrue(sdb.getSeqInfo().getDivision().equals("not specified"));
    assertTrue(sdb.getSeqInfo().getMoltype().equals("aa"));
    assertTrue(sdb.getSeqInfo().getOrganism().equals("Mus musculus (Mouse)"));
    assertTrue(sdb.getSeqInfo().getTaxonomy().equals("Eukaryota;Metazoa;Chordata;Craniata;Vertebrata;Euteleostomi;Mammalia;Eutheria;Euarchontoglires;Glires;Rodentia;Sciurognathi;Muroidea;Muridae;Murinae;Mus"));
    assertTrue(sdb.getSeqInfo().getTopology().equals("linear"));
    assertTrue(sdb.getSeqInfo().getSequenceSize()==648);
    assertTrue(sdb.getSeqInfo().getUpdateDate()==20091124);

    assertTrue(sdb.getSequence().equals(sequence));
    assertTrue(sdb.getSequence().length()==648);

    assertTrue(sdb.getFeatTable().features()==16);

    // Of note, the following objects are then ready to be used with Bioinformatics-UI viewers
    // (https://github.com/pgdurand/Bioinformatics-UI-API):
    //sdb.getDSequence(); // a sequence object
    //sdb.getFeatTable(); // its feature table
  }

}
