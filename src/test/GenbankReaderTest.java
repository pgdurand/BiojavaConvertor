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

public class GenbankReaderTest {

  private static final String gbFile1 = "./data/small_chromosome.gb";
  private static final String gbFile2 = "./data/A10086-pat.gb";

  private static final String sequence = 
      "gttttgtttgatggagaattgcgcagaggggttatatctgcgtgaggatctgtcactcggcggtgtgggatacctccctgctaaggcggg"+
      "ttgagtgatgttccctcggactggggaccgctggcttgcgagctatgtccgctactctcagtactacactctcatttgagcccccgctca"+
      "gtttgctagcagaacccggcacatggttcgccgataccatggaatttcgaaagaaacactctgttaggtggtatgagtcatgacgcacgc"+
      "agggagaggctaaggcttatgctatgctgatctccgtgaatgtctatcattcctccacaggaccc";
  
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
  public void testGenbankBasic(){
    //read sequence sample
    SequenceDataBag sdb = BiojavaUtils.readGenbankEntry(new File(gbFile2));

    //...then check that content is ok
    assertTrue(sdb.getSeqInfo().getId().equals("A10086"));
    assertTrue(sdb.getSeqInfo().getDescription().equals("Cucumber mosaic virus satellite I 17N."));
    assertTrue(sdb.getSeqInfo().getCreationDate()==20061114);
    assertTrue(sdb.getSeqInfo().getDivision().equals("PAT"));
    assertTrue(sdb.getSeqInfo().getMoltype().equals("RNA"));
    assertTrue(sdb.getSeqInfo().getOrganism().equals("Cucumber mosaic virus satellite RNA"));
    assertTrue(sdb.getSeqInfo().getTaxonomy().equals("Viruses;Satellites;Satellite Nucleic Acids;Single stranded RNA satellites;Small linear single stranded RNA satellites"));
    assertTrue(sdb.getSeqInfo().getTopology().equals("linear"));
    assertTrue(sdb.getSeqInfo().getSequenceSize()==335);
    assertTrue(sdb.getSeqInfo().getUpdateDate()==20061114);

    assertTrue(sdb.getSequence().length()==335);
    assertTrue(sdb.getSequence().equals(sequence));
    assertTrue(sdb.getFeatTable().features()==14);

    // Of note, the following objects are then ready to be used with Bioinformatics-UI viewers
    // (https://github.com/pgdurand/Bioinformatics-UI-API):
    //sdb.getDSequence(); // a sequence object
    //sdb.getFeatTable(); // its feature table
  }

  @Test
  public void testGenbankChromosome() {
    //read sequence sample
    SequenceDataBag sdb = BiojavaUtils.readGenbankEntry(new File(gbFile1));
    
    //...then check that content is ok
    assertTrue(sdb.getSeqInfo().getId().equals("NC_007200"));
    assertTrue(sdb.getSeqInfo().getDescription().equals("Aspergillus fumigatus Af293 chromosome 7, whole genome shotgun sequence."));
    assertTrue(sdb.getSeqInfo().getCreationDate()==20100414);
    assertTrue(sdb.getSeqInfo().getDivision().equals("CON"));
    assertTrue(sdb.getSeqInfo().getMoltype().equals("DNA"));
    assertTrue(sdb.getSeqInfo().getOrganism().equals("Aspergillus fumigatus Af293"));
    assertTrue(sdb.getSeqInfo().getTaxonomy().equals("Eukaryota;Fungi;Dikarya;Ascomycota;Pezizomycotina;Eurotiomycetes;Eurotiomycetidae;Eurotiales;Trichocomaceae;mitosporic Trichocomaceae;Aspergillus"));
    assertTrue(sdb.getSeqInfo().getTopology().equals("linear"));
    assertTrue(sdb.getSeqInfo().getSequenceSize()==2058334);
    assertTrue(sdb.getSeqInfo().getUpdateDate()==20100414);

    // in this particular example, we removed the sequence in the file. So we do not have it here.
    // It is worth noting that in such a case, sequence size can be retrieved from 
    // sdb.getSeqInfo().getSequenceSize(); see above.
    assertTrue(sdb.getSequence().length()==0);

    assertTrue(sdb.getFeatTable().features()==2130);
  }

}
